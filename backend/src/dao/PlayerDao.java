package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Player;

public class PlayerDao extends GenericDao {
    protected static PlayerDao instance;
    private PlayerDao() {
        super();
    }

    public static PlayerDao getDao(){
        if (instance == null) {
            instance = new PlayerDao();
        }
        return instance;
    }

    public Player getPlayer(String cookie) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM player WHERE cookie = ?");
        ResultSet rs = statement.executeQuery();

        rs.next();
        return this.generatePlayerFromResultSet(rs);
    }

    public void createPlayer(String cookie, String game_code, boolean host) throws SQLException{
        PreparedStatement statement = this.database.prepareStatement("INSERT INTO Player (cookie, game_code, host) VALUES (?, ?, ?)");
        statement.setString(1, cookie);
        statement.setString(2, game_code);
        statement.setBoolean(3, host);
    }

    public void deletePlayerFromGame(String game_code) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM Player WHERE game_code = ?");
        statement.setString(1, game_code);
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
