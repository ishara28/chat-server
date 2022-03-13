import daos.ServerDAO;
import handlers.ClientThreadHandler;
import handlers.ServerThreadHandler;
import models.CurrentServer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class Main {
    public static void main(String[] args) {
        try{
            CurrentServer currentServer = CurrentServer.getInstance();
//            server.setParameters(args[0], args[1]);

            //*********************************
            // Todo: Use these lines only for debugging
            String args1 = "s3";
            String args2 = "/home/dilanka_rathnasiri/Documents/chat-server/server_conf.txt";

            ServerDAO serverDAO = ServerDAO.getInstance();
            serverDAO.setParameters(args1, args2);

            //*********************************

            //server coordination
            ServerSocket coordinationSocket = new ServerSocket();
            SocketAddress coordinationEndPoint = new InetSocketAddress(currentServer.getServerAddress(), currentServer.getCoordinationPort());
            coordinationSocket.bind(coordinationEndPoint);

            System.out.println("******* server " + currentServer.getServerID() + " coordination connection started on " + currentServer.getServerAddress() + ":" + currentServer.getCoordinationPort() + " *******");

            ServerThreadHandler serverThreadHandler = new ServerThreadHandler(coordinationSocket);
            serverThreadHandler.start();

            //client connection
            ServerSocket clientSocket = new ServerSocket();
            SocketAddress clientEndPoint = new InetSocketAddress(currentServer.getServerAddress(), currentServer.getClientPort());
            clientSocket.bind(clientEndPoint);

            System.out.println("******* server " + currentServer.getServerID() + " client connection started on " + currentServer.getServerAddress() + ":" + currentServer.getClientPort() + " *******");

            while (true){
                ClientThreadHandler clientThreadHandler = new ClientThreadHandler(clientSocket.accept());
                clientThreadHandler.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
