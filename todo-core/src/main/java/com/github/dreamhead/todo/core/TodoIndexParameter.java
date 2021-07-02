package com.github.dreamhead.todo.core;

import lombok.Getter;

public final class TodoIndexParameter {
    @Getter
    private final int index;

    private TodoIndexParameter(final int index) {
        this.index = index;
    }

    public static TodoIndexParameter of(final int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("Todo index should be greater than 0");
        }

        return new TodoIndexParameter(index);
    }
}
