package handlers;

import constants.ResponseTypes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThreadHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private JSONObject data;
    JSONParser parser = new JSONParser();
    ClientHandler clientHandler = ClientHandler.getInstance();
    ChatroomHandler chatroomHandler = ChatroomHandler.getInstance();

    public ClientThreadHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data = (JSONObject) parser.parse(inputLine);

                if (ResponseTypes.NEW_IDENTITY.equals(data.get("type"))) {
                    System.out.println(clientHandler.newIdentity(data, clientSocket));
                } else if (ResponseTypes.LIST.equals(data.get("type"))){
                    System.out.println(chatroomHandler.list(clientSocket));
                } else if (ResponseTypes.WHO.equals(data.get("type"))){
                    out.println(ResponseTypes.WHO);
                } else if (ResponseTypes.CREATE_ROOM.equals(data.get("type"))){
                    out.println(ResponseTypes.CREATE_ROOM);
                } else if (ResponseTypes.JOIN_ROOM.equals(data.get("type"))){
                    out.println(ResponseTypes.JOIN_ROOM);
                } else if (ResponseTypes.MOVE_JOIN.equals(data.get("type"))){
                    out.println(ResponseTypes.MOVE_JOIN);
                } else if (ResponseTypes.DELETE_ROOM.equals(data.get("type"))){
                    out.println(ResponseTypes.DELETE_ROOM);
                } else if (ResponseTypes.MESSAGE.equals(data.get("type"))){
                    out.println(ResponseTypes.MESSAGE);
                } else if (ResponseTypes.QUIT.equals(data.get("type"))){
                    out.println(ResponseTypes.QUIT);
                    break;
                }

                if (".".equals(data.get("type"))) {
                    out.println("bye");
                    break;
                }
//                out.println(data.get("type"));
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
