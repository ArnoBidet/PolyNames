CREATE TABLE IF NOT EXISTS Game (
    game_code CHAR(8) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Word (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word VARCHAR(11),
    CONSTRAINT uq_word UNIQUE (word)
);

CREATE TABLE IF NOT EXISTS Player (
    cookie VARCHAR(255) PRIMARY KEY,
    host BOOLEAN DEFAULT FALSE,
    player_role VARCHAR(20) DEFAULT 'UNDEFINED' CHECK (player_role IN ('WORD_MASTER', 'GUESS_MASTER', 'UNDEFINED')),
    game_code CHAR(8),
    CONSTRAINT fk_player_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code)
);

CREATE TABLE IF NOT EXISTS GameGrid (
    game_code CHAR(8),
    grid_row INTEGER CHECK (grid_row >= 0 AND grid_row < 5),
    grid_col INTEGER CHECK (grid_col >= 0 AND grid_col < 5),
    word_id INTEGER,
    card_type VARCHAR(10) DEFAULT 'NEUTRAL'  CHECK (card_type IN ('NEUTRAL', 'ASSASSIN', 'WORD')),
    is_discovered BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_game_cards PRIMARY KEY (game_code, grid_row, grid_col),
    CONSTRAINT uq_game_grid UNIQUE (game_code, grid_row, grid_col),
    CONSTRAINT fk_game_grid_word_id FOREIGN KEY (word_id) REFERENCES Word(id),
    CONSTRAINT fk_game_grid_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code)
);


CREATE TABLE IF NOT EXISTS Guess (
    game_code CHAR(8),
    game_round INTEGER,
    hint VARCHAR(40),
    associated_cards INTEGER CHECK (associated_cards >= 1 AND associated_cards <= 8),
    found_cards INTEGER CHECK (found_cards >= 0 AND found_cards <= (associated_cards + 1)),
    has_failed BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_guess PRIMARY KEY (game_code, game_round),
    CONSTRAINT fk_guess_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code)
);