package Model.Usuario;

import Model.TipoUsuario;

public class Medico extends Usuario {
    private String especialidad;

    public Medico(String id, String clave, String nombre, String especialidad) {
        super(id, clave, nombre, TipoUsuario.MEDICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    @Override
    public String toString() {
        return "Medico: " + getNombre() + " (ID: " + getId() + ", Especialidad: " + especialidad + ")";
    }
}
