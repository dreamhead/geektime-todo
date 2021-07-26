CREATE TABLE todo_items (
    `id` int auto_increment,
    `content` varchar(255) not null unique,
    `done` tinyint not null default 0,
    primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;