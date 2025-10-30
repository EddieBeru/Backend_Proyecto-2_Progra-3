package Model.Networking;

import Model.Enum.TipoUsuario;
import Model.Usuario.Administrador;
import Model.UsuarioRed;

import java.sql.Date;

public class Utils {
    public static UsuarioRed usuarioToUsuarioRed(Model.Usuario.Usuario usuario) {
        if (usuario instanceof Administrador admin) {
            return new UsuarioRed(
                    admin.getId(),
                    admin.getClave(),
                    admin.getNombre(),
                    TipoUsuario.ADMINISTRADOR,
                    "",
                    "",
                    null,
                    ""
            );
        }
        if (usuario instanceof Model.Usuario.Medico medico) {
            return new UsuarioRed(
                    medico.getId(),
                    medico.getClave(),
                    medico.getNombre(),
                    TipoUsuario.MEDICO,
                    medico.getEspecialidad(),
                    "",
                    null,
                    ""
            );
        }
        if (usuario instanceof Model.Usuario.Farmaceutico farmaceutico) {
            return new UsuarioRed(
                    farmaceutico.getId(),
                    farmaceutico.getClave(),
                    farmaceutico.getNombre(),
                    TipoUsuario.FARMACEUTICO,
                    "",
                    farmaceutico.getLicenciaFarmaceutica(),
                    null,
                    ""
            );
        }
        if (usuario instanceof Model.Usuario.Paciente paciente) {
            return new UsuarioRed(
                    paciente.getId(),
                    paciente.getClave(),
                    paciente.getNombre(),
                    TipoUsuario.PACIENTE,
                    "",
                    "",
                    (Date) paciente.getFechaNacimiento(),
                    paciente.getTelefono()
            );
        }
        return new UsuarioRed(
                usuario.getId(),
                usuario.getClave(),
                usuario.getNombre(),
                TipoUsuario.DEFAULT,
                "",
                "",
                null,
                ""
        );
    }
}
