package Model.Usuario;

import Model.Enum.TipoUsuario;
import lombok.Data;

import java.io.Serializable;

@Data
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

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
