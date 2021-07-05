package com.github.dreamhead.todo.cli;

import com.github.dreamhead.todo.core.TodoIndexParameter;
import com.github.dreamhead.todo.core.TodoItem;
import com.github.dreamhead.todo.core.TodoItemService;
import com.github.dreamhead.todo.core.TodoParameter;
import com.google.common.base.Strings;
import picocli.CommandLine;

import java.util.List;
import java.util.Optional;

@CommandLine.Command(name = "todo")
public class TodoCommand {
    private final TodoItemService service;

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    public TodoCommand(final TodoItemService service) {
        this.service = service;
    }

    @CommandLine.Command(name = "add")
    public int add(@CommandLine.Parameters(index = "0") final String item) {
        if (Strings.isNullOrEmpty(item)) {
            throw new CommandLine.ParameterException(spec.commandLine(), "empty item is not allowed");
        }

        final TodoItem todoItem = this.service.addTodoItem(TodoParameter.of(item));
        System.out.printf("%d. %s%n", todoItem.getIndex(), todoItem.getContent());
        System.out.printf("Item <%d> added%n", todoItem.getIndex());

        return 0;
    }

    @CommandLine.Command(name = "done")
    public int markAsDone(@CommandLine.Parameters(index = "0") final int index) {
        if (index <= 0) {
            throw new CommandLine.ParameterException(spec.commandLine(), "index should be greater than 0");
        }

        final Optional<TodoItem> item = this.service.markTodoItemDone(TodoIndexParameter.of(index));
        if (!item.isPresent()) {
            throw new CommandLine.ParameterException(spec.commandLine(), "unknown todo item index");
        }

        final TodoItem actual = item.get();
        System.out.printf("Item <%d> done%n", actual.getIndex());
        return 0;
    }

    @CommandLine.Command(name = "list")
    public int list(@CommandLine.Option(names = "--all") final boolean all) {
        final List<TodoItem> items = this.service.list(all);

        items.stream()
                .map(this::formatItem)
                .forEach(System.out::println);

        if (all) {
            final long doneCount = items.stream()
                    .filter(TodoItem::isDone)
                    .count();

            System.out.println(formatTotal(items.size(), doneCount));
            return 0;
        }

        System.out.println(formatTotal(items.size()));
        return 0;
    }

    private String formatTotal(final int size, final long doneCount) {
        return "Total: "
                + size + unit(size) + ", "
                + doneCount + unit(doneCount);
    }

    private String formatTotal(final int size) {
        return "Total: " + size + unit(size);
    }

    private String unit(final long count) {
        if (count > 1) {
            return " items";
        }

        return " item";
    }

    private String formatItem(final TodoItem todoItem) {
        if (todoItem.isDone()) {
            return String.format("%d. [done] %s", todoItem.getIndex(), todoItem.getContent());
        }

        return String.format("%d. %s", todoItem.getIndex(), todoItem.getContent());
    }
}
