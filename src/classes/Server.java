package classes;

import services.LoggingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable {

    public ServerSocket serverSocket;
    // serverSocket socket - for handling all requests (serverSocket.accept() returns new socket for each client)
    //       Socket socket - for each client communication


    // server port
    private final int port;

    // connected clients
    private HashMap<Integer, Socket> clients = new HashMap<>();

    // client ID TODO: replace clientId with clientName received from client
    private int clientId = 0;

    // constructor
    public Server(int port) {
        this.port = port;
        this.run();
    }

    // make clients private
    public HashMap<Integer , Socket> getClients() { return this.clients; }

    //TODO: JEDAN THREAD DA SLUSA
    //TODO: SVAKI KLIJENT PO JEDAN THREAD ZA CHAT
    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            logMessage("started: " + this.serverSocket);

            // ONLY ESTABLISH SEPARATE NEW SOCKET FOR EACH CLIENT
            // AND GET BACK TO WAITING FOR NEW CLIENTS
            Thread handleNewClients = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        handleNewClient();
                    }
                }
            });
            handleNewClients.start();
        }
        catch(IOException ex) {
            logMessage("failed to start: " + ex);
            ex.printStackTrace();
        }
    }

    // used only for establishing connection
    private void handleNewClient() {
        try {
            // accept method blocks until request comes
            // then returns different socket on another anonymous port to communicate with client
            logMessage("waiting for client...");
            Socket specificClientSocket = this.serverSocket.accept();
            // increase clientId
            this.clientId++;
            // map clientId to client address
            this.clients.put(this.clientId, specificClientSocket);
            logMessage("connection with client successful: " + this.clients);
            Thread handleClientConnectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleClientConnection(specificClientSocket, clientId);
                }
            });
            handleClientConnectionThread.start();
        }
        catch (IOException ex) {
            logMessage("failed to establish connection with client: " + ex);
            ex.printStackTrace();
        }
    }

    // used after establishing connection
    private void handleClientConnection(Socket socket, int clientId) {
        try {
            handleClientAction(socket, clientId);
        } catch (IOException ex) {
            // TODO: find a way to print which client
            logMessage("connection with client failed: " + ex);
            ex.printStackTrace();
        }
    }

    // used for communication (read messages from client)
    private void handleClientAction(Socket socket, int clientId) throws IOException {
        String message = "";
        Reader reader = new Reader(socket);
        do {
            message = reader.readFromSocket(socket);
            if (message != "") {
                logMessage("new message: " + message);
            }
        } while(!message.equals("END"));
        // when message == "END" from client, close the socket
        // TODO: find a way to remove clientId
        this.closeSocket(socket, clientId);
    }

    // send message to client
    public void sendMessage(Socket clientSocket, String message) {
        logMessage("trying to send message: " + message);
        try {
            // TODO: right now writer is created on each sendMessage call
            // TODO: try to assign writer in hashmap for each socket and use it as long as socket is open
            Writer writer = new Writer(clientSocket);
            writer.writeToSocket(clientSocket, message);
//            writer.close();
        }
        catch(IOException ex) {
            // TODO: to who?
            logMessage("failed to send message: " + ex);
            ex.printStackTrace();
        }
    }

    // close socket and cleanup
    // TODO: find a way to remove clientId from param list
    private void closeSocket(Socket socket, int clientId) {
        try {
            // close socket, this will close read/write streams also
            socket.close();
            // remove from clients hashmap
            this.clients.remove(clientId);
            // TODO: Not proper cleanup, getting 5 sockets in a list when there should be 2 (current setup in main)
            // TODO: also do cleanup when server closes connection
        }
        catch(IOException ex) {
            logMessage("failed to close connection: " + ex);
        }
        // TODO: find a way to log what client left
        logMessage("connection successfully closed: " + this.clients);
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
