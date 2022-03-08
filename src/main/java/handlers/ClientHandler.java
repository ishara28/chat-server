package handlers;

import org.json.simple.JSONObject;
import services.ClientServices;

import java.net.Socket;

public class ClientHandler {
    private ClientServices clientServices = ClientServices.getInstance();

    private static ClientHandler instance;

    private ClientHandler(){
    }

    public static ClientHandler getInstance(){
        if (instance == null){
            synchronized(ClientHandler.class){
                if (instance == null){
                    instance = new ClientHandler();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public JSONObject newIdentity(JSONObject data, Socket socket) {
        return clientServices.registerClient(data, socket);
    }



//    public boolean disconnect(Socket socket, boolean forced){
//        return ClientServices.removeClient(socket, forced);
//    }
}
