package org.example.client;

import com.google.gson.Gson;
import org.example.Constants;
import org.example.model.BroadcastMessage;
import org.example.model.DirectMessage;
import org.example.model.IdentifyMessage;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6000);

        new Thread(() -> {
            try {
                while (true) {
                    byte[] buffer = new byte[1024];
                    socket.getInputStream().read(buffer);
                    String recibido = new String(buffer, StandardCharsets.UTF_8).trim();
                    System.out.println(recibido);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("El otro se desconectó");
        }).start();


        Gson gson = new Gson();
        IdentifyMessage me = new IdentifyMessage("domic0620", Constants.AUTOID);
        String meJson = gson.toJson(me);
        socket.getOutputStream().write(meJson.getBytes());


        Scanner scanner = new Scanner(System.in);
        while (true) {
            String mensaje = scanner.nextLine();
            new Thread(() -> {
                try {
                    //broadcast:<MENSJAE>
                    if(mensaje.startsWith("broadcast:")){
                        BroadcastMessage message = new BroadcastMessage(
                                Constants.BROADCAST,
                                "domic0620",
                                mensaje.substring(10)
                        );
                        String json = gson.toJson(message);
                        socket.getOutputStream().write(json.getBytes());
                    }else if(mensaje.startsWith("direct/")){
                        //direct/<USEERNAME>:<MENSAJE> --> / -> :
                        String aux = mensaje.replace("/", ":");
                        //direct:<User>:<Mensaje>
                        String username = aux.split(":")[1]; // [direct, <User>, <Mensaje>]
                        //enviar mensaje directo
                        DirectMessage directMessage = new DirectMessage(
                                Constants.DIRECT,
                                me.getItsme(),
                                aux.split(":")[2],
                                username
                        );
                        socket.getOutputStream().write(gson.toJson(directMessage).getBytes());
                        System.out.println("DEBUB> "+gson.toJson(directMessage));
                    }

                    //direct/<USEERNAME>:<MENSAJE>


                    //Serializar:

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

}
