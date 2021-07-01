package com.github.dreamhead.todo.core;

import com.google.common.collect.Iterables;

import java.util.Optional;

public class TodoItemService {
    private final TodoRepository repository;

    public TodoItemService(final TodoRepository repository) {
        this.repository = repository;
    }

    public TodoItem addTodoItem(final TodoParameter todoParameter) {
        if (todoParameter == null) {
            throw new IllegalArgumentException("Null or empty content is not allowed");
        }

        final TodoItem item = new TodoItem(todoParameter.getContent());
        return this.repository.save(item);
    }

    public Optional<TodoItem> markTodoItemDone(final int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index should be greater than 0");
        }

        final Iterable<TodoItem> all = this.repository.findAll();

        try {
            final TodoItem todoItem = Iterables.get(all, index - 1);
            todoItem.markDone();
            return Optional.of(this.repository.save(todoItem));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
