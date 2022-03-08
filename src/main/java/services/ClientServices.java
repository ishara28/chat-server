package services;

import constants.ResponseTypes;
import daos.ChatroomDAO;
import daos.ClientDAO;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientServices {
    private ClientDAO clientDAO;
    private ChatroomDAO chatroomDAO;
    private PrintWriter out;
    private JSONObject outputData;
    private JSONObject returnData;
    private ChatroomServices chatroomServices;

    private static ClientServices instance;

    private ClientServices(){
        clientDAO = ClientDAO.getInstance();
        chatroomDAO = ChatroomDAO.getInstance();
        chatroomServices = ChatroomServices.getInstance();
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

        try {
            //todo: change this later with if
            clientDAO.addNewClient(data.get("identity").toString(), socket);

            returnData = new JSONObject();
            returnData.put("type", ResponseTypes.NEW_IDENTITY);
            returnData.put("approved", "true");

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(returnData);
//            writeJSONtoSocket(sock, { type: responseTypes.NEW_IDENTITY, approved: "true" }) //todo: may be change
            chatroomDAO.addParticipantDefault(data.get("identity").toString());

            outputData = new JSONObject();
            outputData.put("type", ResponseTypes.ROOM_CHANGE);
            outputData.put("identity", data.get("identity").toString());
            outputData.put("former", "");
            outputData.put("roomid", "main1"); // todo: "getMainHallId()"
            // broadcast message
//            chatroomService.broadcast(getMainHallId(), outputData); //todo
            chatroomServices.broadcast("main1", outputData);

            System.out.println("ClientService.registerClient done...");
            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    static async removeClient(sock: Socket, forced: boolean): Promise<boolean> {
//        const roomid = ServiceLocator.clientsDAO.getClient(sock)?.roomid;
//        const identity = ServiceLocator.clientsDAO.getIdentity(sock);
//        if (!roomid || !identity) return false;
//        // if the client is owner of a chatroom
//        if (ServiceLocator.chatroomDAO.isOwner(identity, roomid)) {
//            // delete room
//            const participants = ServiceLocator.chatroomDAO.getParticipants(roomid);
//            const mainHallId = getMainHallId();
//            // move all participants to the MainHall
//            participants.forEach((participant: string) => {
//                // move client to the mainHall
//                ServiceLocator.clientsDAO.joinChatroom(mainHallId, participant);
//                ServiceLocator.chatroomDAO.changeChatroom(identity, roomid, mainHallId);
//                // broadcast to previous room
//                ChatroomService.broadcast(roomid, { type: responseTypes.ROOM_CHANGE, identity: participant, former: "", roomid: mainHallId });
//                // broadcast to mainhall
//                ChatroomService.broadcast(mainHallId, { type: responseTypes.ROOM_CHANGE, identity: participant, former: "", roomid: mainHallId });
//            })
//            // delete chatroom
//            ServiceLocator.chatroomDAO.deleteChatroom(roomid);
//            // inform other servers
//            CommunicationService.informChatroomDeletion(roomid);
//        } else {
//            // leave chatroom
//            ServiceLocator.chatroomDAO.removeParticipant(roomid, identity);
//            // broadcast to previous room
//            ChatroomService.broadcast(roomid, { type: responseTypes.ROOM_CHANGE, identity, former: roomid, roomid: "" });
//        }
//        // remove from client list
//        ServiceLocator.clientsDAO.removeClient(sock);
//        // inform other servers
//        await CommunicationService.informClientDeletion(identity);
//        if (!forced) {
//            // delete connection
//            writeJSONtoSocket(sock, { type: responseTypes.ROOM_CHANGE, identity: identity, former: roomid, roomid: "" });
//        }
//        console.log("ClientService.removeClient done...");
//        return true;
//    }
}
