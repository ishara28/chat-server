package daos;

import java.util.ArrayList;
import java.util.HashMap;

public class ForeignChatRoomDAO {

    HashMap<String, ArrayList<String>> foreignChatRooms = new HashMap<>();

    private static ForeignChatRoomDAO instance;

    private ForeignChatRoomDAO(){
    }

    public static ForeignChatRoomDAO getInstance(){
        if (instance == null){
            synchronized(ForeignChatRoomDAO.class){
                if (instance == null){
                    instance = new ForeignChatRoomDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public HashMap<String, ArrayList<String>> getForeignChatRooms() {
        return foreignChatRooms;
    }

    public void saveChatrooms(HashMap<String, ArrayList<String>> chatrooms) {
        chatrooms.forEach((k,v) -> {
            if(foreignChatRooms.containsKey(k)){
                foreignChatRooms.get(k).addAll(v);
            } else{
                foreignChatRooms.put(k, v);
            }
        });
    }
}
