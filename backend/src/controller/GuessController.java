package controller;

import java.sql.SQLException;

import dao.GuessDao;
import dao.PlayerDao;
import model.Guess;
import model.HintRequest;
import model.Player;
import model.PlayerRole;
import webserver.WebServerContext;
import webserver.WebServerRequest;
import webserver.WebServerResponse;

public class GuessController {
    private static GuessDao guessDao;
    private static PlayerDao playerDao;

    private static GuessDao getGuessDao() {

        if (guessDao == null) {
            guessDao = new GuessDao();
        }
        return guessDao;

    }

    private static PlayerDao getPlayerDao() {

        if (playerDao == null) {
            playerDao = new PlayerDao();
        }
        return playerDao;

    }

    public static void createGuess(WebServerContext context) {

        WebServerRequest request = context.getRequest();
        WebServerResponse response = context.getResponse();

        String user_id = request.getCookie("user");
        HintRequest hint = request.extractBody(HintRequest.class);
        String game_id = request.getParam("game-code");

        System.out.println(game_id);

        if (hint.hint().indexOf(" ") != -1 || hint.hint().length() > 27) {
            response.badRequest("Mauvais mot !");
        }
        if (hint.associated_guess() > 8 || hint.associated_guess() < 1) {
            response.badRequest("Mauvais nombre d'indices !");
        }
        Player player;
        try {
            player = getPlayerDao().getPlayer(user_id);

            String role = player.player_role();

            if (!role.equals(PlayerRole.INTUITIONS)) {
                response.badRequest("Mauvais rôle !");
            }

            Guess last_guess = guessDao.getLastGuess(game_id);

            guessDao.createGuess(game_id, 0, role, last_guess != null ? last_guess.game_round() + 1 : 0);

            // @TODO finir la réponse avec le sse

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}
