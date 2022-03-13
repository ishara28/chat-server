package daos;

import models.CurrentServer;
import pojos.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ServerDAO {

    private String leaderid;
    private HashMap<String, Server> serverMap;
    private CurrentServer currentServer;

    private static ServerDAO instance;

    private ServerDAO(){
        leaderid = "s3"; //todo: implement leader election
        serverMap = new HashMap<>();
        currentServer = CurrentServer.getInstance();
    }

    public static ServerDAO getInstance(){
        if (instance == null){
            synchronized(ServerDAO.class){
                if (instance == null){
                    instance = new ServerDAO();//instance will be created at request time
                }
            }
        }
        return instance;
    }

    public String getLeaderid() {
        return leaderid;
    }

    public HashMap<String, Server> getServerMap() {
        return serverMap;
    }

    public void setParameters(String serverID, String serversConfPath){
        try {
            File serversConf = new File(serversConfPath);
            Scanner myReader = new Scanner(serversConf);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] params = data.split(" ");

                serverMap.put(params[0], new Server(params[0], params[1], Integer.parseInt(params[3])));

                if (params[0].equals(serverID)) {
                    currentServer.setServerID(serverID);
                    currentServer.setServerAddress(params[1]);
                    currentServer.setClientPort(Integer.parseInt(params[2]));
                    currentServer.setCoordinationPort(Integer.parseInt(params[3]));
                }


            }
            myReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
