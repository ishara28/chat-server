package handlers;

import daos.ChatroomDAO;
import daos.ClientDAO;
import org.json.simple.JSONObject;
import services.ChatroomServices;
import services.ClientServices;

import java.net.Socket;

public class ChatroomHandler {
    private ChatroomServices chatroomServices;

    private static ChatroomHandler instance;

    private ChatroomHandler(){
        chatroomServices = ChatroomServices.getInstance();
    }

    public static ChatroomHandler getInstance(){
        if (instance == null){
            synchronized(ChatroomHandler.class){
                if (instance == null){
                    instance = new ChatroomHandler();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public JSONObject list(Socket sock) {
        return chatroomServices.listChatrooms(sock);
    }
}
