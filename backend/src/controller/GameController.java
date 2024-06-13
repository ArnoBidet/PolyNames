package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import dao.CardDao;
import dao.GameDao;
import dao.PlayerDao;
import dao.WordDao;
import model.Card;
import model.ChooseRoleRequest;
import model.Game;
import model.Word;
import model.Player;
import model.PlayerRole;
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

            List<Word> words = WordDao.getDao().getRandomList();
            CardDao.getDao().createCards(game_id, words);

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

    public static void choseRole(WebServerContext context) {
        WebServerResponse response = context.getResponse();
        String game_id = context.getRequest().getParam("game_id");
        ChooseRoleRequest body = context.getRequest().extractBody(ChooseRoleRequest.class);
        if (body == null) {
            response.badRequest("Requête invalide; pas de corps de requête");
            return;
        }
        try {
            List<Player> players = PlayerDao.getDao().getPlayerByGameCode(game_id);
            Player sender = players.stream().filter(player -> player.user_id().equals(body.user_id())).findFirst()
                    .orElse(null);
            Player other = players.stream().filter(player -> !player.user_id().equals(body.user_id())).findFirst()
                    .orElse(null);
            if (sender == null) {
                response.badRequest("Joueur non trouvé");
                return;
            } else if (other == null) {
                response.forbidden("Il n'y a pas d'autre joueur dans la partie");
                return;
            } else if (!sender.host()) {
                response.forbidden("Vous n'êtes pas l'hôte de la partie");
                return;
            }
            String hostRole = body.role();
            if (!PlayerRole.isValid(hostRole)) {
                response.badRequest(
                        "Rôle invalide, doit être " + PlayerRole.GUESS_MASTER + " ou " + PlayerRole.WORD_MASTER);
                return;
            }

            String otherRole = PlayerRole.getOppositeRole(hostRole);
            PlayerDao.getDao().setPlayerRole(sender.user_id(), hostRole);
            PlayerDao.getDao().setPlayerRole(other.user_id(), otherRole);
            context.getSSE().emit("wait_for_role_" + game_id, "{\"role\":\"" + otherRole + "\"}");

            response.json("{\"role\":\"" + hostRole + "\"}");
        } catch (SQLException e) {
            response.serverError("Erreur lors de la sélection du rôle");
            System.err.println("Erreur lors de la sélection du rôle");
        }
    }

    public static void getCards(WebServerContext context) {
        WebServerResponse response = context.getResponse();
        String game_id = context.getRequest().getParam("game_id");
        String user_id = context.getRequest().getParam("user_id");
        try {
            List<Player> players = PlayerDao.getDao().getPlayerByGameCode(game_id);
            Player player = players.stream().filter(p -> p.user_id().equals(user_id)).findFirst().orElse(null);
            if (player == null) {
                response.badRequest("Joueur non trouvé");
                return;
            }
            List<Card> cards = CardDao.getDao().getCards(game_id);
            System.out.println(player.user_id() + "" + user_id);
            System.out.println(player.player_role() + " " + player.player_role().equals(PlayerRole.GUESS_MASTER));
            if (player.player_role().equals(PlayerRole.GUESS_MASTER)) {
                cards = cards.stream().map(card -> {
                    return new Card(card.game_id(), card.grid_row(), card.grid_col(), card.word(), null, false);
                }).toList();
            }

            response.json(cards);
        } catch (SQLException e) {
            response.serverError("Erreur lors de la récupération des cartes");
            System.err.println("Erreur lors de la récupération des cartes");
        }
    }
}
