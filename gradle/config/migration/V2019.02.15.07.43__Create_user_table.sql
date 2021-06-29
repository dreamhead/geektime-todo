CREATE TABLE zero_users(
    id bigint(20) not null AUTO_INCREMENT,
    name varchar(100) not null unique,
    password varchar(100) not null,
    primary key(id)
);