package com.github.dreamhead.todo.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.github.dreamhead.todo.core"})
@EntityScan({"com.github.dreamhead.todo.core"})
public class Bootstrap {
    public static void main(final String[] args) {

    }
}
