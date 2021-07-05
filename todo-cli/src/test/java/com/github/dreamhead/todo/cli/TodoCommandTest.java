package com.github.dreamhead.todo.cli;

import com.github.dreamhead.todo.core.TodoIndexParameter;
import com.github.dreamhead.todo.core.TodoItem;
import com.github.dreamhead.todo.core.TodoItemService;
import com.github.dreamhead.todo.core.TodoParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodoCommandTest {
    @TempDir
    File tempDir;
    private TodoItemService service;
    private CommandLine cli;

    @BeforeEach
    void setUp() {
        final ObjectFactory factory = new ObjectFactory();
        final File repositoryFile = new File(tempDir, "repository.json");
        this.service = factory.createService(repositoryFile);
        cli = factory.createCommandLine(repositoryFile);
    }

    @Test
    public void should_add_todo_item() {
        final int result = cli.execute("add", "foo");

        assertThat(result).isEqualTo(0);
        final List<TodoItem> items = service.list(true);
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getContent()).isEqualTo("foo");
    }

    @Test
    public void should_fail_to_add_empty_todo() {
        assertThat(cli.execute("add", "")).isNotEqualTo(0);
    }

    @Test
    public void should_mark_as_done() {
        service.addTodoItem(TodoParameter.of("foo"));

        cli.execute("done", "1");

        final List<TodoItem> items = service.list(true);
        assertThat(items.get(0).isDone()).isTrue();
    }

    @Test
    public void should_fail_to_mark_with_illegal_index() {
        assertThat(cli.execute("done", "-1")).isNotEqualTo(0);
        assertThat(cli.execute("done", "0")).isNotEqualTo(0);
    }

    @Test
    public void should_fail_to_mark_unknown_todo_item() {
        assertThat(cli.execute("done", "1")).isNotEqualTo(0);
    }
    
    @Test
    public void should_list_without_done() {
        service.addTodoItem(TodoParameter.of("foo"));
        service.addTodoItem(TodoParameter.of("bar"));
        service.addTodoItem(TodoParameter.of("blah"));
        service.markTodoItemDone(TodoIndexParameter.of(2));

        assertThat(cli.execute("list")).isEqualTo(0);
    }

    @Test
    public void should_list_with_done() {
        service.addTodoItem(TodoParameter.of("foo"));
        service.addTodoItem(TodoParameter.of("bar"));
        service.addTodoItem(TodoParameter.of("blah"));
        service.markTodoItemDone(TodoIndexParameter.of(2));

        assertThat(cli.execute("list", "--all")).isEqualTo(0);
    }
}