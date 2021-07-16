package org.spring.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.spring.todo.model.entity.Todo;
import org.spring.todo.model.response.TodoResponse;

@Mapper
public interface TodoMapper {
	List<TodoResponse> getAllTodo();
	Todo getTodoById(Long id);
	void insertTodo(Todo todo);
	void updateTodo(Todo todo);
	void deleteTodo(Long id);
}
