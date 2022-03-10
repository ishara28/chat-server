package handlers;

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
    JSONParser parser = new JSONParser();

    public ServerThreadHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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

                    if (".".equals(data.get("type"))) {
                        out.println("bye");
                        break;
                    }
                }

                in.close();
                out.close();
                socket.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
}

