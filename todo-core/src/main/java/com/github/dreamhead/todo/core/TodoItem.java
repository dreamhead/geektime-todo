package com.github.dreamhead.todo.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class TodoItem {
    private final String content;

    public TodoItem(final String content) {
        this.content = content;
    }
}
