package controller;

import java.sql.SQLException;

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

        String user_id = request.getCookie("user");
        HintRequest hint = request.extractBody(HintRequest.class);
        String game_id = request.getParam("game-code");

        Player player;
        try {
            player = PlayerDao.getDao().getPlayer(user_id);

            String role = player.player_role();

            if (!role.equals(PlayerRole.GUESS_MASTER)) {
                response.forbidden("Mauvais rôle !");
            }
            if (hint.hint().indexOf(" ") != -1 || hint.hint().length() > 27) {
                response.badRequest("Mauvais mot !");
            }
            if (hint.associated_guess() > 8 || hint.associated_guess() < 1) {
                response.badRequest("Mauvais nombre d'indices !");
            }

            Hint last_hint = HintDao.getDao().getLastHint(game_id);

            HintDao.getDao().createHint(game_id, 0, role, last_hint != null ? last_hint.game_round() + 1 : 0);

            // @TODO finir la réponse avec le sse

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

            if(card.card_type()==CardType.KILLER){
                //@TODO partie terminée 
                return;
            }
            else if(card.card_type()==CardType.GUESS){
                found_cards++;
                is_done = last_hint.associated_cards()+1 == found_cards;
            }

            HintDao.getDao().updateHint(last_hint.game_id(), last_hint.game_round(), found_cards, is_done);

            // @TODO finir la réponse avec le sse

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

}
