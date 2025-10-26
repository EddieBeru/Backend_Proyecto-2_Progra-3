package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static private final ConcurrentHashMap<String,ClienteHandler> clientes = new ConcurrentHashMap<>();

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
            ClienteHandler cliente = new ClienteHandler(socket);
            Thread clientThread = new Thread(cliente);
            String connId = addClient(cliente);
            System.out.println("Nuevo cliente conectado: " + connId);
            clientThread.start();
        } catch (Exception e){
            System.err.println("Error al inicializar el cliente: " + e.getMessage());
        }
    }

    public static void addClient(String userId, ClienteHandler handler) {
        clientes.put(userId, handler);
    }

    public static String addClient(ClienteHandler handler) {
        String connId = UUID.randomUUID().toString();
        clientes.put(connId, handler);
        handler.setId(connId);
        return connId;
    }

    public static void registerUser(String userId, ClienteHandler handler) {
        String existingKey = null;
        for (Map.Entry<String, ClienteHandler> e : clientes.entrySet()) {
            if (e.getValue() == handler) {
                existingKey = e.getKey();
                break;
            }
        }
        if (existingKey != null) {
            clientes.remove(existingKey);
        }
        clientes.put(userId, handler);
        handler.setId(userId);
    }


    public static void removeClient(String userId) {
        clientes.remove(userId);
    }

    public static ClienteHandler getClient(String userId) {
        return clientes.get(userId);
    }

    public static Collection<ClienteHandler> getAllClients() {
        return clientes.values();
    }
}
