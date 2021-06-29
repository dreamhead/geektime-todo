package com.github.dreamhead.todo.core;

import lombok.Getter;

public class TodoParameter {
    @Getter
    private final String content;

    public TodoParameter(final String content) {
        this.content = content;
    }
}
