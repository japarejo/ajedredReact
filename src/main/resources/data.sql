-- Pricing Plans
--INSERT INTO plans(id, price, type, max_games, allow_game_spectators) VALUES
--(1, 0,'BASIC', 5, FALSE),
--(2, 4.99,'PRO', 50, TRUE);

INSERT INTO USERS(username,password,enabled) VALUES
('dani','$2a$10$8t/plB2wUCGgjLunfLPb2uKsUafbQsPakPgEOdJj8tykwRexCxs.a', TRUE),
('adri', '$2a$10$8t/plB2wUCGgjLunfLPb2uKsUafbQsPakPgEOdJj8tykwRexCxs.a', TRUE),
('admin1', '$2y$10$4RECJejpEAOE7Ct.HdJ9/Of8tzwtp2bzhF6AsPinCwaF0.n4.7Q2i', TRUE),
('test','$2a$10$.0rMkKe5kDbZfDX7nC0jce/HqRRbgMAHs18RN7mzKLa6G43xWx4pC', TRUE);


--INSERT INTO users(username,password,enabled,plan_id) VALUES
--('dani','$2a$10$8t/plB2wUCGgjLunfLPb2uKsUafbQsPakPgEOdJj8tykwRexCxs.a', TRUE, 1),
--('adri', '$2a$10$8t/plB2wUCGgjLunfLPb2uKsUafbQsPakPgEOdJj8tykwRexCxs.a', TRUE, 1),
--('admin1', '$2y$10$4RECJejpEAOE7Ct.HdJ9/Of8tzwtp2bzhF6AsPinCwaF0.n4.7Q2i', TRUE, 2),
--('test','$2a$10$.0rMkKe5kDbZfDX7nC0jce/HqRRbgMAHs18RN7mzKLa6G43xWx4pC', TRUE, 1);

INSERT INTO authorities(id,authority, username) VALUES
(1,'Admin', 'admin1'),(2,'Player', 'dani'),
(3,'Player', 'adri'), (4,'Player', 'test');

INSERT INTO players(username,first_name, last_name, telephone) VALUES
('dani', 'Daniel', 'Rodríguez', '666777999'), ('adri','Adrian', 'Contreras', '654654654'),
('admin1','John', 'Doe', '123456789'), ('test','Lorem', 'Ipsum', '999999999');
