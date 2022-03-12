package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Server {
    private String serverID;
    private String serverAddress = null;
    private int clientsPort;
    private int coordinationPort;

    private static Server instance;

    private Server(){
    }

    public static Server getInstance(){
        if (instance == null){
            synchronized(Server.class){
                if (instance == null){
                    instance = new Server();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public String getServerID() {
        return serverID;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getClientsPort() {
        return clientsPort;
    }

    public int getCoordinationPort() {
        return coordinationPort;
    }

    public void setParameters(String serverID, String serversConfPath){
        this.serverID = serverID;
        try {
            File serversConf = new File(serversConfPath);
            Scanner myReader = new Scanner(serversConf);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] params = data.split(" ");
                if (params[0].equals(serverID)) {
                    this.serverAddress = params[1];
                    this.clientsPort = Integer.parseInt(params[2]);
                    this.coordinationPort = Integer.parseInt(params[3]);
                }
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getMainHallId(){
        return "MainHall-"+serverID;
    }
}
