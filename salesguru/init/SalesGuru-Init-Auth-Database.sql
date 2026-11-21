CREATE DATABASE salesguru_auth OWNER postgres;
\c salesguru_auth

CREATE TABLE public.users (
    username varchar(50) NOT NULL PRIMARY KEY,
    password varchar(500) NOT NULL,
    enabled boolean NOT NULL
);

CREATE TABLE public.authorities (
    username varchar(50) NOT NULL,
    authority varchar(50) NOT NULL,
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (username)
        REFERENCES public.users (username)
);

CREATE UNIQUE INDEX ix_auth_username
    ON public.authorities (username, authority);

GRANT USAGE ON SCHEMA public TO cs600;
GRANT ALL PRIVILEGES ON TABLE public.users, public.authorities TO cs600;
