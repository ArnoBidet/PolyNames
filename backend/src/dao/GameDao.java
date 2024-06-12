package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Game;

public class GameDao extends GenericDao {
	protected static GameDao instance;
	private GameDao() {
		super();
	}

    public static GameDao getDao(){
        if (instance == null) {
            instance = new GameDao();
        }
        return instance;
    }

	public void createGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("INSERT INTO game (game_id) VALUES (?)");
		statement.setString(1, id);
		statement.executeUpdate();
	}

	public Game getGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("SELECT game_id FROM game WHERE game_id = ?");
		statement.setString(1, id);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			return generateGameDaoFromResultSet(results);
		}
		return null;
	}

	public void deleteGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("DELETE FROM game WHERE game_id = ?");
		statement.setString(1, id);
		statement.executeUpdate();
	}

	private Game generateGameDaoFromResultSet(ResultSet results) throws SQLException {
		final String game_id = results.getString("game_id");
		return new Game(game_id);
	}
}
