package model.DAO;

import model.ConexionDB;
import model.Medicamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {
    private final Connection conexion;

    public MedicamentoDAO() throws SQLException {
        conexion = ConexionDB.getConexion();
    }

    public void insert(Medicamento m) throws SQLException {
        String sql = "INSERT INTO medicamentos (codigo, nombre, presentacion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, m.getCodigo());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getPresentacion());
            ps.executeUpdate();
        }
    }

    public void update(Medicamento m) throws SQLException {
        String sql = "UPDATE medicamentos SET nombre = ?, presentacion = ? WHERE codigo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getPresentacion());
            ps.setString(3, m.getCodigo());
            ps.executeUpdate();
        }
    }

    public void delete(String codigo) throws SQLException {
        String sql = "DELETE FROM medicamentos WHERE codigo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        }
    }

    public Medicamento findById(String codigo) throws SQLException {
        String sql = "SELECT codigo, nombre, presentacion FROM medicamentos WHERE codigo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicamento(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("presentacion")
                    );
                }
                return null;
            }
        }
    }

    public List<Medicamento> findAll() throws SQLException {
        String sql = "SELECT codigo, nombre, presentacion FROM medicamentos";
        List<Medicamento> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Medicamento(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("presentacion")
                ));
            }
        }
        return lista;
    }
}
