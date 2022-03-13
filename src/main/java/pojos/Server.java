package pojos;

public class Server {
    private String serverID;
    private String serverAddress = null;
    private int coordinationPort;

    public Server(){
    }

    public Server(String serverID, String serverAddress, int coordinationPort){
        this.serverID = serverID;
        this.serverAddress = serverAddress;
        this.coordinationPort = coordinationPort;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setCoordinationPort(int coordinationPort) {
        this.coordinationPort = coordinationPort;
    }

    public String getServerID() {
        return serverID;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getCoordinationPort() {
        return coordinationPort;
    }

}
