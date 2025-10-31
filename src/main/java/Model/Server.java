package Model;

import Model.Networking.Respuesta;
import Model.Networking.TipoRespuesta;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private ServerSocket serverSocket;
    private static final ConcurrentHashMap<String, ClienteHandler> clients = new ConcurrentHashMap<>();

    static private final ConcurrentHashMap<String,ClienteHandler> clientes = new ConcurrentHashMap<>();

    private boolean running;

    private final int port = 1928;
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

        notifyUserConnected(userId);
    }




    public static Collection<ClienteHandler> getAllClients() {
        return clientes.values();
    }

    public static void notifyUserConnected(String userId) {
        Respuesta aviso = new Respuesta(TipoRespuesta.USUARIO_CONECTADO, getClient(userId));
        for (ClienteHandler ch : clientes.values()) {
            // si el cliente tiene el mismo id, omitir
            String id = ch.getId();
            if (id != null && id.equals(userId)) continue;
            try {
                ch.send(aviso);
            } catch (Exception e) {
                // si falla enviar, remover al cliente problemático
                removeClient(ch.getId());
            }
        }
    }

    /////////////////////////////////////////////////
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor TCP iniciado en puerto " + port);
    }

    public void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClienteHandler handler = new ClienteHandler(socket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Error en accept: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {}
    }

    public static boolean addClient(String username, ClienteHandler handler) {
        return clients.putIfAbsent(username, handler) == null;
    }

    public static void removeClient(String username) {
        if (username != null) clients.remove(username);
    }

    public static ClienteHandler getClient(String username) {
        return clients.get(username);
    }

    public static void main(String[] args) throws IOException {
        int port = 5000;
        Server server = new Server(port);
        server.start();
    }


}
