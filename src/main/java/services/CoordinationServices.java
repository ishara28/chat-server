package services;

import constants.ResponseTypes;
import daos.ForeignChatRoomDAO;
import daos.ForeignClientDAO;
import daos.ServerDAO;
import models.CurrentServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pojos.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoordinationServices {

    private ServerDAO serverDAO;
    private CurrentServer currentServer;
    private ForeignClientDAO foreignClientDAO;
    private ForeignChatRoomDAO foreignChatRoomDAO;
    private PrintWriter out;
    private BufferedReader in;
    JSONParser parser;

    private static CoordinationServices instance;

    private CoordinationServices(){
        serverDAO = ServerDAO.getInstance();
        currentServer = CurrentServer.getInstance();
        foreignClientDAO = ForeignClientDAO.getInstance();
        foreignChatRoomDAO = ForeignChatRoomDAO.getInstance();
        parser = new JSONParser();
    }

    public static CoordinationServices getInstance(){
        if (instance == null){
            synchronized(CoordinationServices.class){
                if (instance == null){
                    instance = new CoordinationServices();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public boolean isClientRegistered(String identity) {
        String leaderid = serverDAO.getLeaderid();
        String serverid = currentServer.getServerID();
        // check if the server is the leader before connecting
        if (leaderid.equals(serverid)) {

            // todo: check if the leader has majority
            if (foreignClientDAO.isRegistered(identity)) {
                return true;
            } else {
//                foreignClientDAO.addNewClient(serverid, identity);

                // todo: Inform other servers - check here later
                JSONObject message = new JSONObject();
                message.put("type", ResponseTypes.BROADCAST_SERVER_UPDATE);
                message.put("leaderid", serverDAO.getLeaderid());
                JSONArray clients = new JSONArray();
                clients.addAll(foreignClientDAO.getForeignClients().keySet());
                message.put("clients", clients);
                JSONArray chatrooms = new JSONArray();
                chatrooms.addAll(foreignChatRoomDAO.getForeignChatRooms().keySet());
                message.put("chatrooms", chatrooms);

                return false;
            }
        } else {
            Server leader = serverDAO.getServerMap().get(leaderid);

            try {
                // todo: check here later
                Socket socket = new Socket(leader.getServerAddress(), leader.getCoordinationPort());
//                SocketAddress coordinationEndPoint = new InetSocketAddress(leader.getServerAddress(), leader.getCoordinationPort());
//                socket.bind(coordinationEndPoint);

                JSONObject message = new JSONObject();
                message.put("type", ResponseTypes.IS_CLIENT);
                message.put("identity", identity);
                message.put("serverid", serverid);

                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);


                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputLine = in.readLine();

                out.close();
                in.close();
                socket.close();

                JSONObject data = (JSONObject) parser.parse(inputLine);
                if (data.get("acknowledged").equals("false")|| data.get("exists").equals("true")) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void saveUpdate(JSONObject data) {
        serverDAO.setLeaderid(data.get("leaderid").toString());
        updateDatabase(data);
    }

    public void updateDatabase(JSONObject data) {
        HashMap<String, ArrayList<String>> clients = new HashMap<>();
        clients.putAll((Map<? extends String, ? extends ArrayList<String>>) data.get("clients"));
        foreignClientDAO.saveClients(clients);

        HashMap<String, ArrayList<String>> chatrooms = new HashMap<>();
        chatrooms.putAll((Map<? extends String, ? extends ArrayList<String>>) data.get("chatrooms"));
        foreignChatRoomDAO.saveChatrooms(chatrooms);
    }
}
