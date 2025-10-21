package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Thread> clientes;

    private boolean running;

    private final int port = 1928;
    ServerSocket serverSocket = new ServerSocket(port);

    public Server() throws IOException {
        this.clientes = new ArrayList<>();
        running = true;
        new Thread(this::listenClients).start();
    }

    private void listenClients() {
        System.out.println("Proxy escuchando en el puerto " + port);
        while(running){
            try {
                serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                continue;
            }
            clientes.add(new Thread(new ClienteHandler()));
        }
    }
}
