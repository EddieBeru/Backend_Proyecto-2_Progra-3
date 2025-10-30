package Model.Networking;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Respuesta implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private TipoRespuesta estado;
    private Object resultado;

    public Respuesta(TipoRespuesta estado, Object resultado) {
        this.estado = estado;
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return estado.toString() + " - " + resultado.toString();
    }
}