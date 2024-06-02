package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.PolyNameDatabase;
import model.GameGrid;

public class GameGridDao extends GenericDao {

    protected database.MySQLDatabase database;

    public GameGridDao() {
        super();
    }

    
    private GameGrid generateGameGridFromResultSet(ResultSet results) throws SQLException {
        final String game_code = results.getString("game_code");
        final int grid_row = results.getInt("grid_row");
        final int grid_col = results.getInt("grid_col");
        final int word_id = results.getInt("word_id");
        final String card_type = results.getString("card_type");
        final Boolean is_discovered = results.getBoolean("is_discovered");
        return new GameGrid(game_code,grid_row,grid_col,word_id,card_type,is_discovered);
    }
}
