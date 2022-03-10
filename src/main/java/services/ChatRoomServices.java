package services;

import constants.ResponseTypes;
import daos.ChatRoomDAO;
import daos.ClientDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pojos.LocalClient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomServices {
    private ChatRoomDAO chatRoomDAO;
    private PrintWriter out;
    private JSONObject returnData;
    private JSONArray roomsJsonArray;
    private ClientDAO clientDAO;

    private static ChatRoomServices instance;

    private ChatRoomServices(){
        chatRoomDAO = ChatRoomDAO.getInstance();
        clientDAO = ClientDAO.getInstance();
    }

    public static ChatRoomServices getInstance(){
        if (instance == null){
            synchronized(ChatRoomServices.class){
                if (instance == null){
                    instance = new ChatRoomServices();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void broadcast(String roomid, JSONObject message){
        ArrayList<String> participants = chatRoomDAO.getParticipants(roomid);
        ArrayList<LocalClient> clients = clientDAO.getClientsFromId(participants); //todo

        clients.forEach((i) -> {
            try {
                out = new PrintWriter(i.getSocket().getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println(message);
//            out.close();
        });

        System.out.println("ChatroomService.broadcast done...");
    }

    public JSONObject listChatrooms(Socket socket) {
        try {
            String [] rooms = chatRoomDAO.getRoomIds();

            returnData = new JSONObject();
            returnData.put("type", ResponseTypes.ROOMLIST);
            roomsJsonArray = new JSONArray();
            roomsJsonArray.addAll(Arrays.asList(rooms));
            returnData.put("rooms", roomsJsonArray);

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(returnData);

            // TODO: ask from leader. if leader is not avaialble show local chatrooms
            System.out.println("ChatroomService.listChatrooms done...");

            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject listParticipants(Socket socket){

        return null;

//        String roomid = clientDAO.getClient(socket).roomid;
//        const roomid = clientsDAO.getClient(sock)?.roomid;
//        if (!roomid) return false;
//        const chatroom = ServiceLocator.chatroomDAO.getRoom(roomid);
//        writeJSONtoSocket(sock, {
//                type: responseTypes.ROOM_CONTENTS,
//                roomid,
//                identities: Array.from(chatroom.participants),
//                owner: chatroom.owner ?? ""
//        });
//        console.log("ChatroomService.listParticipants done...");
//        return true
    }
}