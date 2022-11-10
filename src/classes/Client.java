package classes;

import models.MethodModel;
import services.LoggingService;
import services.ThreadService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;

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
            MethodModel<Client, Socket> method = new MethodModel<>(this, "handleServerConnection", this.socket);
            ThreadService.runInSeparateThread(method);

//            Thread handleServerConnectionThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handleServerConnection(socket);
//                }
//            });
//            handleServerConnectionThread.start();
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
    private void handleServerAction(Socket socket) throws IOException {
        System.out.println("MDKF CALLED");
        String message;
        do {
            message = this.reader.readFromSocket(socket);
            if (!message.equals("")) {
                logMessage("new message: " + message);
            }
        } while(!message.equals("END"));
//         when message == "END" from client, close the socket
        this.closeSocket();
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
