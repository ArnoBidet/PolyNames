package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.PolyNameDatabase;
import model.Word;

public class WordDao extends GenericDao {


    public WordDao() {
        super();
    }

    public List<Word> getRandomList() throws SQLException {
        this.database = new PolyNameDatabase();

        PreparedStatement statement = this.database.prepareStatement("SELECT * FROM Word ORDER BY RAND() LIMIT 25;");
        ResultSet rs = statement.executeQuery();
        List<Word> results = new ArrayList<Word>();
        
        while (rs.next()) 
            results.add(this.generateWordFromResultSet(rs));

        return results;
    }

    private Word generateWordFromResultSet(ResultSet results) throws SQLException {
        final int id = results.getInt("id");
        final String word = results.getString("word");
        return new Word(id, word);
    }
}
