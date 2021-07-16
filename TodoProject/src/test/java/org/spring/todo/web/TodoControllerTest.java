package org.spring.todo.web;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.spring.todo.exceptions.TodoNotFoundException;
import org.spring.todo.model.entity.Todo;
import org.spring.todo.model.request.TodoRequest;
import org.spring.todo.model.response.TodoResponse;
import org.spring.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private TodoService todoService;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testGetAllTodo_001() throws Exception {
		List<TodoResponse> listTodo = new ArrayList<>();
		TodoResponse todo = new TodoResponse();
		todo.setId(1);
		todo.setWorkName("abc");
		todo.setStartingDate(new Date());
		todo.setEndingDate(new Date());
		todo.setStatus("Planning");
		listTodo.add(todo);
		Mockito.when(todoService.getAllTodo()).thenReturn(listTodo);

		MvcResult mvcResulst = mockMvc.perform(get("/api/v1/todo/todolist")).andExpect(status().isOk()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		List<TodoResponse> listActual = mapper.readValue(actual, new TypeReference<List<TodoResponse>>() {
		});
		assertEquals(1, listActual.size());
		assertEquals(todo.getId(), listActual.get(0).getId());
		assertEquals(todo.getWorkName(), listActual.get(0).getWorkName());
		assertEquals(todo.getStartingDate(), listActual.get(0).getStartingDate());
		assertEquals(todo.getEndingDate(), listActual.get(0).getEndingDate());
		assertEquals(todo.getStatus(), listActual.get(0).getStatus());
	}
	
	@Test
	public void testGetAllTodo_002() throws Exception {
		List<TodoResponse> listTodo = new ArrayList<>();
		//record 1
		TodoResponse todo1 = new TodoResponse();
		todo1.setId(1);
		todo1.setWorkName("xyz");
		todo1.setStartingDate(new Date());
		todo1.setEndingDate(new Date());
		todo1.setStatus("Doing");
		listTodo.add(todo1);
		//record 2
		TodoResponse todo2 = new TodoResponse();
		todo2.setId(2);
		todo2.setWorkName("abc");
		todo2.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2020-12-22"));
		todo2.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2020-12-23"));
		todo2.setStatus("Planning");
		listTodo.add(todo2);
		//record 3
		TodoResponse todo3 = new TodoResponse();
		todo3.setId(3);
		todo3.setWorkName("123");
		todo3.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo3.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo3.setStatus("Complete");
		listTodo.add(todo3);
		Mockito.when(todoService.getAllTodo()).thenReturn(listTodo);
		
		MvcResult mvcResulst = mockMvc.perform(get("/api/v1/todo/todolist"))
				.andExpect(status().isOk()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		List<TodoResponse> listActual = mapper.readValue(actual, new TypeReference<List<TodoResponse>>(){});
		
		assertEquals(3, listActual.size());
		
		assertEquals(todo1.getId(),listActual.get(0).getId());
		assertEquals(todo1.getWorkName(),listActual.get(0).getWorkName());
		assertEquals(todo1.getStartingDate(),listActual.get(0).getStartingDate());
		assertEquals(todo1.getEndingDate(),listActual.get(0).getEndingDate());
		assertEquals(todo1.getStatus(),listActual.get(0).getStatus());
		
		assertEquals(todo2.getId(),listActual.get(1).getId());
		assertEquals(todo2.getWorkName(),listActual.get(1).getWorkName());
		assertEquals(todo2.getStartingDate(),listActual.get(1).getStartingDate());
		assertEquals(todo2.getEndingDate(),listActual.get(1).getEndingDate());
		assertEquals(todo2.getStatus(),listActual.get(1).getStatus());
		
		assertEquals(todo3.getId(),listActual.get(2).getId());
		assertEquals(todo3.getWorkName(),listActual.get(2).getWorkName());
		assertEquals(todo3.getStartingDate(),listActual.get(2).getStartingDate());
		assertEquals(todo3.getEndingDate(),listActual.get(2).getEndingDate());
		assertEquals(todo3.getStatus(),listActual.get(2).getStatus());
	}
	
	@Test
	public void testGetAllTodo_003() throws Exception{
		Mockito.when(todoService.getAllTodo()).thenThrow(TodoNotFoundException.class);
		MvcResult mvcResulst = mockMvc.perform(get("/api/v1/todo/todolist"))
				.andExpect(status().isConflict()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("Nothing found", actual);
	}
	
	@Test
	public void testAddNewTodoItem_001() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName(null);
		todo.setStartingDate(new Date());
		todo.setEndingDate(new Date());
		todo.setStatus("Planning");
		
		String json = mapper.writeValueAsString(todo);

		MvcResult mvcResulst = mockMvc.perform(post("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("{\"workName\":\"Work Name field is required\"}", actual);
	}
	
	@Test
	public void testAddNewTodoItem_002() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-22"));
		todo.setStatus("Planning");
		
		String json = mapper.writeValueAsString(todo);

		MvcResult mvcResulst = mockMvc.perform(post("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("{\"endingDate\":\"Ending Date is after Starting Date\"}", actual);
	}
	
	@Test
	public void testAddNewTodoItem_003() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-24"));
		todo.setStatus("Planning");
		String json = mapper.writeValueAsString(todo);

		Mockito.when(todoService.saveTodo(new TodoRequest())).thenReturn(new Todo());
		
		MvcResult mvcResulst = mockMvc.perform(post("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("Create new TODO successfully!", actual);
	}
	
	@Test
	public void testUpdateTodoItem_001() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName(null);
		todo.setStartingDate(new Date());
		todo.setEndingDate(new Date());
		todo.setStatus("Planning");
		
		String json = mapper.writeValueAsString(todo);

		MvcResult mvcResulst = mockMvc.perform(put("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("{\"workName\":\"Work Name field is required\"}", actual);
	}
	
	@Test
	public void testUpdateTodoItem_002() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-22"));
		todo.setStatus("Planning");
		
		String json = mapper.writeValueAsString(todo);

		MvcResult mvcResulst = mockMvc.perform(put("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("{\"endingDate\":\"Ending Date is after Starting Date\"}", actual);
	}
	@Test
	public void testUpdateTodoItem_003() throws Exception {
		TodoRequest todo = new TodoRequest();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-24"));
		todo.setStatus("Planning");
		String json = mapper.writeValueAsString(todo);

		Mockito.when(todoService.updateTodo(new TodoRequest())).thenReturn(new Todo());
		
		MvcResult mvcResulst = mockMvc.perform(put("/api/v1/todo/todolist").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("Updated TODO successfully!", actual);
	}
	
	@Test
	public void testDeleteTodoItem_001() throws Exception {
		Todo todo = new Todo();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-24"));
		todo.setStatus("Planning");
		Mockito.when(todoService.deleteTodo(ArgumentMatchers.any())).thenReturn(todo);

		MvcResult mvcResulst = mockMvc.perform(delete("/api/v1/todo/todolist/1")).andExpect(status().isOk()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("Deleted TODO successfully!", actual);
	}
	
	@Test
	public void testDeleteTodoItem_002() throws Exception {
		Todo todo = new Todo();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-24"));
		todo.setStatus("Planning");
		Mockito.when(todoService.deleteTodo(ArgumentMatchers.any())).thenThrow(RuntimeException.class);

		MvcResult mvcResulst = mockMvc.perform(delete("/api/v1/todo/todolist/1")).andExpect(status().isConflict()).andReturn();
		String actual = mvcResulst.getResponse().getContentAsString();
		assertEquals("Deleted TODO failed!", actual);
	}
	
	@Test
	public void testDeleteTodoItem_003() throws Exception {
		Todo todo = new Todo();
		todo.setWorkName("abc");
		todo.setStartingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-23"));
		todo.setEndingDate(new SimpleDateFormat("yyyyy-MM-dd").parse("2019-12-24"));
		todo.setStatus("Planning");
		Mockito.when(todoService.deleteTodo(ArgumentMatchers.any())).thenThrow(TodoNotFoundException.class);

		mockMvc.perform(delete("/api/v1/todo/todolist/1")).andExpect(status().isNotFound()).andReturn();
	}
}
