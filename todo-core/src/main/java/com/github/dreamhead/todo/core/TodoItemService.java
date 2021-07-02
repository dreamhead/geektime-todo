package com.github.dreamhead.todo.core;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoItemService {
    private final TodoItemRepository repository;

    public TodoItemService(final TodoItemRepository repository) {
        this.repository = repository;
    }

    public TodoItem addTodoItem(final TodoParameter todoParameter) {
        if (todoParameter == null) {
            throw new IllegalArgumentException("Null or empty content is not allowed");
        }

        final TodoItem item = new TodoItem(todoParameter.getContent());
        return this.repository.save(item);
    }

    public Optional<TodoItem> markTodoItemDone(final TodoIndexParameter index) {
        final Iterable<TodoItem> all = this.repository.findAll();

        try {
            final TodoItem todoItem = Iterables.get(all, index.getIndex() - 1);
            todoItem.markDone();
            return Optional.of(this.repository.save(todoItem));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<TodoItem> list(final boolean all) {
        return Streams.stream(this.repository.findAll())
                .filter(item -> all || !item.isDone())
                .collect(Collectors.toList());
    }
}
