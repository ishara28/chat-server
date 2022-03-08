package daos;

import java.net.Socket;

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

    public void addNewClient(String identity, Socket sock){
        //todo: setup database here
//        this.clients[identity] = { socket: sock, roomid: getMainHallId() };
        System.out.println("ClientsDAO.addNewClient " + identity);
    }
}
