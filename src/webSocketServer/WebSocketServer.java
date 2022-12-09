package webSocketServer;

import models.MethodModel;
import services.ThreadService;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/hello")
public class WebSocketServer {

    private Session session;
    private EndpointConfig endpointConfig;

    public WebSocketServer() {
        ClientEndpointConfig builder = ClientEndpointConfig.Builder.create().build();
        System.out.println("builder: " + builder);
    }

    @OnOpen
    public void createSession(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        this.endpointConfig = endpointConfig;
        System.out.println("session: " + session);
        System.out.println("endpointConfig: " + endpointConfig);
    }

}
