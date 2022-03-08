package daos;

import database.LocalDataStore;
import pojos.LocalChatRoom;
import utils.Utils;

import java.util.ArrayList;

public class ChatRoomDAO {

    private static ChatRoomDAO instance;
    private Utils utils;

    private ChatRoomDAO(){
        utils = Utils.getInstance();

        LocalChatRoom mainHall = new LocalChatRoom();
        mainHall.setOwner("");
        mainHall.setParticipants(new ArrayList<>());
        LocalDataStore.localChatRooms.put(utils.getMainHallId(), mainHall);
    }

    public static ChatRoomDAO getInstance(){
        if (instance == null){
            synchronized(ChatRoomDAO.class){
                if (instance == null){
                    instance = new ChatRoomDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void addParticipantDefault(String participant){
        System.out.println("ChatroomDAO.addParticipantDefault " + participant + " to " + utils.getMainHallId());
        LocalDataStore.localChatRooms.get(utils.getMainHallId()).getParticipants().add(participant);
    }

    public String [] getRoomIds(){
        String [] roomids = LocalDataStore.localChatRooms.keySet().toArray(new String[0]);
        System.out.println("ChatroomsDAO.getRoomIds");
        for (int i=0; i<roomids.length; i++){
            System.out.println(roomids[i]);
        }
        return roomids;
    }
}
