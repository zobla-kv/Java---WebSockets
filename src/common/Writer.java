package common;

import java.io.*;
import java.net.Socket;

public class Writer {

    // use PrintWriter instead of BufferedWriter to send whole string
    PrintWriter writer;

    // parametrized constuctor, create Writer attached to a socket
    public Writer(Socket socket) throws IOException {
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    // write message to socket
    public void writeToSocket(Socket socket, String message) {
        // INSERT NEW LINE BEFORE EVERY MESSAGE
        // BECAUSE READER READS LINE BY LINE
        this.writer.println();
        this.writer.println(message);
//        writer.println();
//        writer.println("TEST");
        this.writer.flush();
//        writer.close();
    }

    // close stream
    public void close() { this.writer.close(); }
}
