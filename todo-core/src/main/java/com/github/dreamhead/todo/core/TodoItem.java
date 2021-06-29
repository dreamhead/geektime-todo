package com.github.dreamhead.todo.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class TodoItem {
    @Getter
    private String content;

    public TodoItem(final String content) {
        this.content = content;
    }
}
