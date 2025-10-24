package model.Networking;

import java.io.Serializable;

public class Solicitud implements Serializable {
    private TipoSolicitud accion;
    private Object datos;

    public Solicitud(TipoSolicitud accion, Object datos) {
        this.accion = accion;
        this.datos = datos;
    }

    public TipoSolicitud getAccion() { return accion; }
    public Object getDatos() { return datos; }

    @Override
    public String toString() {
        return accion.toString() + " - " + datos.toString();
    }
}