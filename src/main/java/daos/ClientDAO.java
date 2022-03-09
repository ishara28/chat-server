package daos;

import pojos.LocalClient;
import utils.Utils;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientDAO {

    private static HashMap<String, LocalClient> localClients = new HashMap<>();
    private Utils utils;

    private static ClientDAO instance;

    private ClientDAO(){
        utils = Utils.getInstance();
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
        localClient.setRoomid(utils.getMainHallId());

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

    public LocalClient getClient(Socket socket){
//        Collection<LocalChatRoom> localClientsCollection = LocalDataStore.localChatRooms.values();
//        for (int i=0; i<localClientsCollection.size(); i++){
//            localClientsCollection.
//        }
//        String identity =
        return null;

//        const identity = _.findKey(this.clients, ['socket', socket])
//        if (!identity) return;
//        console.log("ClientsDAO.getClient", identity);
//        return this.clients[identity];
    }
}
