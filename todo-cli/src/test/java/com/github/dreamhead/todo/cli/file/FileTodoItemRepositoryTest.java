package com.github.dreamhead.todo.cli.file;

import com.github.dreamhead.todo.core.TodoItem;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FileTodoItemRepositoryTest {
    @TempDir
    File tempDir;
    private File tempFile;
    private FileTodoItemRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        this.tempFile = File.createTempFile("file", "", tempDir);
        this.repository = new FileTodoItemRepository(this.tempFile);
    }

    @Test
    public void should_find_nothing_for_empty_repository() throws IOException {
        final Iterable<TodoItem> items = repository.findAll();
        assertThat(items).hasSize(0);
    }

    @Test
    public void should_find_saved_items() {
        repository.save(new TodoItem("foo"));
        repository.save(new TodoItem("bar"));
        final Iterable<TodoItem> items = repository.findAll();
        assertThat(items).hasSize(2);
        final TodoItem firstItem = Iterables.get(items, 0);
        assertThat(firstItem.getContent()).isEqualTo("foo");
        assertThat(firstItem.getIndex()).isEqualTo(1);
        final TodoItem secondItem = Iterables.get(items, 1);
        assertThat(secondItem.getContent()).isEqualTo("bar");
        assertThat(secondItem.getIndex()).isEqualTo(2);
    }

    @Test
    public void should_find_saved_content_item(){
        repository.save(new TodoItem("foo"));
        final Optional<TodoItem> todoItemSaved = repository.findByContent("foo");
        assertThat(todoItemSaved).isPresent();
        final Optional<TodoItem> todoItemNotExisted = repository.findByContent("bar");
        assertThat(todoItemNotExisted).isNotPresent();
    }

    @Test
    public void should_update_saved_items() {
        repository.save(new TodoItem("foo"));
        repository.save(new TodoItem("bar"));
        final Iterable<TodoItem> items = repository.findAll();
        final TodoItem toUpdate = Iterables.get(items, 0);
        toUpdate.markDone();

        repository.save(toUpdate);

        final Iterable<TodoItem> updated = repository.findAll();
        assertThat(updated).hasSize(2);
        assertThat(Iterables.get(items, 0).isDone()).isTrue();
    }

    @Test
    public void should_not_save_null_todo_item() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> repository.save(null));
    }
}