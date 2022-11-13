package client;

import common.Reader;
import common.Writer;
import constants.Constants;
import models.MethodModel;
import server.Server;
import services.LoggingService;
import services.ThreadService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Collections;

public class Client {

    public Socket socket;

    private final String name;
    private Writer writer;
    private Reader reader;

    public Client(String name, String host, int port) {
        this.name = name;
        this.connectToServer(host, port);
    }

    public String getName() { return this.name; }

    // connect to a server
    public void connectToServer(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            ThreadService.runInSeparateThread(new MethodModel(
                    this,
                    "handleServerConnection",
                    null
            ));
        }
        catch(IOException ex) {
            logMessage("failed to connect to server: " + ex);
            ex.printStackTrace();
        }
    }

    // handles server related activities
    public void handleServerConnection() {
        try {
            // TODO: remove 'this', figure out how to pass socket to runInSeparateThread method
            setupStreams(this.socket);
            handleServerAction(this.socket);
        } catch (IOException ex) {
            logMessage("connection with server failed: " + ex);
            ex.printStackTrace();
        }
    }

    // initialize read/write streams
    private void setupStreams(Socket socket) throws IOException {
        // TODO: 'this' allowed for client because client has 1 socket (1 reader/writer is enough)
        // TODO: for having multiple sockets on same client this wouldn't work
        this.writer = new Writer(socket);
        this.reader = new Reader(socket);
    }

    // handle server related action (read from server)
    public void handleServerAction(Socket socket) throws IOException {
        logMessage("connected with server");
        String message;
        do {
            message = reader.readFromSocket(socket);
            if (!message.equals("")) {
                if(message.equals(Constants.ABORT_CONNECTION_MESSAGE)) {
                    this.closeSocket();
                    break;
                }
                logMessage("new message: " + message);
            }
        } while(true);
    }

    public void sendMessage(String message) {
        logMessage("trying to send message: " + message);
        this.writer.writeToSocket(this.socket, message);
    }

    // close socket and cleanup
    private void closeSocket() {
        try {
            // close socket, this will close read/write streams also
            this.socket.close();
            // TODO: also close socket when client initiates
        }
        catch(IOException ex) {
            logMessage("failed to close connection: " + ex);
            ex.printStackTrace();
        }
        logMessage("connection successfully closed");
    }

    // log message to the console
    private void logMessage(String message) {
        String msg = "CLIENT " + this.getName() + ": " + message;
        LoggingService.logMessage(msg);
    }

}
