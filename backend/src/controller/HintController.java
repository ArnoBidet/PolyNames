package controller;

import java.sql.SQLException;
import java.util.List;

import dao.CardDao;
import dao.HintDao;
import dao.PlayerDao;
import model.Card;
import model.CardType;
import model.GuessRequest;
import model.Hint;
import model.HintRequest;
import model.Player;
import model.PlayerRole;
import webserver.WebServerContext;
import webserver.WebServerRequest;
import webserver.WebServerResponse;

public class HintController {
    public static void createHint(WebServerContext context) {

        WebServerRequest request = context.getRequest();
        WebServerResponse response = context.getResponse();

        HintRequest hint = request.extractBody(HintRequest.class);
        String game_id = context.getRequest().getParam("game_id");
        String user_id = hint.user_id();

        try {
            List<Player> players = PlayerDao.getDao().getPlayerByGameCode(game_id);
            Player sender = players.stream().filter(player -> player.user_id().equals(user_id)).findFirst()
                    .orElse(null);
            Player other = players.stream().filter(player -> !player.user_id().equals(user_id)).findFirst()
                    .orElse(null);

            String role = sender.player_role();

            if (!role.equals(PlayerRole.WORD_MASTER)) {
                response.forbidden("Mauvais rôle !");
            }
            if (hint.hint().indexOf(" ") != -1 || hint.hint().length() > 27) {
                response.badRequest("Format incorrect !");
            }
            if (hint.associated_guess() > 8 || hint.associated_guess() < 1) {
                response.badRequest("Mauvais nombre d'indices !");
            }
            if (hint.associated_guess() <= 0 || hint.associated_guess() >= 9) {
                response.badRequest("Mauvais nombre de cartes associées, doit être en 1 et 8 !");
            }

            Hint last_hint = HintDao.getDao().getLastHint(game_id);
            int current_round = last_hint != null ? last_hint.game_round() + 1 : 0;
            HintDao.getDao().createHint(game_id, current_round, role, hint.associated_guess());
            System.out.println("game_flow_" + other.user_id() + "_" + game_id);
            context.getSSE().emit("game_flow_" + other.user_id() + "_" + game_id,
                    "{\"hint\":\"" + hint.hint() + "\", \"associated_guess\":\"" + hint.associated_guess() + "\"}");
            response.json(
                    "{\"hint\":\"" + hint.hint() + "\", \"associated_guess\":\"" + hint.associated_guess() + "\"}");

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void makeGuess(WebServerContext context) {

        WebServerRequest request = context.getRequest();
        WebServerResponse response = context.getResponse();

        String user_id = request.getCookie("user");
        GuessRequest guess = request.extractBody(GuessRequest.class);
        String game_id = request.getParam("game-code");

        Player player;
        try {
            player = PlayerDao.getDao().getPlayer(user_id);

            String role = player.player_role();

            if (!role.equals(PlayerRole.GUESS_MASTER)) {
                response.badRequest("Mauvais rôle !");
                return;
            }
            Hint last_hint = HintDao.getDao().getLastHint(game_id);
            if (last_hint.is_done() || last_hint.associated_cards() + 1 < last_hint.found_cards()) {
                response.badRequest("Ce n'est pas votre tour !");
                return;
            }

            if (guess.row() < 5 && guess.row() >= 0 && guess.column() < 5 && guess.column() >= 0) {
                response.badRequest("En dehors du tableau !");
                return;
            }

            Card card = CardDao.getDao().getCard(game_id, guess.row(), guess.column());
            if (card.is_discovered()) {
                response.badRequest("Vous avez déjà proposé cette carte !");
                return;
            }

            int found_cards = last_hint.found_cards();
            boolean is_done = last_hint.is_done();

            if (card.card_type() == CardType.KILLER) {
                // @TODO partie terminée
                return;
            } else if (card.card_type() == CardType.GUESS) {
                found_cards++;
                is_done = last_hint.associated_cards() + 1 == found_cards;
            }

            HintDao.getDao().updateHint(last_hint.game_id(), last_hint.game_round(), found_cards, is_done);

            // @TODO finir la réponse avec le sse

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}
