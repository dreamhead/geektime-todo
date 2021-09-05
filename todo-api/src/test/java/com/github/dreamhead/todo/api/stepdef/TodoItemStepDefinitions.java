package com.github.dreamhead.todo.api.stepdef;

import com.github.dreamhead.todo.api.AddTodoItemRequest;
import com.github.dreamhead.todo.api.MarkAsDoneRequest;
import com.github.dreamhead.todo.api.SpringIntegrationTest;
import com.github.dreamhead.todo.api.TodoItemResponse;
import io.cucumber.java8.En;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoItemStepDefinitions extends SpringIntegrationTest implements En {
    private RestTemplate restTemplate;
    private TodoItemResponse[] responses;
    private Map<String, String> todoItems;

    public TodoItemStepDefinitions() {
        Before(() -> {
            this.restTemplate = new RestTemplate();
            this.todoItems = new HashMap<>();
        });

        After(() -> {
            this.repository.deleteAll();
        });

        Given("todo item {string} is added", (String content) ->
                addTodoItem(content)
        );

        When("list todo items", () -> {
            responses = listTodoItems();
        });

        When("mark todo item {string} as done", (String content) -> {
            final String id = this.todoItems.get(content);
            assertThat(id).isNotNull();
            markTodoItemAsDone(id);
        });

        Then("todo item {string} should be contained", (String content) -> {
            assertThat(Arrays.stream(responses)
                    .anyMatch(item -> item.getContent().equals(content))).isTrue();
        });

        Then("todo item {string} should not be contained", (String content) -> {
            assertThat(Arrays.stream(responses)
                    .noneMatch(item -> item.getContent().equals(content))).isTrue();
        });
    }

    private TodoItemResponse[] listTodoItems() {
        return restTemplate.getForObject("http://localhost:8080/todo-items", TodoItemResponse[].class);
    }

    private void addTodoItem(final String content) {
        AddTodoItemRequest request = new AddTodoItemRequest(content);
        final ResponseEntity<String> entity =
                restTemplate.postForEntity("http://localhost:8080/todo-items", request, String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final List<String> locations = entity.getHeaders().get("Location");
        final String location = locations.get(0);
        final int index = location.lastIndexOf("/");
        final String id = location.substring(index + 1);
        this.todoItems.put(content, id);
    }

    private void markTodoItemAsDone(final String id) {
        final MarkAsDoneRequest request = new MarkAsDoneRequest(true);
        restTemplate.put("http://localhost:8080/todo-items/" + id, request);
    }
}
