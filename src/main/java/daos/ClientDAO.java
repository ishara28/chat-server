package daos;

import database.LocalDataStore;
import pojos.LocalClient;

import java.net.Socket;
import java.util.HashMap;

public class ClientDAO {

    private static ClientDAO instance;

    private ClientDAO(){}

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
        //todo: setup database here

        LocalClient localClient = new LocalClient();
        localClient.setIdentity(identity);
        localClient.setRoomid("MainHall-s1"); //todo: add a find room id function

        LocalDataStore.localClients.put("identity", localClient);

        System.out.println("ClientsDAO.addNewClient " + identity);
    }

//    public LocalClient getClient(Socket socket){
//        const identity = _.findKey(this.clients, ['socket', socket])
//        if (!identity) return;
//        console.log("ClientsDAO.getClient", identity);
//        return this.clients[identity];
//    }
}
