package model;

import model.Networking.Respuesta;
import model.Networking.Solicitud;
import model.Networking.TipoRespuesta;
import model.Networking.TipoSolicitud;
import model.Service.ChatService;
import model.Service.MedicamentoService;
import model.Service.RecetaService;
import model.Service.UsuarioService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienteHandler implements Runnable {
    private boolean running = true;

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    Usuario usuario;
    String userId;

    //Servicios
    public ChatService chatService = new ChatService();
    MedicamentoService medService = new MedicamentoService();
    RecetaService recetaService = new RecetaService();
    UsuarioService usuarioService = new UsuarioService();


    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //Inicializar streams
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error al inicializar streams en ClienteHandler: " + e.getMessage());
            return;
        }

        try {
            while (running){
                Solicitud soli = (Solicitud) in.readObject();
                Respuesta resp = procesarSolicitud(soli);
                out.writeObject(resp);
                out.flush();
            }
            close();
        } catch (Exception e) {
            System.err.println("Error en ClienteHandler: " + e.getMessage());
        }
    }

    private Respuesta procesarSolicitud(Solicitud sol){
        TipoSolicitud soli = sol.getAccion();
        Object datos = sol.getDatos();
        return switch (soli) {
            case CERRAR -> {
                running = false;
                yield new Respuesta(TipoRespuesta.OK, "Conexion cerrada");
            }
            case LOGIN -> usuarioService.login(((String[]) datos)[0], ((String[]) datos)[1]);
            case REGISTRO -> usuarioService.register((Usuario) datos);
            case GET_USUARIOS -> usuarioService.getUsuarios();
            case GET_USUARIOS_ACTIVOS -> null;
            case CAMBIO_CLAVE -> null;
            case MENSAJE -> chatService.procesarMensaje(this, ((String[]) datos)[0], ((String[]) datos)[1]);
            case MENSAJE_TODOS -> chatService.procesarMensajeATodos(this, (String) datos);
        };
    }

    public void send(Respuesta respuesta) throws Exception {
        synchronized (out) {
            out.writeObject(respuesta);
            out.flush();
        }
    }

    public void close() {
        running = false;
        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (Exception ignored) {}
        if (userId != null) Server.removeClient(userId);
    }

    public void setId(String id) {
        this.userId = id;
    }

    public String getId() {
        return this.userId;
    }
}
