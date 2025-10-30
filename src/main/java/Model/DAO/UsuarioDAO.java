package Model.DAO;

import Model.ConexionDB;
import Model.Enum.TipoUsuario;
import Model.UsuarioRed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final Connection conexion;

    public UsuarioDAO() throws SQLException {
        conexion = ConexionDB.getConexion();
    }

    public void insert(UsuarioRed u) throws SQLException {
        String sql = "INSERT INTO usuarios (id, clave, nombre, tipo, especialidad, licencia_farmaceutica, fecha_nacimiento, telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getClave());
            ps.setString(3, u.getNombre());
            ps.setString(4, String.valueOf(u.getTipo()));
            ps.setString(5, u.getEspecialidad());
            ps.setString(6, u.getLicenciaFarmaceutica());
            if (u.getFechaNacimiento() != null) ps.setDate(7, u.getFechaNacimiento());
            else ps.setNull(7, Types.DATE);
            ps.setString(8, u.getTelefono());
            ps.executeUpdate();
        }
    }

    public void update(UsuarioRed u) throws SQLException {
        String sql = "UPDATE usuarios SET clave = ?, nombre = ?, tipo = ?, especialidad = ?, licencia_farmaceutica = ?, fecha_nacimiento = ?, telefono = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, u.getClave());
            ps.setString(2, u.getNombre());
            ps.setString(3, String.valueOf(u.getTipo()));
            ps.setString(4, u.getEspecialidad());
            ps.setString(5, u.getLicenciaFarmaceutica());
            if (u.getFechaNacimiento() != null) ps.setDate(6, u.getFechaNacimiento());
            else ps.setNull(6, Types.DATE);
            ps.setString(7, u.getTelefono());
            ps.setString(8, u.getId());
            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public UsuarioRed findById(String id) throws SQLException {
        String sql = "SELECT id, clave, nombre, tipo, especialidad, licencia_farmaceutica, fecha_nacimiento, telefono FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioRed(
                            rs.getString("id"),
                            rs.getString("clave"),
                            rs.getString("nombre"),
                            TipoUsuario.valueOf(rs.getString("tipo")),
                            rs.getString("especialidad"),
                            rs.getString("licencia_farmaceutica"),
                            rs.getDate("fecha_nacimiento"),
                            rs.getString("telefono")
                    );
                }
                return null;
            }
        }
    }

    public List<UsuarioRed> findAll() throws SQLException {
        String sql = "SELECT id, clave, nombre, tipo, especialidad, licencia_farmaceutica, fecha_nacimiento, telefono FROM usuarios";
        List<UsuarioRed> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new UsuarioRed(
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("nombre"),
                        TipoUsuario.valueOf(rs.getString("tipo")),
                        rs.getString("especialidad"),
                        rs.getString("licencia_farmaceutica"),
                        rs.getDate("fecha_nacimiento"),
                        rs.getString("telefono")
                ));
            }
        }
        return lista;
    }
}
