package controller;

import java.sql.SQLException;
import java.util.UUID;

import dao.GameDao;
import dao.PlayerDao;
import webserver.WebServerContext;
import webserver.WebServerResponse;

public class GameController {

    private static GameDao gameDao;
    private static PlayerDao playerDao;


    private static GameDao getGameDao() {
        if (gameDao == null) {
            gameDao = new GameDao();
        }
        return gameDao;

    }

    private static PlayerDao getPlayerDao() {
        if (playerDao == null) {
            playerDao = new PlayerDao();
        }
        return playerDao;

    }

    public static void createGame(WebServerContext context) {
        WebServerResponse response = context.getResponse();
        String game_code = UUID.randomUUID().toString().substring(0, 8);
        String cookie = UUID.randomUUID().toString().substring(0,8);
        try {
            getGameDao().createGame(game_code);
            getPlayerDao().createPlayer(cookie);
            

        } catch (SQLException e) {
            response.serverError("Erreur lors de la création de la partie");
            System.err.println("Erreur lors de la création de la partie");
            
        }

    }

}
