import controller.GameController;
// import controller.HintController;
import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        final int port_number = 8081;
        webserver.getRouter().post(
                "/api/create-game",
                (WebServerContext context) -> {
                    GameController.createGame(context);
                });
        webserver.getRouter().post(
                "/api/join-game/:game_code",
                (WebServerContext context) -> {
                    GameController.joinGame(context);
                });

        webserver.getRouter().post(
                "/api/chose-role/:game_code",
                (WebServerContext context) -> {
                    GameController.joinGame(context);
                });

        webserver.listen(port_number);
    }
}
