package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {


    //Usado para broadcast
    public static ArrayList<Session> sessions = new ArrayList<>();

    public static HashMap<String, Session> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //Esperar solicitudes de handshake TCP

        new Thread(()->{
            try {
                ServerSocket server = new ServerSocket(6000);
                while (true) {
                    System.out.println("Esperando...");
                    Socket socket = server.accept();
                    System.out.println(socket.getPort());
                    System.out.println(socket.getLocalPort());
                    System.out.println("Conectado");
                    //Crear proceso de recepcion
                    Session session = new Session(socket);
                    session.runSession();
                    sessions.add(session);
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }).start();

    }

    public static void sendBroadcast(String mensaje){
        sessions.forEach(session ->{
            try {
                session.getSocket().getOutputStream().write(mensaje.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
