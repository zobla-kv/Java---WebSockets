package server;

import client.Client;
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
import java.util.Collections;
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

    public void waitForClients() {
        while(true) {
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
            String clientName = addClientConfig(specificClientSocket);
            handleClientConnection(clients.get(clientName));
    }

    // setup new client data on server
    private String addClientConfig(Socket specificClientSocket) throws IOException {
        // increase clientId
        this.clientId++;
        // generate client name
        String name = "Client_" + clientId;
        // map writer to client
        Writer writer = new Writer(specificClientSocket);
        // map clientId to client address
        this.clients.put(name, new ClientModel(name, specificClientSocket, writer));
        return name;
    }

    // used after establishing connection
    public void handleClientConnection(ClientModel client) {
        try {
            String message;
            Reader reader = new Reader(client.socket);
            logMessage("connection with client " + client.name + " successful: " + this.clients);
            do {
                message = reader.readFromSocket(client.socket);
                if (!message.equals("")) {
                    if(message.equals(Constants.ABORT_CONNECTION_MESSAGE)) {
                        this.closeSocket(client);
                        break;
                    }
                    logMessage("new message: " + message);
                }
            } while(true);
        } catch (IOException ex) {
            logMessage("connection with client: " + client.name + " failed");
            ex.printStackTrace();
        }
    }

    // close socket and cleanup
    // TODO: do cleanup when server closes connection
    private void closeSocket(ClientModel client) {
        try {
            // close socket, this will close read/write streams also
            client.socket.close();
            // remove from clients hashmap
            this.clients.remove(client.name);
        }
        catch(IOException ex) {
            logMessage("failed to close connection: " + ex);
        }
        logMessage("connection with client: " + client.name + " successfully closed: " + this.clients);
    }

    // send message to client
    public void sendMessage(String clientName, String message) {
        logMessage("trying to send message: " + message);
//        try {
            // TODO: right now writer is created on each sendMessage call
            // TODO: try to assign writer in hashmap for each socket and use it as long as socket is open
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

    // get socket for specific client
    // TODO: FIGURE OUT HOW TO PASS CLIENT NAME TO SERVER AND PUT IN HASHMAP
    public void getClientSocket(String clientName) {
//        return
    }

    // log message to the console
    // TODO: add server name or something to log
    private void logMessage(String message) {
        String msg = "SERVER: " + message;
        LoggingService.logMessage(msg);
    }
}
