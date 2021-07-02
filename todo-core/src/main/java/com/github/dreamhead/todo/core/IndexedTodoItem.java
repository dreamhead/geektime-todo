package com.github.dreamhead.todo.core;

import lombok.Getter;

public class IndexedTodoItem {
    @Getter
    private int index;
    private TodoItem item;

    public IndexedTodoItem(final int index, final TodoItem item) {
        this.index = index;
        this.item = item;
    }

    public String getContent() {
        return item.getContent();
    }

    public boolean isDone() {
        return item.isDone();
    }
}
