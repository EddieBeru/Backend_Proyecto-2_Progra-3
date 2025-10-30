package Model.Networking;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Solicitud implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private TipoSolicitud accion;
    private Object datos;

    public Solicitud(TipoSolicitud accion, Object datos) {
        this.accion = accion;
        this.datos = datos;
    }

    @Override
    public String toString() {
        return accion.toString() + " - " + datos.toString();
    }
}