import classes.Client;
import classes.Server;
import constants.Constants;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // new server
        Server server1 = new Server(5000);

        // pause main thread
        Thread.sleep(2000);
//
        // new classes.Client connect to server
        Client gondor = new Client("Gondor", Constants.LOCALHOST , Constants.SERVER_PORT);

        // pause main thread
        Thread.sleep(2000);
////
        // new classes.Client connect to server
        gondor.sendMessage("GONDOR TO SERVER");
////
//        // new classes.Client connect to server
//        System.out.println("GUSTER CONNECTING_____");
//        Thread.sleep(2000);
//        classes.Client guster = new classes.Client("Guster", "localhost", 5000);
//
//        // zobla write to server
//        System.out.println("ZOBLA SENDING MESSAGE TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.sendMessage(zobla.getName() + ": first message");
//
//        // server close connection with zobla
//        System.out.println("SERVER CLOSE CONNECTION WITH ZOBLA_____");
//        Thread.sleep(2000);
//        server1.sendMessage(server1.getClients().get(1), "END");
//
//        // zobla write to server
//        System.out.println("ZOBLA DISCONNECTED SEND MESSAGE TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.sendMessage(zobla.getName() + ": disconnected by server");
//
//        // zobla connect to server
//        System.out.println("ZOBLA CONNECT TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.connectToServer("localhost", 5000);
//
//        // zobla write to server
//        System.out.println("ZOBLA SEND MESSAGE TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.sendMessage(zobla.getName() + ": first message after getting back");
//
//        // zobla close connection with server
//        System.out.println("ZOBLA CLOSE CONNECTION WITH SERVER_____");
//        Thread.sleep(2000);
//        zobla.sendMessage("END");
//
//        // server close connection with guster
//        System.out.println("SERVER CLOSE CONNECTION WITH GUSTER_____");
//        Thread.sleep(2000);
//        server1.sendMessage(server1.getClients().get(2), "END");
//
//        // guster connect to server
//        System.out.println("GUSTER CONNECT TO SERVER_____");
//        Thread.sleep(2000);
//        guster.connectToServer("localhost", 5000);
//
//        // server send messages to zobla (disconnected) and guster (connected)
//        System.out.println("SERVER SENDING MESSAGES TO ZOBLA - disc and GUSTER - conn_____");
//        Thread.sleep(2000);
//        server1.sendMessage(server1.getClients().get(2), "from server to disconnected zobla");
//        server1.sendMessage(server1.getClients().get(4), "from server to connected guster");
//
//        // zobla write to server
//        System.out.println("ZOBLA DISCONNECTED WRITE TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.sendMessage(zobla.getName() + ": disconnected by client");
//
//        // guster write after connect
//        System.out.println("GUSTER WRITE AFTER GETTING BACK_____");
//        Thread.sleep(2000);
//        guster.sendMessage(guster.getName() + ": first messsage after getting back");
//
//        // zobla connect to server
//        System.out.println("ZOBLA CONNECTING TO SERVER_____");
//        Thread.sleep(2000);
//        zobla.connectToServer("localhost", 5000);
//
//        // zobla write to server
//        System.out.println("ZOBLA WRITE TO SERVER AFTER GETTING BACK_____");
//        Thread.sleep(2000);
//        zobla.sendMessage(zobla.getName() + " FINALLY BACK");

        // TODO: escape '\n' like things when sending messages as writer stays open

    }
}
