package services;

import constants.ResponseTypes;
import daos.ChatRoomDAO;
import daos.ClientDAO;
import models.CurrentServer;
import org.json.simple.JSONObject;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientServices {
    private ClientDAO clientDAO;
    private ChatRoomDAO chatroomDAO;
    private PrintWriter out;
    private JSONObject outputData;
    private JSONObject returnData;
    private ChatRoomServices chatroomServices;
    private CurrentServer currentServer;
    private CoordinationServices coordinationServices;
    private Utils utils;

    private static ClientServices instance;

    private ClientServices(){
        clientDAO = ClientDAO.getInstance();
        chatroomDAO = ChatRoomDAO.getInstance();
        chatroomServices = ChatRoomServices.getInstance();
        currentServer = CurrentServer.getInstance();
        coordinationServices = CoordinationServices.getInstance();
        utils = Utils.getInstance();
    }

    public static ClientServices getInstance(){
        if (instance == null){
            synchronized(ClientServices.class){
                if (instance == null){
                    instance = new ClientServices();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public JSONObject registerClient(JSONObject data, Socket socket){

        String identity = data.get("identity").toString();

        try {
            if (!utils.isValidIdentity(identity) || clientDAO.isRegistered(identity)){
                returnData = new JSONObject();
                returnData.put("type", ResponseTypes.NEW_IDENTITY);
                returnData.put("approved", "false");

                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(returnData);
            } else if(coordinationServices.isClientRegistered(identity)) {
                returnData = new JSONObject();
                returnData.put("type", ResponseTypes.NEW_IDENTITY);
                returnData.put("approved", "false");

                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(returnData);
            }

            clientDAO.addNewClient(identity, socket);

            returnData = new JSONObject();
            returnData.put("type", ResponseTypes.NEW_IDENTITY);
            returnData.put("approved", "true");

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(returnData);
//            out.close();

            chatroomDAO.addParticipantDefault(identity);

            outputData = new JSONObject();
            outputData.put("type", ResponseTypes.ROOM_CHANGE);
            outputData.put("identity", identity);
            outputData.put("former", "");
            outputData.put("roomid", currentServer.getMainHallId());
            // broadcast message
            chatroomServices.broadcast(currentServer.getMainHallId(), outputData);

            System.out.println("ClientService.registerClient done...");
            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removeClient(Socket socket){
        String roomid = clientDAO.getClient(socket).getRoomid();
        String identity = clientDAO.getIdentity(socket);

        if (roomid == null || identity == null){
            return false;
        }


        // if the client is owner of a chatroom
        if (chatroomDAO.isOwner(identity, roomid)) {
            // delete room
            ArrayList<String> participants = chatroomDAO.getParticipants(roomid);
            String mainHallId = currentServer.getMainHallId();

            participants.forEach((i) -> {
                clientDAO.joinChatroom(mainHallId, i);
                chatroomDAO.changeChatroom(identity, roomid, mainHallId);

                // broadcast to previous room
                JSONObject broadcastMessage = new JSONObject();
                broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
                broadcastMessage.put("identity", i);
                broadcastMessage.put("former", "");
                broadcastMessage.put("roomid", mainHallId);
                chatroomServices.broadcast(roomid, broadcastMessage);

                // broadcast to mainhall
                chatroomServices.broadcast(mainHallId, broadcastMessage);
            });

            // move all participants to the MainHall
            participants.forEach((i) -> {
                // move client to the mainHall
                clientDAO.joinChatroom(mainHallId, i);
                chatroomDAO.changeChatroom(identity, roomid, mainHallId);

                // broadcast to previous room
                JSONObject broadcastMessage = new JSONObject();
                broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
                broadcastMessage.put("identity", i);
                broadcastMessage.put("former", "");
                broadcastMessage.put("roomid", mainHallId);
                chatroomServices.broadcast(roomid, broadcastMessage);

                // broadcast to mainhall
                chatroomServices.broadcast(mainHallId, broadcastMessage);
            });

            // delete chatroom
            chatroomDAO.deleteChatroom(roomid);
            // inform other servers
//            CommunicationService.informChatroomDeletion(roomid); //todo
        } else {
            // leave chatroom
            chatroomDAO.removeParticipant(roomid, identity);

            // broadcast to previous room
            JSONObject broadcastMessage = new JSONObject();
            broadcastMessage.put("type", ResponseTypes.ROOM_CHANGE);
            broadcastMessage.put("identity", identity);
            broadcastMessage.put("former", roomid);
            broadcastMessage.put("roomid", "");
            chatroomServices.broadcast(roomid, broadcastMessage);
        }
        // remove from client list
        clientDAO.removeClient(socket);

        // inform other servers
//        await CommunicationService.informClientDeletion(identity); //todo


        // delete connection
        JSONObject message = new JSONObject();
        message.put("type", ResponseTypes.ROOM_CHANGE);
        message.put("identity", identity);
        message.put("former", roomid);
        message.put("roomid", "");
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ClientService.removeClient done...");
        return true;
    }

}
