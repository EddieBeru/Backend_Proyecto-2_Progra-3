package model;

import java.sql.Date;

public class Usuario {
    private String id;
    private String clave;
    private String nombre;
    private String tipo;
    private String especialidad;
    private String licenciaFarmaceutica;
    private Date fechaNacimiento;

    public Usuario() {}

    public Usuario(String id, String clave, String nombre, String tipo, String especialidad,
                   String licenciaFarmaceutica, Date fechaNacimiento) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
        this.especialidad = especialidad;
        this.licenciaFarmaceutica = licenciaFarmaceutica;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getLicenciaFarmaceutica() { return licenciaFarmaceutica; }
    public void setLicenciaFarmaceutica(String licenciaFarmaceutica) { this.licenciaFarmaceutica = licenciaFarmaceutica; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
