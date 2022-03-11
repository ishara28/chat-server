package services;

import constants.ResponseTypes;
import daos.ChatRoomDAO;
import daos.ClientDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pojos.LocalChatRoom;
import pojos.LocalClient;
import utils.Utils;

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
    private Utils utils;

    private static ChatRoomServices instance;

    private ChatRoomServices(){
        chatRoomDAO = ChatRoomDAO.getInstance();
        clientDAO = ClientDAO.getInstance();
        utils = Utils.getInstance();
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

        String roomid = clientDAO.getClient(socket).getRoomid();

        if(roomid == null){
            return null;
        }

        LocalChatRoom chatroom = chatRoomDAO.getRoom(roomid);
        JSONObject message = new JSONObject();
        message.put("type", ResponseTypes.ROOM_CONTENTS);
        message.put("roomid", roomid);
        JSONArray identities = new JSONArray();
        identities.addAll(chatroom.getParticipants());
        message.put("identities", identities);
        message.put("owner", chatroom.getOwner());

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ChatroomService.listParticipants done...");
        return message;
    }

    public JSONObject createRoom(JSONObject data, Socket socket){
        String roomid = data.get("roomid").toString();
        String former = clientDAO.getClient(socket).getRoomid();
        String identity = clientDAO.getIdentity(socket);

        if(former == null || identity == null){
            return null;
        }

        //todo add if conditions

        chatRoomDAO.addNewChatroom(former, roomid, identity);
        clientDAO.joinChatroom(roomid, identity);

        JSONObject createRoomMessage = new JSONObject();
        createRoomMessage.put("type", ResponseTypes.CREATE_ROOM);
        createRoomMessage.put("roomid", roomid);
        createRoomMessage.put("approved", "true");

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(createRoomMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // broadcast to previous room
        JSONObject broadcastMessage = new JSONObject();
        broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
        broadcastMessage.put("identity", identity);
        broadcastMessage.put("former", former);
        broadcastMessage.put("roomid", roomid);

        broadcast(former, broadcastMessage);

        // send to client itself
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(broadcastMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ChatroomService.createRoom done...");
        return createRoomMessage;
    }

    public JSONObject joinRoom(JSONObject data, Socket socket) {
        String roomid = data.get("roomid").toString();
        String former = clientDAO.getClient(socket).getRoomid();
        String identity = clientDAO.getIdentity(socket);

        if(former == null || identity == null){
            return null;
        }

        //todo: add if to check validity

        clientDAO.joinChatroom(roomid, identity);
        chatRoomDAO.changeChatroom(identity, former, roomid);

        // broadcast to previous room
        JSONObject broadcastMessage = new JSONObject();
        broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
        broadcastMessage.put("identity", identity);
        broadcastMessage.put("former", former);
        broadcastMessage.put("roomid", roomid);

        broadcast(former, broadcastMessage);

        // broadcast to new room
        broadcast(roomid, broadcastMessage);

        return broadcastMessage;
    }

    public JSONObject message(JSONObject data, Socket socket){
        String content = data.get("content").toString();
        String roomid = clientDAO.getClient(socket).getRoomid();
        String identity = clientDAO.getIdentity(socket);

        if(data == null || roomid == null || identity == null){
            return null;
        }

        // broadcast to all members in chatroom
        JSONObject message = new JSONObject();
        message.put("type", ResponseTypes.MESSAGE);
        message.put("identity", identity);
        message.put("content", content);

        broadcastExceptSender(roomid, message, identity);
        return message;
    }

    public void broadcastExceptSender(String roomid, JSONObject message, String senderId){
        ArrayList<String> participants = (ArrayList<String>) chatRoomDAO.getParticipants(roomid).clone();
        participants.remove(senderId);

        // get sockets
        ArrayList<LocalClient> clients = clientDAO.getClientsFromId(participants);

        // broadcast
        clients.forEach((i) -> {
            try {
                out = new PrintWriter(i.getSocket().getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.println(message);
        });

        System.out.println("ChatroomService.broadcast done...");
    }

    public JSONObject deleteRoom(JSONObject data, Socket socket){
        String roomid = data.get("roomid").toString();
        String identity = clientDAO.getIdentity(socket);
        if (identity == null){
            return null;
        }

        //todo: has to check validity
        //todo: has check owner of the room: only owner can delete room
        ArrayList<String> participants = (ArrayList<String>) chatRoomDAO.getParticipants(roomid).clone();
        String mainHallId = utils.getMainHallId();
        // move all participants to the MainHall
        participants.forEach((i) -> {
            // move client to the mainHall
            clientDAO.joinChatroom(mainHallId, i);
            chatRoomDAO.changeChatroom(i, roomid, mainHallId);
            // broadcast to previous room
            JSONObject broadcastMessage = new JSONObject();
            broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
            broadcastMessage.put("identity", i);
            broadcastMessage.put("former", roomid);
            broadcastMessage.put("roomid", mainHallId);
            broadcast(roomid, broadcastMessage);
            // broadcast to mainhall
            broadcast(mainHallId, broadcastMessage);
        });
        // delete chatroom
        chatRoomDAO.deleteChatroom(roomid);

        JSONObject returnMessage = new JSONObject();
        returnMessage.put("type", ResponseTypes.DELETE_ROOM);
        returnMessage.put("roomid", roomid);
        returnMessage.put("approved", "true");

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println(returnMessage);

        return returnMessage;
    }
}
