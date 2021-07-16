package org.spring.todo.service;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.spring.todo.model.entity.Todo;
import org.spring.todo.model.request.TodoRequest;
import org.spring.todo.model.response.TodoResponse;

/**
 *  TodoService
 * 
 * @author TinhPP
 *
 */
public interface TodoService {
	List<TodoResponse> getAllTodo();
	Todo saveTodo(TodoRequest todoRequest);
	Todo updateTodo(TodoRequest todoRequest) throws NotFoundException;
	Todo deleteTodo(Long id);
}
