package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.TodoStatus;
import com.example.dditlms.domain.entity.Todo;
import com.example.dditlms.domain.repository.TodoRepository;
import com.example.dditlms.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    @Override
    @Transactional
    public void saveTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    @Transactional
    public void deleteTodo(long id) {
        Optional<Todo> todoWrapper =todoRepository.findById(id);
        Todo todo = todoWrapper.orElse(null);
        todoRepository.delete(todo);
    }

    @Override
    @Transactional
    public void checkTodo(long id) {
        Optional<Todo> todoWrapper =todoRepository.findById(id);
        Todo todo = todoWrapper.orElse(null);
        if(todo.getTodoStatus().equals(TodoStatus.TODO)){
            todo.setTodoStatus(TodoStatus.DONE);
        }else{
            todo.setTodoStatus(TodoStatus.TODO);
        }
        todoRepository.save(todo);
    }
}
