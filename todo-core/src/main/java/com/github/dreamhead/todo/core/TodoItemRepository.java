package com.github.dreamhead.todo.core;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TodoItemRepository extends Repository<TodoItem, Long> {
    TodoItem save(TodoItem item);

    Iterable<TodoItem> findAll();

   Optional<TodoItem> findByContent(String content);
}
