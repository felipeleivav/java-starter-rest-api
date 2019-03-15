
/* MYSQL - MARIADB USER TABLE */
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `register_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_activity` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

/* POSTGRESQL USER TABLE */
CREATE TABLE public."user" (
  id serial NOT NULL,
  username varchar NOT NULL,
  "password" varchar NOT NULL,
  email varchar NOT NULL,
  last_activity timestamptz NULL,
  CONSTRAINT user_pk PRIMARY KEY (id)
);

/* ANY DB INSERT (user 'test', pass 'testtest') */
INSERT INTO "user"
(id, username, "password", email, last_activity)
VALUES(1, 'test', '$2a$15$qACt79DHAB0rN7q.RYL/EesSzfA217IIkq69yBygSmzQePhRa4geq', 'test@test.co', '2019-03-03 19:09:07');

