insert into roles (id, name)
values (1, 'ROLE_ADMIN');
insert into roles (id, name)
values (2, 'ROLE_USER');
insert into security_db.users (id, age, email, password, surname, name)
values (1, 20, 'admin@mail.ru', '$2y$10$SNYyGwSA9X1Ijar5Q47KluzjWsa2M08teGDHFn0TgoyM7RrfSP4o2', 'adminov', 'admin');
insert into security_db.users (id, age, email, password, surname, name)
values (2, 20, 'user@mail.ru', '$2y$10$SNYyGwSA9X1Ijar5Q47KluzjWsa2M08teGDHFn0TgoyM7RrfSP4o2', 'userov', 'user');
insert into users_roles (user_id, role_id)
values (1, 1);
insert into users_roles (user_id, role_id)
values (2, 2);