package daos;

import java.util.ArrayList;

public class ForeignChatroomsDAO {

    private static ForeignChatroomsDAO instance;

    private ForeignChatroomsDAO(){}

    public static ForeignChatroomsDAO getInstance(){
        if (instance == null){
            synchronized(ForeignChatroomsDAO.class){
                if (instance == null){
                    instance = new ForeignChatroomsDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public String [] getRoomIds(){
//        const roomids = _.flatten(_.map(_.values(this.chatrooms), (set) => Array.from(set))); //todo: setup database with rooms

        String [] roomids = {"MainHall-s1", "MainHall-s2", "jokes"};
        System.out.println("ForeignChatroomsDAO.getRoomIds");
        for (int i=0; i<roomids.length; i++){
            System.out.println(roomids[i]);
        }
        return roomids;
    }
}
