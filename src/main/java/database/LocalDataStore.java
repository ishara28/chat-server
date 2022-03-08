package database;

import pojos.LocalChatRoom;
import pojos.LocalClient;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalDataStore {

    private LocalDataStore(){}

    public static HashMap<String, LocalClient> localClients = new HashMap<>();
    public static HashMap<String, LocalChatRoom> localChatRooms = new HashMap<>();

}
