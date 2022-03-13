package daos;

import models.CurrentServer;
import pojos.LocalClient;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientDAO {

    private static HashMap<String, LocalClient> localClients = new HashMap<>();
    private CurrentServer currentServer;

    private static ClientDAO instance;

    private ClientDAO(){
        currentServer = CurrentServer.getInstance();
    }

    public static ClientDAO getInstance(){
        if (instance == null){
            synchronized(ClientDAO.class){
                if (instance == null){
                    instance = new ClientDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void addNewClient(String identity, Socket socket){
        LocalClient localClient = new LocalClient();
        localClient.setSocket(socket);
        localClient.setRoomid(currentServer.getMainHallId());

        localClients.put(identity, localClient);

        System.out.println("ClientsDAO.addNewClient " + identity);
    }

    public ArrayList<LocalClient> getClientsFromId(ArrayList<String> ids) {
        ArrayList<LocalClient> roomParticipants = new ArrayList<>();
        for (int i=0; i<ids.size(); i++){
            roomParticipants.add(localClients.get(ids.get(i)));
        }
        System.out.println("ClientsDAO.getClientsFromId");
        return roomParticipants;
    }

    public String getIdentity(Socket socket){
        final String[] identity = {null};

        localClients.forEach((key, value) -> {
            if ( value.getSocket() == socket){
                identity[0] = key;
            }
        });

        return identity[0];
    }

    public void joinChatroom(String newRoomId, String identity){
        LocalClient localClient = localClients.get(identity);
        localClient.setRoomid(newRoomId);
        localClients.put(identity, localClient);
        System.out.println("ClientsDAO.joinChatroom " + identity);
    }

    public boolean removeClient(Socket socket){
        final String[] identity = {null};
        localClients.forEach((key, value) -> {
            if ( value.getSocket() == socket){
                identity[0] = key;
            }
        });

        if (identity[0] == null){
            return false;
        }

        System.out.println("ClientsDAO.deleteClient " + identity[0]);
        if (localClients.remove(identity[0]) != null){
            return true;
        };

        return false;
    }

    public LocalClient getClient(Socket socket){ //todo
        final String[] identity = {null};
        localClients.forEach((key, value) -> {
            if ( value.getSocket() == socket){
                identity[0] = key;
            }
        });

        System.out.println("ClientsDAO.getClient" + identity[0]);

        return localClients.get(identity[0]);
    }

    public boolean isRegistered(String identity) {
        boolean isRegistered = localClients.containsKey(identity);
        System.out.println("ClientsDAO.isRegistered " + identity + " " + isRegistered);
        return isRegistered;
    }
}
