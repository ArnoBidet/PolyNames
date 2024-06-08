package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Hint;

public class HintDao extends GenericDao{
    protected static HintDao instance;

    private HintDao() {
        super();
    }

    public static HintDao getDao(){
        if (instance == null) {
            instance = new HintDao();
        }
        return instance;
    }

    public void createHint(String game_code, int game_round, String hint, int associated_cards) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("INSERT INTO Hint (game_code, game_round, hint, associated_cards) VALUES (?, ?, ?, ?)");
        statement.setString(1, game_code);
        statement.setInt(2, game_round);
        statement.setString(3, hint);
        statement.setInt(4, associated_cards);
        statement.executeUpdate();
    }

    public void updateHint(String game_code, int game_round, int found_cards, boolean is_done) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("UPDATE Hint SET found_cards = ?, is_done = ? WHERE game_code = ? AND game_round = ?");
        statement.setInt(1, found_cards);
        statement.setBoolean(2, is_done);
        statement.setString(3, game_code);
        statement.setInt(4, game_round);
        statement.executeUpdate();
    }

    public List<Hint> getHintes(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Hint WHERE game_code = ?");
        statement.setString(1, game_code);
        ResultSet results = statement.executeQuery();
        List<Hint> result = new ArrayList<Hint>();
        while(results.next()){
            result.add(this.generateHintFromResultSet(results));
        }
        return result;
    }


    public Hint getLastHint(String game_code)throws SQLException{

        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Hint WHERE game_code = ? ORDER BY game_round DESC LIMIT 1");
        statement.setString(1, game_code);
        ResultSet results = statement.executeQuery();
        
        if(results.next()){
            return this.generateHintFromResultSet(results);
        }
        return null;

    }

    public void deleteHint(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM Hint WHERE game_code = ?");
        statement.setString(1, game_code);
        statement.executeUpdate();
    }

    private Hint generateHintFromResultSet(ResultSet results) throws SQLException {
        final String game_code = results.getString("game_code");
        final int game_round = results.getInt("game_round");
        final String hint = results.getString("hint");
        final int associated_cards = results.getInt("associated_cards");
        final int found_cards = results.getInt("found_cards");
        final boolean is_done = results.getBoolean("is_done");
        return new Hint(game_code,game_round,hint,associated_cards,found_cards,is_done);
    }
}