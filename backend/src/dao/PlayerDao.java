package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Player;

public class PlayerDao extends GenericDao {
    public PlayerDao() {
        super();
    }

    public Player getPlayer(String cookie) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM player WHERE cookie = ?");
        ResultSet rs = statement.executeQuery();

        rs.next();
        return this.generatePlayerFromResultSet(rs);
    }

    public void createPlayer(String cookie) throws SQLException{
        PreparedStatement statement = this.database.prepareStatement("INSERT INTO Player (cookie) VALUES (?)");
        statement.setString(1, cookie);
    }

    private Player generatePlayerFromResultSet(ResultSet results) throws SQLException {
        final String cookie = results.getString("cookie");
        final boolean host = results.getBoolean("host");
        final String player_role = results.getString("player_role");
        final String game_code = results.getString("game_code");
        return new Player(cookie, host, player_role, game_code);
    }
}
