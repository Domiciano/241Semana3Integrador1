package org.example.server;

import com.google.gson.Gson;
import org.example.Constants;
import org.example.model.BroadcastMessage;
import org.example.model.DirectMessage;
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
                            BroadcastMessage broadcastMessage = gson.fromJson(recibido, BroadcastMessage.class);
                            Server.sendBroadcast(broadcastMessage.getAuthor()+" dice: \n"+broadcastMessage.getMessage());
                            break;
                        case "autoid":
                            //Anunciarlo en tod0 chat
                            IdentifyMessage user = gson.fromJson(recibido, IdentifyMessage.class);
                            Server.sendBroadcast("Se ha conectado " + user.getItsme());
                            //Registrar en hashmap
                            Server.users.put(user.getItsme(), this);
                            //Miercoles

                            break;
                        case "direct":
                            //{"to":"andres", "type":"direct", "message":"Hola", "author":"domi062'"}
                            DirectMessage direct = gson.fromJson(recibido, DirectMessage.class);
                            Session s = Server.users.get(direct.getTo());
                            if(s != null) {
                                String finalMessage = direct.getAuthor() + " te dijo directamente: " + direct.getMessage();
                                s.getSocket().getOutputStream().write(
                                        finalMessage.getBytes()
                                );
                            }else{
                                //Podriamos decirle al usuario author que el to no existe
                                socket.getOutputStream().write(
                                        ("El usuario "+direct.getTo()+" no esta conectado").getBytes()
                                );
                            }

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
