package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioRed {
    private String id;
    private String clave;
    private String nombre;
    private TipoUsuario tipo;
    private String especialidad;
    private String licenciaFarmaceutica;
    private Date fechaNacimiento;

    public UsuarioRed() {
    }

}
