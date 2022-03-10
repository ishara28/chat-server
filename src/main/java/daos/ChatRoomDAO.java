package daos;

import pojos.LocalChatRoom;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomDAO {

    public static HashMap<String, LocalChatRoom> localChatRooms = new HashMap<>();

    private static ChatRoomDAO instance;
    private Utils utils;

    private ChatRoomDAO(){
        utils = Utils.getInstance();

        LocalChatRoom mainHall = new LocalChatRoom();
        mainHall.setOwner("");
        mainHall.setParticipants(new ArrayList<>());
        localChatRooms.put(utils.getMainHallId(), mainHall);
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

        LocalChatRoom updatedChatRoom = localChatRooms.get(utils.getMainHallId());
        ArrayList<String> updatedParticipants = updatedChatRoom.getParticipants();
        updatedParticipants.add(participant);
        updatedChatRoom.setParticipants(updatedParticipants);
        localChatRooms.put(utils.getMainHallId(), updatedChatRoom);
    }

    public String [] getRoomIds(){
        String [] roomids = localChatRooms.keySet().toArray(new String[0]);
        System.out.println("ChatroomsDAO.getRoomIds");
        for (int i=0; i<roomids.length; i++){
            System.out.println(roomids[i]);
        }
        return roomids;
    }

    public ArrayList<String> getParticipants(String roomid){
        ArrayList<String> participants =  localChatRooms.get(roomid).getParticipants();
        System.out.println("participants of " + roomid);
        for (int i=0; i<participants.size(); i++){
            System.out.println(participants.get(i));
        }
        return participants;
    }
}