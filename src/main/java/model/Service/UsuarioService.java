package model.Service;

import model.DAO.UsuarioDAO;
import model.Networking.Respuesta;
import model.Networking.TipoRespuesta;
import model.PasswordUtil;
import model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    public UsuarioService() {
    }

    public Respuesta login(String username, String contra){
        List<Usuario> usuarios;
        try {
            usuarios = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarios: " + e.getMessage());
        }

        for(Usuario u : usuarios){
            if (u.getId().equalsIgnoreCase(username) &&
                    PasswordUtil.verificar(contra.toCharArray(), u.getClave())){
                return new Respuesta(TipoRespuesta.OK, u);
            }
        }

        return new Respuesta(TipoRespuesta.INVALIDO, "Usuario no encontrado o clave incorrecta");
    }

    public Respuesta register(Usuario usuario){
        List<Usuario> usuarios = new ArrayList<>();
        try {
            usuarios = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Ocurrió un error");
        }

        //BUSCAR SI YA EXISTE CON ESE ID
        for (Usuario u : usuarios){
            if (u.getId().equalsIgnoreCase(usuario.getId())){
                return new Respuesta(TipoRespuesta.YA_EXISTE, "Usuario ya existe");
            }
        }

        try {
            new UsuarioDAO().insert(usuario);
            return new Respuesta(TipoRespuesta.OK, usuario);
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

    public Respuesta cambiarClave(String id, String viejaClave, String nuevaClave) {
        List<Usuario> usuarios;
        try {
            usuarios = new UsuarioDAO().findAll();
        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener los usuarios: " + e.getMessage());
        }

        for (Usuario u : usuarios) {
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
        return new Respuesta(TipoRespuesta.NO_EXISTE, "Usuario no encontrado");
    }
}
