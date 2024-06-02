package webserver;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class WebServerSSE {

    private WebServerRouter router;

    private HashMap<String, OutputStream> clientStreams;
    private HashMap<String, ArrayList<String>> channels;

    public WebServerSSE(WebServerRouter router)
    {
        this.router = router;

        this.clientStreams = new HashMap<>();
        this.channels = new HashMap<>();

        this.init();
    }

    private void init()
    {
        router.get("/__sse/:clientId", (WebServerContext context) -> { this.connect(context);} );
        router.post("/__sse/:clientId/channel/:channel", (WebServerContext context) -> { this.subscribe(context);} );
        router.delete("/__sse/:clientId/channel/:channel", (WebServerContext context) -> { this.unsubscribe(context);} );
    }

    public void connect(WebServerContext context)
    {
        String clientId = context.getRequest().getParam("clientId");
        OutputStream outputStream = context.getResponse().openSSEStream();

        clientStreams.put(clientId, outputStream);
    }

    public void subscribe(WebServerContext context)
    {
        String clientId = context.getRequest().getParam("clientId");
        String channelName = context.getRequest().getParam("channel");

        if(this.clientStreams.get(clientId) == null)
        {
            context.getResponse().notFound("Unknown Client ID");
            return;
        }

        ArrayList<String> channelUsers = this.channels.get(channelName);

        if(channelUsers == null)
        {
            channelUsers = new ArrayList<String>();
            this.channels.put(channelName, channelUsers);
        }
        else if(channelUsers.contains(clientId))
        {
            context.getResponse().ok("Already subscribed to this channel");
            return;
        }

        channelUsers.add(clientId);

        context.getResponse().ok("");

        this.emit(channelName, String.format("New user: %s", clientId));
    }    

    public void unsubscribe(WebServerContext context)
    {
        String clientId = context.getRequest().getParam("clientId");
        String channelName = context.getRequest().getParam("channel");

        if(this.clientStreams.get(clientId) == null)
        {
            context.getResponse().notFound("Unknown Client ID");
            return;
        }

        ArrayList<String> channelUsers = this.channels.get(channelName);

        if(channelUsers == null)
        {
            context.getResponse().notFound("Unknown Channel");
            return;
        }

        if(channelUsers.contains(clientId) == false)
        {
            context.getResponse().notFound("Not subscribe to this channel");
            return;
        }

        channelUsers.remove(clientId);

        context.getResponse().ok("");
    }

    public void emit(String channel, String message)
    {
        ArrayList<String> channelUsers = this.channels.get(channel);

        if(channelUsers == null)
            return;

        for(String clientId: channelUsers)
        {
            this.emit(this.clientStreams.get(clientId), channel, message);
        }
    }

    private void emit(OutputStream stream, String channel, String message)
    {
        if(stream == null)
            return;

        try{
            stream.write(String.format("event: %s\ndata: %s", channel, message).getBytes());
            stream.write("\n\n".getBytes());
            stream.flush();
        }
        catch(Exception e)
        {

        }
    }
}
