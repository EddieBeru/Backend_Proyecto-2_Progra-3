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

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    //Servicios
    ChatService chatService = new ChatService();
    MedicamentoService medService = new MedicamentoService();
    RecetaService recetaService = new RecetaService();
    UsuarioService usuarioService = new UsuarioService();


    public ClienteHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try{
            while (running){
                Solicitud soli = (Solicitud) in.readObject();
                Respuesta resp = procesarSolicitud(soli);
                out.writeObject(resp);
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error en ClienteHandler: " + e.getMessage());
        }
    }

    private Respuesta procesarSolicitud(Solicitud sol){
        TipoSolicitud soli = sol.getAccion();
        Object datos = sol.getDatos();
        return switch (soli) {
            case LOGIN -> usuarioService.login(((String[]) datos)[0], ((String[]) datos)[1]);
            case REGISTRO -> usuarioService.register((Usuario) datos);
            case GET_USUARIOS -> usuarioService.getUsuarios();
            default -> new Respuesta(TipoRespuesta.ACCION_DESCONOCIDA, "Acción no reconocida");
        };
    }
}
