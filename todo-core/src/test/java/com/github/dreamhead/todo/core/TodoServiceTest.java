package com.github.dreamhead.todo.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {
    private TodoService service;
    private TodoRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = mock(TodoRepository.class);
        this.service = new TodoService(this.repository);
    }

    @Test
    public void should_add_todo_item() {
        when(repository.save(any())).then(returnsFirstArg());
        TodoItem item = service.addTodoItem(new TodoParameter("foo"));
        assertThat(item.getContent()).isEqualTo("foo");
        verify(repository).save(item);
    }
}
