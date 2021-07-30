package com.github.dreamhead.todo.core;

import com.google.common.collect.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TodoItemService {
    private final TodoItemRepository repository;

    @Autowired
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

        final Optional<TodoItem> optionalItem = StreamSupport.stream(all.spliterator(), false)
                .filter(element -> element.getIndex() == index.getIndex())
                .findFirst();
        return optionalItem.flatMap(this::doMarkAsDone);
    }

    private Optional<TodoItem> doMarkAsDone(final TodoItem todoItem) {
        todoItem.markDone();
        return Optional.of(this.repository.save(todoItem));
    }

    public List<TodoItem> list(final boolean all) {
        return Streams.stream(this.repository.findAll())
                .filter(item -> all || !item.isDone())
                .collect(Collectors.toList());
    }
}
