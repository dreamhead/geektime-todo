package com.github.dreamhead.todo.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dreamhead.todo.core.TodoItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor
@Getter
public class TodoItemResponse {
    @JsonProperty
    private long index;
    @JsonProperty
    private String content;
    @JsonProperty
    private boolean done;

    public TodoItemResponse(final TodoItem item) {
        this.index = item.getIndex();
        this.content = item.getContent();
        this.done = item.isDone();
    }
}
