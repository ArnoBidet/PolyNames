package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Player;

public class PlayerDao extends GenericDao{
    public PlayerDao() {
        super();
    }

    public void createPlayer(String cookie, String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("INSERT INTO Player (cookie, host, game_code) VALUES (?, ?, ?, ?)");
        statement.setString(1, cookie);
        statement.setBoolean(2, true);
        statement.setString(3, game_code);
        statement.executeUpdate();
    }

    private Player generatePlayerFromResultSet(ResultSet results) throws SQLException {
        final String cookie = results.getString("cookie");
        final boolean host = results.getBoolean("host");
        final String player_role = results.getString("player_role");
        final String game_code = results.getString("game_code");
        return new Player(cookie, host, player_role, game_code);
    }
}
