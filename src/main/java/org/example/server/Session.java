package org.example.server;

import com.google.gson.Gson;
import org.example.Constants;
import org.example.model.IdentifyMessage;
import org.example.model.Type;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Session {

    private Socket socket;

    public Session(Socket socket){
        this.socket = socket;
    }

    public void runSession(){
        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    socket.getInputStream().read(buffer);
                    String recibido = new String(buffer, StandardCharsets.UTF_8).trim();
                    System.out.println(recibido);

                    //Deserializar el tipo
                    Gson gson = new Gson();
                    Type type = gson.fromJson(recibido, Type.class);

                    switch (type.getType()){
                        case "broadcast":
                            Server.sendBroadcast(recibido);
                            break;
                        case "autoid":
                            //Anunciarlo en tod0 chat
                            IdentifyMessage user = gson.fromJson(recibido, IdentifyMessage.class);
                            Server.sendBroadcast("Se ha conectado " + user.getItsme());
                            //Registrar en hashmap
                            //Miercoles
                            break;
                        case "direct":
                            break;
                    }


                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("El otro se desconectó");
        }).start();
    }

    public Socket getSocket() {
        return socket;
    }
}
