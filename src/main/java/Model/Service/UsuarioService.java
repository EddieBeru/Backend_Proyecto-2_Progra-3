package Model.Service;

import Model.ClienteHandler;
import Model.DAO.UsuarioDAO;
import Model.Networking.Respuesta;
import Model.Networking.TipoRespuesta;
import Model.PasswordUtil;
import Model.Server;
import Model.UsuarioRed;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    public UsuarioService() {
    }

    public Respuesta login(String username, String contra){
        List<UsuarioRed> usuarioReds;
        try {
            usuarioReds = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarioReds: " + e.getMessage());
        }

        for(UsuarioRed u : usuarioReds){
            if (u.getId().equalsIgnoreCase(username) &&
                    PasswordUtil.verificar(contra.toCharArray(), u.getClave())){
                return new Respuesta(TipoRespuesta.OK, u);
            }
        }

        return new Respuesta(TipoRespuesta.INVALIDO, "UsuarioRed no encontrado o clave incorrecta");
    }

    public Respuesta register(UsuarioRed usuarioRed){
        List<UsuarioRed> usuarioReds = new ArrayList<>();
        try {
            usuarioReds = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Ocurrió un error");
        }

        //BUSCAR SI YA EXISTE CON ESE ID
        for (UsuarioRed u : usuarioReds){
            if (u.getId().equalsIgnoreCase(usuarioRed.getId())){
                return new Respuesta(TipoRespuesta.YA_EXISTE, "UsuarioRed ya existe");
            }
        }

        try {
            new UsuarioDAO().insert(usuarioRed);
            return new Respuesta(TipoRespuesta.OK, usuarioRed);
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Ocurrio un error");
        }
    }

    public Respuesta getUsuarios(){
        try {
            return new Respuesta(TipoRespuesta.OK,
                    new UsuarioDAO().findAll());
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarios: " + e.getMessage());
        }
    }

    public Respuesta getUsuariosActivos(){
        try {
            List<ClienteHandler> clientesActivos = Server.getAllClients().stream().toList();
            List<UsuarioRed> usuariosActivos = new ArrayList<>();
            for (ClienteHandler ch : clientesActivos) {
                String userId = ch.getId();
                if (userId != null) {
                    try {
                        UsuarioRed u = new UsuarioDAO().findById(userId);
                        if (u != null && !usuariosActivos.contains(u)) {
                            usuariosActivos.add(u);
                        }
                    } catch (Exception e) {
                        // Manejar excepción si es necesario
                    }
                }
            }
            return new Respuesta(TipoRespuesta.OK,usuariosActivos);
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarios activos: " + e.getMessage());
        }
    }

    public Respuesta cambiarClave(String id, String viejaClave, String nuevaClave) {
        List<UsuarioRed> usuarioReds;
        try {
            usuarioReds = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarioReds: " + e.getMessage());
        }

        for (UsuarioRed u : usuarioReds) {
            if (u.getId().equalsIgnoreCase(id)) {
                if (!PasswordUtil.verificar(viejaClave.toCharArray(), u.getClave())) {
                    return new Respuesta(TipoRespuesta.INVALIDO, "Contraseña antigua incorrecta");
                }
                if (viejaClave.equals(nuevaClave)) {
                    return new Respuesta(TipoRespuesta.INVALIDO, "La nueva contraseña no puede ser igual a la antigua");
                }
                if (PasswordUtil.ValidarClave(nuevaClave)){
                    return new Respuesta(TipoRespuesta.INVALIDO, "La nueva contraseña no cumple con los requisitos de seguridad");
                }
                u.setClave(nuevaClave);
                try {
                    new UsuarioDAO().update(u);
                    return new Respuesta(TipoRespuesta.OK, "Contraseña cambiada exitosamente");
                } catch (Exception e) {
                    return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                            "Error al actualizar la contraseña: " + e.getMessage());
                }
            }
        }
        return new Respuesta(TipoRespuesta.NO_EXISTE, "UsuarioRed no encontrado");
    }
}
