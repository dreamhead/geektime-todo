package com.github.dreamhead.todo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.dreamhead.todo.core.TodoItem;
import com.github.dreamhead.todo.core.TodoItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoItemResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoItemRepository repository;

    @Autowired
    private ObjectMapper mapper;

    private TypeFactory factory = TypeFactory.defaultInstance();

    @Test
    public void should_add_item() throws Exception {
        String todoItem = "{ " +
                "\"content\": \"foo\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoItem))
                .andExpect(status().isCreated());

        assertThat(repository.findAll()).anyMatch(item -> item.getContent().equals("foo"));
    }

    @Test
    public void should_fail_to_add_empty_item() throws Exception {
        String todoItem = "{ " +
                "\"content\": \"\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoItem))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_fail_to_add_repeat_item() throws Exception {
        final TodoItem existedItem = repository.save(new TodoItem("foo"));
        String todoItem = "{ " +
                "\"content\": \"foo\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoItem))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_fail_to_add_unknown_request() throws Exception {
        String todoItem = "";

        mockMvc.perform(MockMvcRequestBuilders.post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoItem))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_mark_as_done() throws Exception {
        final TodoItem newItem = repository.save(new TodoItem("foo"));

        String done = "{ \"done\": true }";

        System.out.println("Before mark as done:" + newItem.getIndex());
        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/" + newItem.getIndex())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isOk());

        assertThat(repository.findAll()).anyMatch(item -> item.getContent().equals("foo") && item.isDone());
    }

    @Test
    public void should_fail_to_mark_with_illegal_index() throws Exception {
        String done = "{ \"done\": true }";

        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_fail_to_mark_with_0_index() throws Exception {
        String done = "{ \"done\": true }";

        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_fail_to_mark_unknown_todo_item() throws Exception {
        String done = "{ \"done\": true }";

        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/" + (Integer.MAX_VALUE - 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_fail_to_mark_todo_item_for_wrong_content() throws Exception {
        String done = "{ \"done\": false }";

        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/" + (Integer.MAX_VALUE - 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_fail_to_mark_with_unknown_request() throws Exception {
        String done = "";

        mockMvc.perform(MockMvcRequestBuilders.put("/todo-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(done))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_list_without_done() throws Exception {
        repository.save(new TodoItem("foo"));
        repository.save(doneItem("bar"));
        repository.save(new TodoItem("blah"));

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final CollectionType type = factory.constructCollectionType(List.class, TodoItemResponse.class);
        List<TodoItemResponse> responses = mapper.readValue(result.getResponse().getContentAsString(), type);
        assertThat(responses).hasSize(2);
    }

    @Test
    public void should_list_with_done() throws Exception {
        repository.save(new TodoItem("foo"));
        repository.save(doneItem("bar"));
        repository.save(new TodoItem("blah"));

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/todo-items?all=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        final CollectionType type = factory.constructCollectionType(List.class, TodoItemResponse.class);
        List<TodoItemResponse> responses = mapper.readValue(result.getResponse().getContentAsString(), type);
        assertThat(responses).hasSize(3);
    }

    private TodoItem doneItem(final String content) {
        final TodoItem item = new TodoItem(content);
        item.markDone();
        return item;
    }
}
