package com.github.dreamhead.todo.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TodoExceptionTest {
    @Test
    public void should_create_todo_exception() {
        final TodoException exception = new TodoException("foo", new IllegalArgumentException());
        assertThat(exception).hasMessage("foo");
    }

}