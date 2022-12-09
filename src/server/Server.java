package server;

import common.Reader;
import common.Writer;
import constants.Constants;
import models.ClientModel;
import models.MethodModel;
import services.LoggingService;
import services.ThreadService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    public ServerSocket serverSocket;
    // serverSocket socket - for handling all requests (serverSocket.accept() returns new socket for each client)
    //       Socket socket - for each client communication

    // server port
    private final int port;

    // connected clients
    private final HashMap<String, ClientModel> clients = new HashMap<>();

    // client ID TODO: replace clientId with clientName received from client
    private long clientId = 0;

    // constructor
    public Server(int port) {
        this.port = port;
        this.start();
    }

    // get all clients
    public HashMap<String , ClientModel> getClients() { return this.clients; }

    // get single client
    public ClientModel getClient(String clientName) { return this.clients.get(clientName); }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            logMessage("started: " + this.serverSocket);
            ThreadService.runInSeparateThread(new MethodModel(this, "waitForClients", null ));
        }
        catch(IOException ex) {
            logMessage("failed to start: " + ex);
            ex.printStackTrace();
        }
    }

    public void close() throws IOException {
        try {
            logMessage("closing...");
            this.serverSocket.close();
        }
        catch(IOException ex) {
            logMessage("failed to close: " + ex);
            ex.printStackTrace();
        }
    }

    public void waitForClients() {
        while(!this.serverSocket.isClosed()) {
           try {
               logMessage("waiting for client...");
               // accept method blocks until request comes
               // then returns different socket on another anonymous port to communicate with client
               Socket specificClientSocket = this.serverSocket.accept();
               ThreadService.runInSeparateThread(new MethodModel(this, "handleClient", specificClientSocket));
           }
           catch (IOException ex) {
               logMessage("failed to establish connection with client: " + ex);
               ex.printStackTrace();
           }
        }
    }

    // handle new client
    public void handleClient(Socket specificClientSocket) throws IOException {
//            String clientName = addClientConfig(specificClientSocket);
            handleClientConnection(specificClientSocket);
    }

    // setup new client data on server
    // TODO: generate random number to prevent clients with same names
    private ClientModel addClientConfig(Socket clientSocket, String message) throws IOException {
        String clientName = getParameterFromMessage("name", message);
        ClientModel client = new ClientModel(clientName, clientSocket, new Writer(clientSocket));
        // increase clientId
        this.clientId++;
        // map client name to client settings
        clients.put(clientName, client);
        return client;
    }

    // get name
    private String getParameterFromMessage(String param, String message) {
        // TODO: + 1 for }, not good for multiple params
        int index = message.indexOf(param + "=") + param.length() + 1;
        String value = "";
        for (int i = index; i < message.length() - 1; i++) {
            value += message.charAt(i);
        }
        // remove double quotes
        return value.replace("\"", "");
    }

    // used after establishing connection
    public void handleClientConnection(Socket specificClientSocket) {
        ClientModel client = new ClientModel(null, null, null);
        try {
            String message;
            Reader reader = new Reader(specificClientSocket);
            do {
                message = reader.readFromSocket();
                if (!message.equals("")) {
                    if (message.startsWith(Constants.CLIENT_CONFIG_MESSAGE)) {
                        client = addClientConfig(specificClientSocket, message);
                        logMessage("connection with client " + client.name + " successful: " + this.clients);
                    }
                    if(message.equals(Constants.ABORT_CONNECTION_MESSAGE)) {
                        this.disconnectClient(client.name);
                        break;
                    }
                    logMessage("new message: " + message);
                }
            } while(true);
        } catch (IOException ex) {
            // TODO: client maybe closed connection unexpectedly
            //  without sending right keyword
            //  do cleanup also - call disconnectClient
            logMessage("connection with client: " + client.name + " failed");
            ex.printStackTrace();
        }
    }

    // close socket and cleanup
    // TODO: DO I REALLY NEED ClientModel or Client is enough or maybe just a name?
    //  already changed, try to switch to Client ?
    private void disconnectClient(String clientName) {
        try {
            // close socket, this will close read/write streams also
            this.clients.get(clientName).socket.close();
            // remove from clients hashmap
            this.clients.remove(clientName);
        }
        catch(IOException ex) {
            logMessage("failed to disconnect client: " + ex);
        }
        logMessage("client: " + clientName + " successfully disconnected: " + this.clients);
    }

    // send message to client
    public void sendMessage(String clientName, String message) {
        if (!isClientConnected(clientName)) {
            // TODO: throw exception or something
            logMessage("send message failed: client " + clientName + " not connected");
            return;
        }
        logMessage("trying to send message: " + message);
//        try {
            ClientModel client = clients.get(clientName);
            client.writer.writeToSocket(client.socket, message);
//            writer.close();
//        }
        // TODO: after changing to client.writer.writeToSocket
        //  from new Writer().writeToSocket
        //  IOException is gone. WHY ??
//        catch(IOException ex) {
//            // TODO: to who?
//            logMessage("failed to send message: " + ex);
//            ex.printStackTrace();
//        }
    }

    private boolean isClientConnected(String name) {
        return clients.containsKey(name);
    }

    // log message to the console
    // TODO: add server name or something to log
    private void logMessage(String message) {
        String msg = "SERVER: " + message;
        LoggingService.logMessage(msg);
    }
}
