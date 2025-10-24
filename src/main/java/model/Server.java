package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Thread> clientes;

    private boolean running;

    private final int port = 1928;
    ServerSocket serverSocket;

    public Server(){
        //Intenta conectarse al DB
        try {
            ConexionDB.getConexion();
            System.out.println("Conexión a la base de datos exitosa.");
        } catch (Exception e){
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        //Inicializar los atributos
        try {
            this.clientes = new ArrayList<>();
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            return;
        }

        running = true;
        new Thread(this::listenClients).start();
    }

    private void listenClients() {
        System.out.println("Proxy escuchando en el puerto " + port);
        while(running){
            try {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress().getHostName());
                new Thread(() -> inicializarCliente(socket)).start();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    private void inicializarCliente(Socket socket){
        try {
            Thread clientThread = new Thread(new ClienteHandler(socket));
            clientes.add(clientThread);
            clientThread.start();
        } catch (Exception e){
            System.err.println("Error al inicializar el cliente: " + e.getMessage());
        }
    }
}
