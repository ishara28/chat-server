package pojos;

import java.util.ArrayList;

public class LocalChatRoom {
    private String owner;
    private ArrayList<String> participants;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }
}
