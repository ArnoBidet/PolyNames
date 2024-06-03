package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Guess;

public class GuessDao extends GenericDao{
    public GuessDao() {
        super();
    }

    public void createGuess(String game_code, int game_round, String hint, int associated_cards, int found_cards, boolean has_failed) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("INSERT INTO Guess (game_code, game_round, hint, associated_cards, found_cards, has_failed) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, game_code);
        statement.setInt(2, game_round);
        statement.setString(3, hint);
        statement.setInt(4, associated_cards);
        statement.setInt(5, found_cards);
        statement.setBoolean(6, has_failed);
        statement.executeUpdate();
    }

    public void updateGuess(String game_code, int game_round, int found_cards, boolean has_failed) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("UPDATE Guess SET found_cards = ?, has_failed = ? WHERE game_code = ? AND game_round = ?");
        statement.setInt(1, found_cards);
        statement.setBoolean(2, has_failed);
        statement.setString(3, game_code);
        statement.setInt(4, game_round);
        statement.executeUpdate();
    }

    public List<Guess> getGuesses(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Guess WHERE game_code = ?");
        statement.setString(1, game_code);
        ResultSet results = statement.executeQuery();
        List<Guess> result = new ArrayList<Guess>();
        while(results.next()){
            result.add(this.generateGuessFromResultSet(results));
        }
        return result;
    }


    public Guess getLastGuess(String game_code)throws SQLException{

        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Guess WHERE game_code = ? ORDER BY game_round DESC LIMIT 1");
        statement.setString(1, game_code);
        ResultSet results = statement.executeQuery();
        
        if(results.next()){
            return this.generateGuessFromResultSet(results);
        }
        return null;

    }

    public void deleteGuess(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM Guess WHERE game_code = ?");
        statement.setString(1, game_code);
        statement.executeUpdate();
    }

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