package org.spring.todo.service.impl;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.spring.todo.exceptions.TodoNotFoundException;
import org.spring.todo.mapper.TodoMapper;
import org.spring.todo.model.entity.Todo;
import org.spring.todo.model.request.TodoRequest;
import org.spring.todo.model.response.TodoResponse;
import org.spring.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TodoServiceImpl
 * 
 * @author TinhPP
 *
 */
@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper todoMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	/**
	 * Get all Todo
	 */
	@Override
	public List<TodoResponse> getAllTodo() {
		return todoMapper.getAllTodo();
	}

	/**
	 * Save todo
	 */
	@Override
	@Transactional
	public Todo saveTodo(TodoRequest todoRequest) {
		Todo todo = modelMapper.map(todoRequest, Todo.class);
		todoMapper.insertTodo(todo);
		return todo;
	}

	/**
	 * Update todo
	 * @throws NotFoundException 
	 */
	@Override
	@Transactional
	public Todo updateTodo(TodoRequest todoRequest) {
		Todo todo = modelMapper.map(todoRequest, Todo.class);
		Todo existingTodo = todoMapper.getTodoById((long)todo.getId());
		if (existingTodo == null) {
			throw new TodoNotFoundException("Work Name with ID: '" + todo.getId()
			+ "' cannot be updated because it doesn't exist.");
		}
		todoMapper.updateTodo(todo);
		return todo;
	}

	/**
	 * Delete todo
	 */
	@Override
	@Transactional
	public Todo deleteTodo(Long id) {
		Todo existingTodo = todoMapper.getTodoById(id);
		if (existingTodo == null) {
			throw new TodoNotFoundException("Work Name with ID: " + id
			+ "' cannot be deleted because it doesn't exist.");
		}
		todoMapper.deleteTodo(id);
		return existingTodo;
	}
}
