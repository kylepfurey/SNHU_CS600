-- .sql
-- SalesGuruPlus DataSource Generator
-- by Kyle Furey

CREATE DATABASE salesguru_auth OWNER cs600;
\c salesguru_auth

CREATE TABLE users (
    username varchar(50)  NOT NULL PRIMARY KEY,
    password varchar(500) NOT NULL,
    enabled  boolean      NOT NULL
);
CREATE TABLE authorities (
    username  varchar(50) NOT NULL,
    authority varchar(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);
CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

GRANT ALL ON TABLE users, authorities TO cs600;
