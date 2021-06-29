package com.github.dreamhead.todo.core;

public class TodoService {
    private final TodoRepository repository;

    public TodoService(final TodoRepository repository) {
        this.repository = repository;
    }

    public TodoItem addTodoItem(final TodoParameter todoParameter) {
        final TodoItem item = new TodoItem(todoParameter.getContent());
        return this.repository.save(item);
    }
}
