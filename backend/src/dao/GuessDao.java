package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Guess;

public class GuessDao {
    private Guess generateGuessFromResultSet(ResultSet results) throws SQLException {
        final String game_code = results.getString("game_code");
        final int game_round = results.getInt("game_round");
        final String hint = results.getString("hint");
        final int associated_cards = results.getInt("associated_cards");
        final int found_cards = results.getInt("found_cards");
        final boolean has_failed = results.getBoolean("has_failed");
        return new Guess(game_code,game_round,hint,associated_cards,found_cards,has_failed);
    }
}