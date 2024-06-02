package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.PolyNameDatabase;
import model.Game;

public class GameDao extends GenericDao {

	protected database.MySQLDatabase database;

	public GameDao() {
		super();
	}

	public void createGame(String id) throws SQLException {

		this.database = new PolyNameDatabase();

		PreparedStatement statement = this.database.prepareStatement("INSERT INTO Game (game_code) VALUES (?)");
		statement.setString(1, id);

	}

	private Game generateGameDaoFromResultSet(ResultSet results) throws SQLException {
		final String game_code = results.getString("game_code");

		return new Game(game_code);

	}
}
