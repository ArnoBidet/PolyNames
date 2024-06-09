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
            PlayerDao.getDao().createPlayer(cookie, game_code, true);
            
            response.addCookie("user", cookie);
            response.json("{\"game_code\":\"" + game_code + "\"}");           
        } catch (SQLException e) {
            response.serverError("Erreur lors de la création de la partie");
            System.err.println("Erreur lors de la création de la partie");
            
        }
    }

    public static void joinGame(WebServerContext context) {
        System.out.println("player_waiting_");
        WebServerResponse response = context.getResponse();
        String game_code = context.getRequest().getParam("game_code");
        System.out.println("player_waiting_"+game_code);
        String cookie = UUID.randomUUID().toString().substring(0,8);
        try {
            PlayerDao.getDao().createPlayer(cookie, game_code, false);
            response.addCookie("user", cookie);
            response.json("{\"game_code\":\"" + game_code + "\"}");
            System.out.println("player_waiting_"+game_code);
            context.getSSE().emit("player_waiting_"+game_code, "player_joined");
        } catch (SQLException e) {
            response.serverError("Erreur lors de la connexion à la partie");
            System.err.println("Erreur lors de la connexion à la partie");
        }
    }

    }

}
