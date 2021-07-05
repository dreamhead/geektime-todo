package com.github.dreamhead.todo.cli;

import com.github.dreamhead.todo.cli.file.FileTodoItemRepository;
import com.github.dreamhead.todo.core.TodoItemRepository;
import com.github.dreamhead.todo.core.TodoItemService;
import picocli.CommandLine;

import java.io.File;

public class ObjectFactory {
    public CommandLine createCommandLine(final File repositoryFile) {
        return new CommandLine(createTodoCommand(repositoryFile));
    }

    private TodoCommand createTodoCommand(final File repositoryFile) {
        final TodoItemService service = createService(repositoryFile);
        return new TodoCommand(service);
    }

    public TodoItemService createService(final File repositoryFile) {
        final TodoItemRepository repository = new FileTodoItemRepository(repositoryFile);
        return new TodoItemService(repository);
    }
}
