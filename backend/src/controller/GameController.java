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
        String game_id = UUID.randomUUID().toString().substring(0, 8);
        String user_id = UUID.randomUUID().toString().substring(0, 8);
        try {
            GameDao.getDao().createGame(game_id);
            PlayerDao.getDao().createPlayer(user_id, game_id, true);
            
            response.json("{\"game_id\":\"" + game_id + "\", \"user_id\":\"" + user_id + "\"}");
        } catch (SQLException e) {
            response.serverError("Erreur lors de la création de la partie");
            System.err.println("Erreur lors de la création de la partie");
            
        }
    }

    public static void joinGame(WebServerContext context) {
        WebServerResponse response = context.getResponse();
        String game_id = context.getRequest().getParam("game_id");
        String user_id = UUID.randomUUID().toString().substring(0, 8);
        try {
            Game game = GameDao.getDao().getGame(game_id);
            if (game == null) {
                response.badRequest("Partie non trouvée");
                return;
            }
            PlayerDao.getDao().createPlayer(user_id, game_id, false);
            response.json("{\"game_id\":\"" + game_id + "\", \"user_id\":\"" + user_id + "\"}");
            context.getSSE().emit("player_waiting_" + game_id, "player_joined");
        } catch (SQLException e) {
            response.serverError("Erreur lors de la connexion à la partie");
            System.err.println("Erreur lors de la connexion à la partie");
        }
    }

    }

}
