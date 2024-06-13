package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Player;

public class PlayerDao extends GenericDao {
    protected static PlayerDao instance;

    private PlayerDao() {
        super();
    }

    public static PlayerDao getDao() {
        if (instance == null) {
            instance = new PlayerDao();
        }
        return instance;
    }

    public Player getPlayer(String user_id) throws SQLException {
        PreparedStatement statement = this.database
                .prepareStatement("SELECT user_id, host, player_role, game_id FROM player WHERE id = ?");
        statement.setString(1, user_id);
        ResultSet rs = statement.executeQuery();
        while (rs.next())
            return this.generatePlayerFromResultSet(rs);
        return null;
    }

    public List<Player> getPlayerByGameCode(String game_id) throws SQLException {
        PreparedStatement statement = this.database
                .prepareStatement("SELECT user_id, host, player_role, game_id FROM player WHERE game_id = ?");
        statement.setString(1, game_id);
        ResultSet rs = statement.executeQuery();
        List<Player> result = new ArrayList<Player>();
        while (rs.next()) {
            result.add(generatePlayerFromResultSet(rs));
        }
        return result;
    }

    public void createPlayer(String user_id, String game_id, boolean host) throws SQLException {
        PreparedStatement statement = this.database
                .prepareStatement("INSERT INTO player (user_id, game_id, host) VALUES (?, ?, ?)");
        statement.setString(1, user_id);
        statement.setString(2, game_id);
        statement.setBoolean(3, host);
        statement.executeUpdate();
    }

    public void setPlayerRole(String user_id, String role) throws SQLException {
        PreparedStatement statement = this.database
                .prepareStatement("UPDATE player SET player_role = ? WHERE user_id = ?");
        statement.setString(1, role);
        statement.setString(2, user_id);
        statement.executeUpdate();
    }

    public void deletePlayersFromGame(String game_id) throws SQLException {
        PreparedStatement statement = this.database.prepareStatement("DELETE FROM player WHERE game_id = ?");
        statement.setString(1, game_id);
        statement.executeUpdate();
    }

    private Player generatePlayerFromResultSet(ResultSet results) throws SQLException {
        final String id = results.getString("user_id");
        final boolean host = results.getBoolean("host");
        final String player_role = results.getString("player_role");
        final String game_id = results.getString("game_id");
        return new Player(id, host, player_role, game_id);
    }
}
