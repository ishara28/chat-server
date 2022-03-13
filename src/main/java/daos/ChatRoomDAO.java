package daos;

import models.CurrentServer;
import pojos.LocalChatRoom;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatRoomDAO {

    public static HashMap<String, LocalChatRoom> localChatRooms = new HashMap<>();

    private static ChatRoomDAO instance;
    private CurrentServer currentServer;

    private ChatRoomDAO(){
        currentServer = CurrentServer.getInstance();

        LocalChatRoom mainHall = new LocalChatRoom();
        mainHall.setOwner("");
        mainHall.setParticipants(new ArrayList<>());
        localChatRooms.put(currentServer.getMainHallId(), mainHall);
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
        System.out.println("ChatroomDAO.addParticipantDefault " + participant + " to " + currentServer.getMainHallId());

        LocalChatRoom updatedChatRoom = localChatRooms.get(currentServer.getMainHallId());
        ArrayList<String> updatedParticipants = updatedChatRoom.getParticipants();
        updatedParticipants.add(participant);
        updatedChatRoom.setParticipants(updatedParticipants);
        localChatRooms.put(currentServer.getMainHallId(), updatedChatRoom);
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

    public boolean isOwner(String identity, String roomid){
        boolean isOwner = localChatRooms.get(roomid).getOwner() == identity;
        System.out.println("ChatroomDAO.isOwner " + identity + " " + roomid + " " + isOwner);
        return isOwner;
    }

    public void changeChatroom(String participant, String previousRoomId, String newRoomId) {
        LocalChatRoom previousRoom = localChatRooms.get(previousRoomId);
        ArrayList<String> previousRoomParticipants = previousRoom.getParticipants();
        previousRoomParticipants.remove(participant);
        previousRoom.setParticipants(previousRoomParticipants);
        localChatRooms.put(previousRoomId, previousRoom);

        LocalChatRoom newRoom = localChatRooms.get(newRoomId);
        ArrayList<String> newRoomParticipants = newRoom.getParticipants();
        newRoomParticipants.add(participant);
        newRoom.setParticipants(newRoomParticipants);
        localChatRooms.put(newRoomId, newRoom);
    }

    public void deleteChatroom(String roomid){
        System.out.println("ChatroomDAO.deleteChatroom" + roomid);
        localChatRooms.remove(roomid);
    }

    public void removeParticipant(String roomid, String participant){
        System.out.println("ChatroomDAO.removeParticipant " + participant + " from " + roomid);
        LocalChatRoom chatRoom = localChatRooms.get(roomid);
        ArrayList<String> participants = chatRoom.getParticipants();
        participants.remove(participant);
        chatRoom.setParticipants(participants);
        localChatRooms.put(roomid, chatRoom);
    }

    public LocalChatRoom getRoom(String roomid){
        System.out.println("ChatroomDAO.getRoom" + roomid);
        return localChatRooms.get(roomid);
    }

    public void addNewChatroom(String previousRoomId, String newRoomId, String identity){
        // remove from previous chatroom
        removeParticipant(previousRoomId, identity);
        LocalChatRoom newChatRoom = new LocalChatRoom();
        newChatRoom.setOwner(identity);
        ArrayList<String> participants = new ArrayList<>();
        participants.add(identity);
        newChatRoom.setParticipants(participants);
        localChatRooms.put(newRoomId, newChatRoom);
        System.out.println("ChatroomDAO.addNewChatroom "+ identity + " from " + previousRoomId + " to " + newRoomId);
    }
}
