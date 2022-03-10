import handlers.ClientThreadHandler;
import handlers.ServerThreadHandler;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class Main {
    public static void main(String[] args) {
        try{
            //server coordination
            ServerSocket coordinationSocket = new ServerSocket();
            SocketAddress coordinationEndPoint = new InetSocketAddress("0.0.0.0", 4444);
            coordinationSocket.bind(coordinationEndPoint);

            System.out.println("******* server coordination connection started on " + String.valueOf(coordinationSocket.getLocalSocketAddress()) + " *******");

            ServerThreadHandler serverThreadHandler = new ServerThreadHandler(coordinationSocket);
            serverThreadHandler.start();

            //client connection
            ServerSocket clientSocket = new ServerSocket();
            SocketAddress clientEndPoint = new InetSocketAddress("0.0.0.0", 5555);
            clientSocket.bind(clientEndPoint);

            System.out.println("******* client connection started on " + String.valueOf(clientSocket.getLocalSocketAddress()) + " *******");

            while (true){
                ClientThreadHandler clientThreadHandler = new ClientThreadHandler(clientSocket.accept());
                clientThreadHandler.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
