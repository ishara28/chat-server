package handlers;

import constants.ResponseTypes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThreadHandler extends Thread {
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private JSONObject data;
    private LeaderHandler leaderHandler;
    JSONParser parser = new JSONParser();

    public ServerThreadHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.leaderHandler = LeaderHandler.getInstance();
    }

    public void run() {
        while (true){
            try {
                Socket socket = serverSocket.accept();
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    data = (JSONObject) parser.parse(inputLine);

                    //election
                    if (ResponseTypes.START_ELECTION.equals(data.get("type"))) {
                        System.out.println();
                    } else if (ResponseTypes.DECLARE_LEADER.equals(data.get("type"))){
                        System.out.println();
                    }

                    //received by leader
                    else if (ResponseTypes.IS_CLIENT.equals(data.get("type"))){ //done
                        System.out.println(leaderHandler.isClient(data, socket));
                    } else if (ResponseTypes.IS_CHATROOM.equals(data.get("type"))){
                        System.out.println();
                    } else if (ResponseTypes.CHATROOM_SERVER.equals(data.get("type"))){
                        System.out.println();
                    } else if (ResponseTypes.INFORM_ROOMDELETION.equals(data.get("type"))){
                        System.out.println();
                    } else if (ResponseTypes.INFORM_CLIENTDELETION.equals(data.get("type"))){
                        System.out.println();
                    }

                    // received by other nodes
                    else if (ResponseTypes.REQUEST_DATA.equals(data.get("type"))){
                        System.out.println();
                    } else if (ResponseTypes.BROADCAST_SERVER_UPDATE.equals(data.get("type"))){
                        System.out.println();
                    } else if (ResponseTypes.HEARTBEAT.equals(data.get("type"))){
                        System.out.println();
                    }

                    if (".".equals(data.get("type"))) {
                        out.println("bye");
                        break;
                    }
                }

//                in.close();
//                out.close();
//                socket.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

