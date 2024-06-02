package database;

import java.sql.SQLException;

public class PolyNameDatabase extends MySQLDatabase{

	public PolyNameDatabase() throws SQLException {
		super("127.0.0.1", 3306, "polyname",  "root", "password");
	}
	
}
