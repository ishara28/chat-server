import handlers.ClientThreadHandler;
import handlers.ServerThreadHandler;
import models.Server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class Main {
    public static void main(String[] args) {
        try{
            Server server = Server.getInstance();
            server.setParameters(args[0], args[1]);

            //server coordination
            ServerSocket coordinationSocket = new ServerSocket();
            SocketAddress coordinationEndPoint = new InetSocketAddress(server.getServerAddress(), server.getCoordinationPort());
            coordinationSocket.bind(coordinationEndPoint);

            System.out.println("******* server " + server.getServerID() + " coordination connection started on " + server.getServerAddress() + ":" + server.getCoordinationPort() + " *******");

            ServerThreadHandler serverThreadHandler = new ServerThreadHandler(coordinationSocket);
            serverThreadHandler.start();

            //client connection
            ServerSocket clientSocket = new ServerSocket();
            SocketAddress clientEndPoint = new InetSocketAddress(server.getServerAddress(), server.getClientsPort());
            clientSocket.bind(clientEndPoint);

            System.out.println("******* server " + server.getServerID() + " client connection started on " + server.getServerAddress() + ":" + server.getClientsPort() + " *******");

            while (true){
                ClientThreadHandler clientThreadHandler = new ClientThreadHandler(clientSocket.accept());
                clientThreadHandler.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
