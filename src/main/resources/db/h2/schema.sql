DROP TABLE piece IF EXISTS;
DROP TABLE games IF EXISTS;
DROP TABLE chess_board IF EXISTS;
DROP TABLE game_player IF EXISTS;
DROP TABLE authorities IF EXISTS;
DROP TABLE players IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE plans IF EXISTS;

CREATE TABLE plans (
  id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  type                  VARCHAR(255) UNIQUE,
  price                 NUMERIC(7,2),
  max_games             INTEGER NOT NULL,
  allow_game_spectators BOOLEAN NOT NULL
);

CREATE TABLE users (
  username   VARCHAR(255) PRIMARY KEY,
  password   VARCHAR(255) NOT NULL,
  enabled    BOOLEAN NOT NUll,
  plan_id    INTEGER NOT NULL
);

ALTER TABLE users ADD CONSTRAINT fk_users_plans FOREIGN KEY (plan_id) REFERENCES plans (id);

CREATE TABLE authorities (
  id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  authority  VARCHAR(20),
  username   VARCHAR(255)
);

ALTER TABLE authorities ADD CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username);

CREATE TABLE players (
  id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  color      VARCHAR(20),
  telephone  VARCHAR(20),
  time       INTEGER,
  start_turn TIMESTAMP,
  username   VARCHAR(255)
);

ALTER TABLE players ADD CONSTRAINT fk_players_users FOREIGN KEY (username) REFERENCES users (username);

CREATE TABLE chess_board (
  id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  turn          VARCHAR(80),
  jaque         BOOLEAN NOT NULL,
  jaque_mate    BOOLEAN NOT NULL,
  coronacion    BOOLEAN NOT NULL,
  id_coronacion INTEGER NOT NULL
);

CREATE TABLE games (
  id               INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name             VARCHAR(30) UNIQUE,
  tiempo           INTEGER NOT NULL,
  espectadores     BOOLEAN NOT NULL,
  numero_jugadores INTEGER NOT NULL,
  fin_partida      BOOLEAN NOT NULL,
  chessboard_id    INTEGER NOT NULL
);

ALTER TABLE games ADD CONSTRAINT fk_games_chessboard FOREIGN KEY (chessboard_id) REFERENCES chess_board (id);

CREATE TABLE game_player (
  game_id   INTEGER,
  player_id INTEGER
);

ALTER TABLE game_player ADD CONSTRAINT fk_game_player_games FOREIGN KEY (game_id) REFERENCES games (id);
ALTER TABLE game_player ADD CONSTRAINT fk_game_player_players FOREIGN KEY (player_id) REFERENCES players (id);


CREATE TABLE piece (
  id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  type         VARCHAR(255),
  color        VARCHAR(20),
  x_position   INTEGER NOT NULL,
  y_position   INTEGER NOT NULL,
  pieza_movida BOOLEAN NOT NULL,
  peon_paso    BOOLEAN NOT NULL,
  board_id     INTEGER NOT NULL
);

ALTER TABLE piece ADD CONSTRAINT fk_piece_chessboard FOREIGN KEY (board_id) REFERENCES chess_board (id);
