package controller;

import java.sql.SQLException;
import java.util.UUID;

import dao.GameDao;
import dao.PlayerDao;
import webserver.WebServerContext;
import webserver.WebServerResponse;

public class GameController {

    public static void createGame(WebServerContext context) {
        WebServerResponse response = context.getResponse();
        String game_code = UUID.randomUUID().toString().substring(0, 8);
        String cookie = UUID.randomUUID().toString().substring(0,8);
        try {
            GameDao.getDao().createGame(game_code);
            PlayerDao.getDao().createPlayer(cookie);
            

        } catch (SQLException e) {
            response.serverError("Erreur lors de la création de la partie");
            System.err.println("Erreur lors de la création de la partie");
            
        }

    }

}
