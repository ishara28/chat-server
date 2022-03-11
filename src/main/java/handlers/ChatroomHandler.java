package handlers;

import org.json.simple.JSONObject;
import services.ChatRoomServices;

import java.net.Socket;

public class ChatroomHandler {
    private ChatRoomServices chatroomServices;

    private static ChatroomHandler instance;

    private ChatroomHandler(){
        chatroomServices = ChatRoomServices.getInstance();
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

    public JSONObject list(Socket socket) {
        return chatroomServices.listChatrooms(socket);
    }

    public JSONObject who(Socket socket) {
        return chatroomServices.listParticipants(socket);
    }

    public JSONObject createRoom(JSONObject data, Socket socket) {
        return chatroomServices.createRoom(data, socket);
    }

    public JSONObject joinRoom(JSONObject data, Socket socket) {
        return chatroomServices.joinRoom(data, socket);
    }

    public JSONObject message(JSONObject data, Socket socket){
        return chatroomServices.message(data, socket);
    }
}
