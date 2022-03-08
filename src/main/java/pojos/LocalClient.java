package pojos;

import java.net.Socket;

public class LocalClient {
//    private Socket socket;
    private String identity;
    private String roomid;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
