package Model.Usuario;

import Model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Paciente extends Usuario {
    private Date fechaNacimiento;
    private String telefono;

    public Paciente(String id, String clave, String nombre, Date fechaNacimiento, String telefono) {
        super(id, clave, nombre, TipoUsuario.PACIENTE);
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    public Paciente(String nombre) {
        super("","",nombre, TipoUsuario.DEFAULT);
    }

}

