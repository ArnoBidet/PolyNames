package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Player;

public class PlayerDao {

    private Player generatePlayerFromResultSet(ResultSet results) throws SQLException {
        final String cookie = results.getString("cookie");
        final boolean host = results.getBoolean("host");
        final String player_role = results.getString("player_role");
        final String game_code = results.getString("game_code");
        return new Player(cookie, host, player_role, game_code);
    }
}
