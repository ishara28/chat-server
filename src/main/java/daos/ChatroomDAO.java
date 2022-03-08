package daos;

public class ChatroomDAO {

    private static ChatroomDAO instance;

    private ChatroomDAO(){}

    public static ChatroomDAO getInstance(){
        if (instance == null){
            synchronized(ChatroomDAO.class){
                if (instance == null){
                    instance = new ChatroomDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public void addParticipantDefault(String participant){
//        System.out.println("ChatroomDAO.addParticipantDefault " + participant, " to ", getMainHallId()); //todo
        System.out.println("ChatroomDAO.addParticipantDefault " + participant + " to Mainhall");
//        this.chatrooms[getMainHallId()].participants.add(participant); todo: add database array
    }
}
