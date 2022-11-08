import java.io.IOException;
import java.net.Socket;

public class Client {

    private String name;
    public Socket socket;

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
            Thread handleServerConnectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleServerConnection(socket);
                }
            });
            handleServerConnectionThread.start();
        }
        catch(IOException ex) {
            System.out.println("CLIENT: failed to connect to server: " + ex);
        }
    }

    // handles server related activities
    private void handleServerConnection(Socket socket) {
        try {
            setupStreams(socket);
            handleServerAction(socket);
        } catch (IOException ex) {
            System.out.println("CLIENT: connection with server failed: " + ex);
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
        String message = "";
        do {
            message = this.reader.readFromSocket(socket);
            if (message != "") {
                System.out.println("CLIENT: new message: " + message);
            }
        } while(!message.equals("END"));
//         when message == "END" from client, close the socket
        this.closeSocket();
    }

    public void sendMessage(String message) {
        System.out.println("CLIENT: trying to send message: " + message);
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
            System.out.println("SERVER: failed to close socket: " + ex);
        }
        System.out.println("CLIENT: server closed connection: " + this.getName());
    }

}
