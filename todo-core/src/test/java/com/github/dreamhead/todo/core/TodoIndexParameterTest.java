package com.github.dreamhead.todo.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TodoIndexParameterTest {
    @Test
    public void should_create_index_parameter() {
        final TodoIndexParameter parameter = TodoIndexParameter.of(1);
        assertThat(parameter.getIndex()).isEqualTo(1);
    }
    
    @Test
    public void should_not_create_index_parameter() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> TodoIndexParameter.of(0));
    }

}