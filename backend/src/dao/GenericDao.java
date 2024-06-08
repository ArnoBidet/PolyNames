package dao;

import java.sql.SQLException;

import database.PolyNameDatabase;

public abstract class GenericDao <T> {
	protected database.MySQLDatabase database;
	protected T instance;

	GenericDao() {
		try {
			this.database = new PolyNameDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public abstract T getDao();
}
