package com.github.dreamhead.todo.api.repository;

import com.github.dreamhead.todo.core.TodoItemRepository;

public interface TodoItemJpaRepository extends TodoItemRepository {
    void deleteAll();
}
