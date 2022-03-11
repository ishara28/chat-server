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
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JSONObject data;
    JSONParser parser = new JSONParser();
    ClientHandler clientHandler = ClientHandler.getInstance();
    ChatroomHandler chatroomHandler = ChatroomHandler.getInstance();

    public ClientThreadHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data = (JSONObject) parser.parse(inputLine);

                if (ResponseTypes.NEW_IDENTITY.equals(data.get("type"))) { //done
                    System.out.println(clientHandler.newIdentity(data, socket));
                } else if (ResponseTypes.LIST.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.list(socket));
                } else if (ResponseTypes.WHO.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.who(socket));
                } else if (ResponseTypes.CREATE_ROOM.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.createRoom(data, socket));
                } else if (ResponseTypes.JOIN_ROOM.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.joinRoom(data, socket));
                } else if (ResponseTypes.DELETE_ROOM.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.deleteRoom(data, socket));
                } else if (ResponseTypes.MESSAGE.equals(data.get("type"))){ //done
                    System.out.println(chatroomHandler.message(data, socket));
                } else if (ResponseTypes.QUIT.equals(data.get("type"))){ //todo: has a error when quit from another room
                    System.out.println(clientHandler.disconnect(socket));
                }

                if (".".equals(data.get("type"))) {
                    out.println("bye");
                    break;
                }
            }

//            in.close();
//            out.close();
//            socket.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
