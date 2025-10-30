package Model;

import Model.Enum.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioRed implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String clave;
    private String nombre;
    private TipoUsuario tipo;
    private String especialidad;
    private String licenciaFarmaceutica;
    private Date fechaNacimiento;
    private String telefono;

    public UsuarioRed()  {

    }

}