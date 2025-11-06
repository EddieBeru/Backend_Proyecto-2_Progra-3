package Model;

import Model.Networking.*;
import Model.Service.ChatService;
import Model.Service.MedicamentoService;
import Model.Service.RecetaService;
import Model.Service.UsuarioService;

import java.io.*;
import java.net.Socket;

public class ClienteHandler implements Runnable {
    private boolean running = true;

    private final Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    UsuarioRed usuarioRed;
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
        Respuesta resp = switch (soli) {
            case CERRAR -> {
                running = false;
                yield new Respuesta(TipoRespuesta.OK, "Conexion cerrada");
            }
            case LOGIN -> {
                // Realizar login y si es OK registrar al usuarioRed en el Server
                String[] cred = (String[]) datos;
                Respuesta r = usuarioService.login(cred[0], cred[1]);
                if (r.getEstado() == TipoRespuesta.OK && r.getResultado() instanceof UsuarioRed u) {
                    String uid = u.getId();
                    setId(uid);
                    Server.registerUser(uid, this);
                }
                yield r;
            }
            case REGISTRO -> usuarioService.register((UsuarioRed) datos);
            case GET_USUARIOS -> usuarioService.getUsuarios();
            case GET_USUARIOS_ACTIVOS -> usuarioService.getUsuariosActivos();
            case CAMBIO_CLAVE -> {
                String[] info = (String[]) datos;
                String id = info[0];
                String viejaClave = info[1];
                String nuevaClave = info[2];
                yield usuarioService.cambiarClave(id, viejaClave, nuevaClave);
            }
            case MENSAJE -> chatService.procesarMensaje(this, ((String[]) datos)[0], ((String[]) datos)[1]);
            case MENSAJE_TODOS -> chatService.procesarMensajeATodos(this, (String) datos);
            case EDITAR -> usuarioService.editarUsuario((UsuarioRed) datos);
            case ELIMINAR -> usuarioService.eliminarUsuario((String) datos);
            case GET_MEDICAMENTOS -> medService.getMedicamentos();
            case ADD_MEDICAMENTO -> medService.addMedicamento((Medicamento) datos);
            case EDIT_MEDICAMENTO -> medService.updateMedicamento((Medicamento) datos);
            case DELETE_MEDICAMENTO -> medService.removeMedicamento((String) datos);
            case GET_RECETAS -> null;
            case ADD_RECETA -> null;
            case EDIT_RECETA -> null;
            case DELETE_RECETA -> null;
        };
        System.out.println("Solicitud " + soli.toString() + " recibida de " + socket.getRemoteSocketAddress());
        System.out.println("Repondido con " + resp.getEstado().toString() + " a " + socket.getRemoteSocketAddress());
        return resp;
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
