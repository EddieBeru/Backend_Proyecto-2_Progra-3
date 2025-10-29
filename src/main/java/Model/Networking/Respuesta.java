package Model.Networking;
import java.io.Serializable;

public class Respuesta implements Serializable {
    private TipoRespuesta estado;
    private Object resultado;

    public Respuesta(TipoRespuesta estado, Object resultado) {
        this.estado = estado;
        this.resultado = resultado;
    }

    public TipoRespuesta getEstado() { return estado; }
    public Object getResultado() { return resultado; }

    @Override
    public String toString() {
        return estado.toString() + " - " + resultado.toString();
    }
}