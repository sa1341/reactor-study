create table members (
    id bigint not null auto_increment,
    name varchar(255)   not null,
    age bigint not null,
    `created_at` DATETIME NOT NULL COMMENT 'created time',
    `updated_at` DATETIME NOT NULL COMMENT 'last modified time',
    primary key (id)
) engine=InnoDB;
