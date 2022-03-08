package services;

import constants.ResponseTypes;
import daos.ForeignChatroomsDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ChatroomServices {
    private ForeignChatroomsDAO foreignChatroomsDAO;
    private PrintWriter out;
    private JSONObject returnData;
    private JSONArray roomsJsonArray;

    private static ChatroomServices instance;

    private ChatroomServices(){
        foreignChatroomsDAO = ForeignChatroomsDAO.getInstance();
    }

    public static ChatroomServices getInstance(){
        if (instance == null){
            synchronized(ChatroomServices.class){
                if (instance == null){
                    instance = new ChatroomServices();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void broadcast(String roomid, JSONObject message){
//        const participants = ServiceLocator.chatroomDAO.getParticipants(roomid); //todo
        // get sockets
//        const clients = ServiceLocator.clientsDAO.getClientsFromId(participants) //todo
        // broeadcast
//        clients.forEach(client => {
//                writeJSONtoSocket(client.socket, message);
//        }) //todo
        System.out.println("ChatroomService.broadcast done...");
    }

    public JSONObject listChatrooms(Socket socket) {
        try {
            String [] rooms = foreignChatroomsDAO.getRoomIds();

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
}
