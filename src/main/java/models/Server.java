//package models;
//
//import handlers.ClientThreadHandler;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//
//public class Server {
//    private ServerSocket clientSocket;
//
//    public void start(int port) {
//        try {
//            clientSocket = new ServerSocket(port);
//            System.out.println("******* Client connection started on " + String.valueOf(clientSocket.getLocalSocketAddress()) + " *******");
//
//            while (true) {
//                new ClientThreadHandler(clientSocket.accept()).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stop(){
//        try {
//            clientSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
