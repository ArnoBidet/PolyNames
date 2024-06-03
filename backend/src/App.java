import controller.GameController;
import controller.HintController;
import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        final int port_number = 8081;
        webserver.getRouter().post(
                "/api/new-game",
                (WebServerContext context) -> {
                    GameController.createGame(context);
                });

        webserver.getRouter().post(
                "/api/:game_code/hint", (WebServerContext context) -> {
                    HintController.createHint(context);
                });

        webserver.getRouter().post(
                "/api/:game_code/guess", (WebServerContext context) -> {
                    HintController.makeGuess(context);
                });

        webserver.listen(port_number);
    }
}
