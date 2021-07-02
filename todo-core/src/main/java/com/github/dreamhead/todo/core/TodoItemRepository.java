package com.github.dreamhead.todo.core;

public interface TodoItemRepository {
    TodoItem save(TodoItem item);

    Iterable<TodoItem> findAll();

    long count();
}
