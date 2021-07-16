package org.spring.todo.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.todo.common.Utils;
import org.spring.todo.exceptions.TodoNotFoundException;
import org.spring.todo.model.request.TodoRequest;
import org.spring.todo.model.response.TodoResponse;
import org.spring.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

	private static Logger logger = LoggerFactory.getLogger(TodoController.class);
	
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/todolist")
	public ResponseEntity<?> getAllTodo() {
		logger.info("GET request access '/todolist' path.");
		try {
			List<TodoResponse> listTodo = todoService.getAllTodo();
			return new ResponseEntity<>(listTodo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Nothing found", HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping("/todolist")
	public ResponseEntity<?> addNewTodoItem(@Valid @RequestBody TodoRequest todoRequest, BindingResult result) {
		logger.info("POST request access '/todolist' path with item: {}", todoRequest);
		try {
			// Validate
	        ResponseEntity<?> errorMap = Utils.validation(result);
	        if(errorMap!=null) return errorMap;
	        errorMap = Utils.validationDate(todoRequest.getStartingDate(), todoRequest.getEndingDate());
	        if(errorMap!=null) return errorMap;
	        // Save
			todoService.saveTodo(todoRequest);
			return new ResponseEntity<>("Create new TODO successfully!", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Create new TODO failed!", HttpStatus.CONFLICT);
		}
	}
	
	@PutMapping("/todolist")
	public ResponseEntity<?> updateTodoItem(@Valid @RequestBody TodoRequest todoRequest, BindingResult result) {
		logger.info("PUT request access '/todolist' path with item {}", todoRequest);
		try {
			// Validate
	        ResponseEntity<?> errorMap = Utils.validation(result);
	        if(errorMap!=null) return errorMap;
	        errorMap = Utils.validationDate(todoRequest.getStartingDate(), todoRequest.getEndingDate());
	        if(errorMap!=null) return errorMap;
	        // Update
			todoService.updateTodo(todoRequest);
			return new ResponseEntity<>("Updated TODO successfully!", HttpStatus.OK);
		} catch (TodoNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Updated TODO failed!", HttpStatus.CONFLICT);
		}
	}
	
	@DeleteMapping("/todolist/{id}")
	public ResponseEntity<String> deleteTodoItem(@PathVariable("id") Long id) {
		logger.info("DELETE request access '/todolist/{}' path.", id);
		try {
			todoService.deleteTodo(id);
			return new ResponseEntity<>("Deleted TODO successfully!", HttpStatus.OK);
		} catch (TodoNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>("Deleted TODO failed!", HttpStatus.CONFLICT);
		}
	}
}
