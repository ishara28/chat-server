package models;

public class CurrentServer {
    private String serverID;
    private String serverAddress = null;
    private int clientPort;
    private int coordinationPort;

    private static CurrentServer instance;

    private CurrentServer(){
    }

    public static CurrentServer getInstance(){
        if (instance == null){
            synchronized(CurrentServer.class){
                if (instance == null){
                    instance = new CurrentServer();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public int getCoordinationPort() {
        return coordinationPort;
    }

    public void setCoordinationPort(int coordinationPort) {
        this.coordinationPort = coordinationPort;
    }

    public String getMainHallId(){
        return "MainHall-"+serverID;
    }
}
