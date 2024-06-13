package dao;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.CardType;
import model.Word;

public class CardDao extends GenericDao {
    protected static CardDao instance;
    final int GRID_ROW = 5;
    final int GRID_COL = 5;

    final int GUESS_NUMBER = 8;
    final int ASSASSIN_NUMBER = 2;

    private CardDao() {
        super();
    }

    public static CardDao getDao() {
        if (instance == null)
            instance = new CardDao();
        return instance;
    }

    public Card getCard(String game_id, int grid_row, int grid_col) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement(
                "SELECT game_id, grid_row, grid_col, word, card_type, is_discovered FROM v_card  WHERE game_id = ? AND grid_row = ? AND grid_col = ?");
        statement.setString(1, game_id);
        statement.setInt(2, grid_row);
        statement.setInt(3, grid_col);
        ResultSet results = statement.executeQuery();
        if (results.next()) 
            return generateCardFromResultSet(results);
        return null;
    }

    public List<Card> getCards(String game_id) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement(
                "SELECT game_id, grid_row, grid_col, word, card_type, is_discovered FROM v_card WHERE game_id = ?");
        statement.setString(1, game_id);
        ResultSet results = statement.executeQuery();
        List<Card> result = new ArrayList<Card>();
        while (results.next())
            result.add(generateCardFromResultSet(results));
        return result;
    }

    public void createCards(String game_id, List<Word> words) throws SQLException {
        List<Word> neutrals = new ArrayList<Word>();
        for (Word word : words) 
            neutrals.add(word);
        
        List<Word> assassin = new ArrayList<Word>();
        List<Word> guess = new ArrayList<Word>();

        for (int i = 0; i < GUESS_NUMBER; i++) {
            int index = (int) (Math.random() * neutrals.size());
            guess.add(neutrals.get(index));
            neutrals.remove(index);
        }
        for (int i = 0; i < ASSASSIN_NUMBER; i++) {
            int index = (int) (Math.random() * neutrals.size());
            assassin.add(neutrals.get(index));
            neutrals.remove(index);
        }

        this.database.setAutoCommit(false);
        PreparedStatement statement = this.database
                .prepareStatement(
                        "INSERT INTO card (game_id, grid_row, grid_col, word_id, card_type) VALUES (?, ?, ?, ?, ?)");

        for (int row = 0; row < GRID_ROW; row++) {
            for (int col = 0; col < GRID_COL; col++) {
                int i = row * GRID_COL + col;
                Word word = words.get(i);
                String card_type = CardType.NEUTRAL;
                if (assassin.contains(word)) {
                    card_type = CardType.ASSASSIN;
                } else if (guess.contains(word)) {
                    card_type = CardType.WORD;
                }
                statement.setString(1, game_id);
                statement.setInt(2, row);
                statement.setInt(3, col);
                statement.setInt(4, word.id());
                statement.setString(5, card_type);
                statement.addBatch();
            }
        }
        try {
            statement.executeBatch();
            this.database.commit();
            this.database.setAutoCommit(true);
        } catch (BatchUpdateException b) {
            System.err.println(b.getNextException());
        }
    }

    public void deleteCard(String game_id) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM card WHERE game_id = ?");
        statement.setString(1, game_id);
        statement.executeUpdate();
    }

    private Card generateCardFromResultSet(ResultSet results) throws SQLException {
        final String game_id = results.getString("game_id");
        final int grid_row = results.getInt("grid_row");
        final int grid_col = results.getInt("grid_col");
        final String word = results.getString("word");
        final String card_type = results.getString("card_type");
        final Boolean is_discovered = results.getBoolean("is_discovered");
        return new Card(game_id, grid_row, grid_col, word, card_type, is_discovered);
    }
}
