package com.github.dreamhead.todo.api;

import com.github.dreamhead.todo.core.TodoItem;
import com.github.dreamhead.todo.core.TodoItemRepository;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:test.properties")
public class TodoItemRepositoryTest {
    @Autowired
    private TodoItemRepository repository;

    @Test
    public void should_find_nothing_for_empty_repository() {
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
        final TodoItem secondItem = Iterables.get(items, 1);
        assertThat(secondItem.getContent()).isEqualTo("bar");
        assertThat(secondItem.getIndex()).isEqualTo(firstItem.getIndex() + 1);
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
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                .isThrownBy(() -> repository.save(null));
    }
}
