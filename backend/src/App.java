import controller.GameController;
import controller.GuessController;
import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        final int port_number = 8081;
        webserver.getRouter().get(
                "/api/new-game",
                (WebServerContext context) -> {
                    GameController.createGame(context);
                });
        
        
        
        webserver.getRouter().get(
            "/api/:game_code/hint", (WebServerContext context)->{
                    GuessController.createHint(context);
            });
        
        
        
        


        webserver.listen(port_number);
    }
}
