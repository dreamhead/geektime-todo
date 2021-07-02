package com.github.dreamhead.todo.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TodoParameterTest {
    @Test
    public void should_create_todo_parameter() {
        final TodoParameter parameter = TodoParameter.of("foo");
        assertThat(parameter.getContent()).isEqualTo("foo");
    }

    @Test
    public void should_not_create_todo_parameter() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TodoParameter.of(""));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TodoParameter.of(null));
    }

}