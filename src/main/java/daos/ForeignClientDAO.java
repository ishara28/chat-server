package daos;

import java.util.ArrayList;
import java.util.HashMap;

public class ForeignClientDAO {
    HashMap<String, ArrayList<String>> foreignClients = new HashMap<>();

    private static ForeignClientDAO instance;

    private ForeignClientDAO(){
    }

    public static ForeignClientDAO getInstance(){
        if (instance == null){
            synchronized(ForeignClientDAO.class){
                if (instance == null){
                    instance = new ForeignClientDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public boolean isRegistered(String identity) {

        final boolean[] isRegistered = {false};

        foreignClients.forEach((k,v) -> {
            v.forEach((i) -> {
                if (i.equals(identity)) {
                    isRegistered[0] = true;
                }
            });
        });

        System.out.println("ForeignClientsDAO.isRegistered " + identity + " " + isRegistered[0]);
        return isRegistered[0];
    }

    public void addNewClient(String serverid, String identity) {
        if(!foreignClients.containsKey(serverid)){
            foreignClients.put(serverid, new ArrayList<>());
        }
        foreignClients.get(serverid).add(identity);
        System.out.println("ForeignClientsDAO.addNewClient " + serverid + " " + identity);
    }

    public HashMap<String, ArrayList<String>> getForeignClients() {
        return foreignClients;
    }
}
