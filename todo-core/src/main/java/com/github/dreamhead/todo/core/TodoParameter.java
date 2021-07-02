package com.github.dreamhead.todo.core;

import com.google.common.base.Strings;
import lombok.Getter;

public final class TodoParameter {
    @Getter
    private final String content;

    private TodoParameter(final String content) {
        this.content = content;
    }

    public static TodoParameter of(final String content) {
        if (Strings.isNullOrEmpty(content)) {
            throw new IllegalArgumentException("Empty content is not allowed");
        }

        return new TodoParameter(content);
    }
}
