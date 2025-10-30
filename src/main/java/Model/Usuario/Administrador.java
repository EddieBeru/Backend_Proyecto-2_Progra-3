package Model.Usuario;

import Model.Enum.TipoUsuario;

public class Administrador extends Usuario {

    public Administrador(String id, String clave, String nombre) {
        super(id, clave, nombre, TipoUsuario.ADMINISTRADOR);
    }

    @Override
    public String toString() {
        return "Administrador: " + getNombre() + " (ID: " + getId() + ")";
    }
}
