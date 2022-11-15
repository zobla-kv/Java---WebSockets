package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader {

    // common.Reader connected to a stream
    BufferedReader reader;

    // parametrized constuctor, create Reader attached to a socket
    // TODO: not good atm because readFromSocket expects a socket as arg (it is already set here)
    public Reader(Socket socket) throws IOException {
        InputStreamReader stream = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(stream);
        this.reader = reader;
    }

    // read message from socket
    public String readFromSocket() throws IOException {
        String data = "";
        while(reader.ready() && reader.readLine() != null) {
            data += reader.readLine();
        }
        return data;
    }
}
