package dao;

import java.sql.SQLException;

import database.PolyNameDatabase;

public abstract class GenericDao {
	protected database.MySQLDatabase database;

	GenericDao() {
		try {
			this.database = new PolyNameDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
