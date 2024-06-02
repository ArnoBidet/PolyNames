package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Word;

public class WordDao {
    private Word generateWordFromResultSet(ResultSet results) throws SQLException {
        final int id = results.getInt("id");
        final String word = results.getString("word");
        return new Word(id,word);
    }
}
