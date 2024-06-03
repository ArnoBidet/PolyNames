package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Word;

public class CardDao extends GenericDao {
    final int GRID_ROW = 5;
    final int GRID_COL = 5;
    protected database.MySQLDatabase database;

    public CardDao(){
        super();
    }

    public List<Card> getCard(String game_id) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Card WHERE game_code = ?");
        statement.setString(1, game_id);
        ResultSet results = statement.executeQuery();
        List<Card> result = new ArrayList<Card>();
        if (results.next()) {
            result.add(generateCardFromResultSet(results));
        }
        return result;
    }

    public void createCard(String game_code, List<Word> words) throws SQLException {
        PreparedStatement statement = this.database
                .prepareStatement("INSERT INTO Card (game_code, grid_row, grid_col, word_id) VALUES (?, ?, ?, ?)");

        for (int row = 0; row < GRID_ROW; row++) {
            for (int col = 0; col < GRID_COL; col++) {
                Word word = words.get(row * GRID_COL + col);
                statement.setString(1, game_code);
                statement.setInt(2, row);
                statement.setInt(3, col);
                statement.setInt(4, word.id());
                statement.addBatch();
            }
        }
        statement.executeBatch();
    }

    public void deleteCard(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM Card WHERE game_code = ?");
        statement.setString(1, game_code);
        statement.executeUpdate();
    }

    private Card generateCardFromResultSet(ResultSet results) throws SQLException {
        final String game_code = results.getString("game_code");
        final int grid_row = results.getInt("grid_row");
        final int grid_col = results.getInt("grid_col");
        final int word_id = results.getInt("word_id");
        final String card_type = results.getString("card_type");
        final Boolean is_discovered = results.getBoolean("is_discovered");
        return new Card(game_code, grid_row, grid_col, word_id, card_type, is_discovered);
    }
}
