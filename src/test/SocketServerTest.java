package test;

import client.Client;
import server.Server;
import constants.Constants;

import java.io.IOException;

public class SocketServerTest {

    public static void start() {
        try {
            // new server
            Server server1 = new Server(5000);

            Thread.sleep(2000);
            Client zobla = new Client("Zobla", Constants.LOCALHOST , Constants.SERVER_PORT);

            Thread.sleep(2000);
            Client gondor = new Client("Gondor", Constants.LOCALHOST , Constants.SERVER_PORT);

            Thread.sleep(2000);
            gondor.sendMessage("GONDOR TO SERVER");

            Thread.sleep(2000);
            zobla.sendMessage(Constants.ABORT_CONNECTION_MESSAGE);

            Thread.sleep(2000);
            zobla.sendMessage("ZOBLA DISCONNECTED MESSAGE");

            Thread.sleep(2000);
            gondor.sendMessage("GONDOR TO SERVER");

            Thread.sleep(2000);
            server1.sendMessage("Zobla", "SISO 1");

            Thread.sleep(2000);
            zobla.connectToServer(Constants.LOCALHOST, Constants.SERVER_PORT);

            Thread.sleep(2000);
            zobla.sendMessage("ZOBLA IS BACK");

            Thread.sleep(2000);
            server1.sendMessage("Zobla", "SISO 2");

            Thread.sleep(2000);
            gondor.sendMessage(Constants.ABORT_CONNECTION_MESSAGE);

            zobla.sendMessage(Constants.ABORT_CONNECTION_MESSAGE);
            zobla.connectToServer(Constants.LOCALHOST, Constants.SERVER_PORT);

//            server1.close();

        } catch(InterruptedException ex) {
            System.out.println("Failed to run SocketServer test: " + ex);
        }
    }



    // TODO: escape '\n' like things when sending messages as writer stays open
}
