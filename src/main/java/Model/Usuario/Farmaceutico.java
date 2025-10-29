package Model.Usuario;

import Model.TipoUsuario;

public class Farmaceutico extends Usuario {
    private String licenciaFarmaceutica;

    public Farmaceutico(String id, String clave, String nombre, String licenciaFarmaceutica) {
        super(id, clave, nombre, TipoUsuario.FARMACEUTICO);
        this.licenciaFarmaceutica = licenciaFarmaceutica;
    }

    public String getLicenciaFarmaceutica() {
        return licenciaFarmaceutica;
    }

    public void setLicenciaFarmaceutica(String licenciaFarmaceutica) {
        this.licenciaFarmaceutica = licenciaFarmaceutica;
    }

    @Override
    public String toString() {
        return "Farmaceutico: " + getNombre() + " (ID: " + getId() + ", Licencia: " + licenciaFarmaceutica + ")";
    }
}
