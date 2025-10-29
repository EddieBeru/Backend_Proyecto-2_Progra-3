package Model.Usuario;

import Model.TipoUsuario;
import lombok.Data;

@Data
public class Usuario {
    private String id;
    private String clave;
    private String nombre;
    private TipoUsuario tipo; // medico, farmaceuta, administrador

    public Usuario(String id, String clave, String nombre, TipoUsuario tipo) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
