-- Pricing Plans
INSERT INTO plans(id, price, type, max_games, allow_game_spectators) VALUES
(1, 0.00, 'BASIC', 5, FALSE),
(2, 5.00, 'ADVANCED', 50, TRUE),
(3, 15.00, 'PRO', 250, TRUE);

INSERT INTO users(username, password, enabled, plan_id) VALUES
('basic','$2y$10$vGB5x90M4FrCqLy.YSrr2O8SeUHG18JjO9VMrnUKqIFSZBqW2BvJS', TRUE, 1),
('advanced', '$2a$12$jcGNMNNAXLetpMDB8YOd8uHhN9rlhE7.QnLpn8O/7./BQbbB/2ILO', TRUE, 2),
('pro','$2a$12$RKcOH8BbhaKd39Lmi6pquetj3iFnFw4DgyICxSmv3LBKp2AL62pzG', TRUE, 3),
('admin1', '$2y$10$4RECJejpEAOE7Ct.HdJ9/Of8tzwtp2bzhF6AsPinCwaF0.n4.7Q2i', TRUE, 3);

INSERT INTO authorities(id,authority, username) VALUES
(1,'Admin', 'admin1'),
(2,'Player', 'basic'),
(3,'Player', 'advanced'),
(4,'Player', 'pro');

INSERT INTO players(id, username, first_name, last_name, telephone) VALUES
(1, 'basic', 'John', 'Doe', '123456789'),
(2, 'advanced', 'Jane', 'Doe', '098765432'),
(3, 'pro', 'Foo', 'Bar', '999999999'),
(4, 'admin1', 'Foo', 'Baz', '111111111');
