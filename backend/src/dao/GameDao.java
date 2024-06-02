package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import database.PolyNameDatabase;

public abstract class GameDao {
	protected database.MySQLDatabase database;

	public void GameDao() {
		
			this.database = new PolyNameDatabase();

			String id = UUID.randomUUID().toString().substring(0,8);

			PreparedStatement statement = this.database.prepareStatement("INSERT INTO Game (game_code) VALUES (?)");
			statement.setString(1, id);

		
	}
}
