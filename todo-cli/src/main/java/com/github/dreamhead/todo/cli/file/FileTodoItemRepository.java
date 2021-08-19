package com.github.dreamhead.todo.cli.file;

import com.github.dreamhead.todo.core.TodoItem;
import com.github.dreamhead.todo.core.TodoItemRepository;
import com.github.dreamhead.todo.util.Jsons;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FileTodoItemRepository implements TodoItemRepository {
    private final File file;

    public FileTodoItemRepository(final File file) {
        this.file = file;
    }

    @Override
    public TodoItem save(final TodoItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Todo item should not be null");
        }

        final Iterable<TodoItem> items = findAll();
        final Iterable<TodoItem> newContent = toSaveContent(item, items);
        Jsons.writeObjects(this.file, newContent);
        return item;
    }

    private Iterable<TodoItem> toSaveContent(final TodoItem newItem, final Iterable<TodoItem> items) {
        if (newItem.getIndex() == 0) {
            long newIndex = Iterables.size(items) + 1;
            newItem.assignIndex(newIndex);

            return ImmutableList.<TodoItem>builder()
                    .addAll(items)
                    .add(newItem)
                    .build();
        }

        return StreamSupport.stream(items.spliterator(), false)
                .map(item -> updateItem(newItem, item))
                .collect(Collectors.toList());
    }

    private TodoItem updateItem(final TodoItem newItem, final TodoItem item) {
        if (item.getIndex() == newItem.getIndex()) {
            return newItem;
        }

        return item;
    }

    @Override
    public Iterable<TodoItem> findAll() {
        if (this.file.length() == 0) {
            return ImmutableList.of();
        }

        return Jsons.toObjects(this.file);
    }

    @Override
    public Optional<TodoItem> findByContent(final String content) {
        throw new UnsupportedOperationException();
    }

}
