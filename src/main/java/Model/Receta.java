package Model;

import java.sql.Timestamp;
public class Receta {
    private Integer id;
    private String medicoId;
    private String pacienteId;
    private String estado;
    private Timestamp fechaConfeccion;
    private Timestamp fechaRetiro;

    public Receta(){}

    public Receta(Integer id, String medicoId, String pacienteId, String estado, Timestamp fechaConfeccion, Timestamp fechaRetiro) {
        this.id = id;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.estado = estado;
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getMedicoId() { return medicoId; }
    public void setMedicoId(String medicoId) { this.medicoId = medicoId; }

    public String getPacienteId() { return pacienteId; }
    public void setPacienteId(String pacienteId) { this.pacienteId = pacienteId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getFechaConfeccion() { return fechaConfeccion; }
    public void setFechaConfeccion(Timestamp fechaConfeccion) { this.fechaConfeccion = fechaConfeccion; }

    public Timestamp getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(Timestamp fechaRetiro) { this.fechaRetiro = fechaRetiro; }
}
