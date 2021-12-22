package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Todo;

public interface TodoService {
    public void saveTodo(Todo todo);
    public void deleteTodo(long id);
    public void checkTodo(long id);
}
