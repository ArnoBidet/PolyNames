import controller.GameController;
import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        final int port_number = 8081;
        webserver.getRouter().get(
                "/api/test-cookie",
                (WebServerContext context) -> {
                    GameController.createGame(context);
                });
        webserver.listen(port_number);
    }
}
