create table if not exists BOOK (
    BOOK_ID bigint not null auto_increment,
    TITLE_KOREAN varchar(100) not null,
    TITLE_ENGLISH varchar(100) not null,
    DESCRIPTION varchar(100) not null,
    AUTHOR varchar(100) not null,
    ISBN varchar(100) not null UNIQUE,
    PUBLISH varchar(100) not null,
    CREATED_AT datetime not null,
    LAST_MODIFIED_AT datetime not null,
    PRIMARY KEY (BOOK_ID)
);
