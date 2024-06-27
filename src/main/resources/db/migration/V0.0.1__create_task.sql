CREATE TABLE task
(
    id               bigint NOT NULL AUTO_INCREMENT,
    title            varchar(255),
    summary          varchar(1024),
    date_of_creation date,
    due_date date,
    PRIMARY KEY (id)
);
