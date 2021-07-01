package com.github.dreamhead.todo.core;

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
}
