package services;

import constants.ResponseTypes;
import daos.ForeignChatRoomDAO;
import daos.ForeignClientDAO;
import daos.ServerDAO;
import models.CurrentServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class LeaderServices {

    private static LeaderServices instance;
    private ForeignClientDAO foreignClientDAO;
    private ForeignChatRoomDAO foreignChatRoomDAO;
    private ServerDAO serverDAO;
    private CurrentServer currentServer;
    private JSONObject returnData;
    private PrintWriter out;

    private LeaderServices(){
        foreignClientDAO = ForeignClientDAO.getInstance();
        foreignChatRoomDAO = ForeignChatRoomDAO.getInstance();
        serverDAO = ServerDAO.getInstance();
        currentServer = CurrentServer.getInstance();
    }

    public static LeaderServices getInstance(){
        if (instance == null){
            synchronized(LeaderServices.class){
                if (instance == null){
                    instance = new LeaderServices();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public JSONObject checkClientExists(JSONObject data, Socket socket) {
        String identity = data.get("identity").toString();
        String serverid = data.get("serverid").toString();
        returnData = new JSONObject();

        // todo: check majority
        if(foreignClientDAO.isRegistered(identity)){
            returnData.put("acknowledged", "true");
            returnData.put("exists", "true");
            returnData.put("type", ResponseTypes.IS_CLIENT);
            returnData.put("identity", identity);

            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(returnData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            foreignClientDAO.addNewClient(serverid, identity);

            returnData.put("acknowledged", "true");
            returnData.put("exists", "false");
            returnData.put("type", ResponseTypes.IS_CLIENT);
            returnData.put("identity", identity);

            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(returnData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //todo: Inform other servers - check here later
            JSONObject broadcastMessage = new JSONObject();
            broadcastMessage.put("type", ResponseTypes.BROADCAST_SERVER_UPDATE);
            broadcastMessage.put("leaderid", serverDAO.getLeaderid());
            JSONArray messageClients = new JSONArray();
            messageClients.addAll(foreignClientDAO.getForeignClients().keySet());
            broadcastMessage.put("clients", messageClients);
            JSONArray messageRooms = new JSONArray();
            messageRooms.addAll(foreignChatRoomDAO.getForeignChatRooms().keySet());
            broadcastMessage.put("chatrooms", messageRooms);



            broadcastServers(broadcastMessage);
        }

        return returnData;

    }


    public void broadcastServers(JSONObject data) {
        serverDAO.getServerMap().forEach((k, v) -> {
            if(!k.equals(currentServer.getServerID())){
                String serverAddress = v.getServerAddress();
                int coordinationPort = v.getCoordinationPort();

                try {
                    Socket socket = new Socket(serverAddress, coordinationPort);

                    out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(data);

                    out .close();
                    socket.close();
                } catch (IOException e) {
                    if (e.getMessage().equals("Connection refused")){
                        System.out.println(k + " Server failed");
                    } else{
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
