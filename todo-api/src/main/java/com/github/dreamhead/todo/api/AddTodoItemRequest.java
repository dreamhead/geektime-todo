package com.github.dreamhead.todo.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class AddTodoItemRequest {
    @Getter
    private String content;

    @JsonCreator
    public AddTodoItemRequest(@JsonProperty("content") final String content) {
        this.content = content;
    }
}
