CREATE TABLE IF NOT EXISTS Game (
    game_code TEXT PRIMARY KEY
)

CREATE TABLE IF NOT EXISTS Player (
    cookie CHAR(8) PRIMARY KEY,
    host BOOLEAN,
    player_role TEXT,
    game_code TEXT,
    CONSTRAINT fk_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code),
    CONSTRAINT ck_player_role CHECK (player_role IN ('WORD_MASTER', 'GUESS_MASTER', 'UNDEFINED'))
)

CREATE TABLE IF NOT EXISTS GameGrid (
    game_code CHAR(8),
    grid_row INTEGER,
    grid_col INTEGER,
    word_id INTEGER,
    card_type TEXT,
    is_discovered BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_game_cards PRIMARY KEY (game_code, grid_row, grid_col),
    CONSTRAINT uq_game_Grid UNIQUE (game_code, grid_row, grid_col),
    CONSTRAINT fk_word_id FOREIGN KEY (word_id) REFERENCES Word(id),
    CONSTRAINT ck_row CHECK (grid_row >= 0 AND grid_row < 5),
    CONSTRAINT ck_col CHECK (grid_col >= 0 AND grid_col < 5),
    CONSTRAINT fk_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code)
)

CREATE TABLE IF NOT EXISTS Word (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    word TEXT,
    CONSTRAINT uq_word UNIQUE (word)
)

CREATE TABLE IF NOT EXISTS Guess (
    game_code CHAR(8),
    game_round INTEGER,
    hint TEXT,
    associated_cards INTEGER,
    found_cards INTEGER,
    has_failed BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_guess PRIMARY KEY (game_code, game_round),
    CONSTRAINT fk_game_code FOREIGN KEY (game_code) REFERENCES Game(game_code)
    CONSTRAINT ck_associated_cards CHECK (associated_cards >= 1 AND associated_cards <= 8),
    CONSTRAINT ck_found_cards CHECK (found_cards >= 0 AND found_cards <= associated_cards + 1)
)