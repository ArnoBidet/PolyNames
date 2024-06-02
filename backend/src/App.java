import webserver.WebServer;
import webserver.WebServerContext;

public class App {
    public static void main(String[] args) throws Exception {
        WebServer webserver = new WebServer();
        final int port_number = 8081;
        webserver.getRouter().get(
                "/hello_world",
                (WebServerContext context) -> {
                    context.getResponse().ok("Hello, world!");
                });
        webserver.listen(port_number);
    }
}
