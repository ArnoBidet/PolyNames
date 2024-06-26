CREATE DATABASE IF NOT EXISTS polyname;

USE polyname;

CREATE TABLE IF NOT EXISTS game (
    game_id CHAR(8) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS word (
    id INTEGER PRIMARY KEY AUTO_INCREMENT ,
    word VARCHAR(11),
    CONSTRAINT uq_word UNIQUE (word)
);

CREATE TABLE IF NOT EXISTS player (
    user_id VARCHAR(8) PRIMARY KEY,
    host BOOLEAN DEFAULT FALSE,
    player_role VARCHAR(20) DEFAULT 'UNDEFINED' CHECK (player_role IN ('WORD_MASTER', 'GUESS_MASTER', 'UNDEFINED')),
    game_id CHAR(8),
    CONSTRAINT fk_player_game_id FOREIGN KEY (game_id) REFERENCES game(game_id)
);

CREATE TABLE IF NOT EXISTS card (
    game_id CHAR(8),
    word_id INTEGER,
    grid_row INTEGER CHECK (grid_row >= 0 AND grid_row < 5),
    grid_col INTEGER CHECK (grid_col >= 0 AND grid_col < 5),
    card_type VARCHAR(10) DEFAULT 'NEUTRAL'  CHECK (card_type IN ('NEUTRAL', 'KILLER', 'GUESS')),
    is_discovered BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_game_cards PRIMARY KEY (game_id, word_id),
    CONSTRAINT uq_game_grid UNIQUE (game_id, grid_row, grid_col),
    CONSTRAINT fk_game_grid_word_id FOREIGN KEY (word_id) REFERENCES word(id),
    CONSTRAINT fk_game_grid_game_id FOREIGN KEY (game_id) REFERENCES game(game_id)
);


CREATE TABLE IF NOT EXISTS hint (
    game_id CHAR(8),
    game_round INTEGER,
    hint VARCHAR(27),
    associated_cards INTEGER CHECK (associated_cards >= 1 AND associated_cards <= 8),
    found_cards INTEGER DEFAULT 0 CHECK (found_cards >= 0 AND found_cards <= (associated_cards + 1)),
    is_done BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_hint PRIMARY KEY (game_id, game_round),
    CONSTRAINT fk_hint_game_id FOREIGN KEY (game_id) REFERENCES game(game_id)
);



CREATE VIEW v_card AS
SELECT game_id, word, grid_row, grid_col, card_type, is_discovered
FROM card c, word w
WHERE c.word_id = w.id;