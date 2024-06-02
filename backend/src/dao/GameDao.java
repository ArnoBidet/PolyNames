package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Game;

public class GameDao extends GenericDao {
	public GameDao() {
		super();
	}

	public void createGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("INSERT INTO Game (game_code) VALUES (?)");
		statement.setString(1, id);
	}

	public Game getGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("SELECT game_code FROM Game WHERE game_code = ?");
		statement.setString(1, id);
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			return generateGameDaoFromResultSet(results);
		}
		return null;
	}

	public void deleteGame(String id) throws SQLException {
		PreparedStatement statement = this.database.prepareStatement("DELETE FROM Game WHERE game_code = ?");
		statement.setString(1, id);
		statement.executeUpdate();
	}

	private Game generateGameDaoFromResultSet(ResultSet results) throws SQLException {
		final String game_code = results.getString("game_code");
		return new Game(game_code);
	}
}
