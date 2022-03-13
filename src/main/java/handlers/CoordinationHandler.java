package handlers;

import org.json.simple.JSONObject;
import services.CoordinationServices;

public class CoordinationHandler {

    private CoordinationServices coordinationServices;

    private static CoordinationHandler instance;

    private CoordinationHandler(){
        coordinationServices = CoordinationServices.getInstance();
    }

    public static CoordinationHandler getInstance(){
        if (instance == null){
            synchronized(CoordinationHandler.class){
                if (instance == null){
                    instance = new CoordinationHandler();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void broadcastServerUpdate(JSONObject data) {
        coordinationServices.saveUpdate(data);
    }
}
