package handlers;

import org.json.simple.JSONObject;
import services.LeaderServices;

import java.net.Socket;

public class LeaderHandler {

    private LeaderServices leaderServices;

    private static LeaderHandler instance;

    private LeaderHandler(){
        leaderServices = LeaderServices.getInstance();
    }

    public static LeaderHandler getInstance(){
        if (instance == null){
            synchronized(LeaderHandler.class){
                if (instance == null){
                    instance = new LeaderHandler();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public JSONObject isClient(JSONObject data, Socket socket){
        return leaderServices.checkClientExists(data, socket);
    }
}
