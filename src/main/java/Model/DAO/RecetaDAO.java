package Model.DAO;

import Model.ConexionDB;
import Model.Receta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class RecetaDAO {
    private final Connection conexion;

    public RecetaDAO() throws SQLException {
        conexion = ConexionDB.getConexion();
    }

    public int insert(Receta r) throws SQLException {
        String sql = "INSERT INTO recetas (medico_id, paciente_id, estado, fecha_confeccion, fecha_retiro) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (r.getMedicoId() != null) ps.setString(1, r.getMedicoId()); else ps.setNull(1, Types.VARCHAR);
            ps.setString(2, r.getPacienteId());
            ps.setString(3, r.getEstado());
            ps.setTimestamp(4, r.getFechaConfeccion());
            if (r.getFechaRetiro() != null) ps.setTimestamp(5, r.getFechaRetiro()); else ps.setNull(5, Types.TIMESTAMP);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    r.setId(id);
                    return id;
                }
            }
            return -1;
        }
    }

    public void update(Receta r) throws SQLException {
        String sql = "UPDATE recetas SET medico_id = ?, paciente_id = ?, estado = ?, fecha_confeccion = ?, fecha_retiro = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (r.getMedicoId() != null) ps.setString(1, r.getMedicoId()); else ps.setNull(1, Types.VARCHAR);
            ps.setString(2, r.getPacienteId());
            ps.setString(3, r.getEstado());
            ps.setTimestamp(4, r.getFechaConfeccion());
            if (r.getFechaRetiro() != null) ps.setTimestamp(5, r.getFechaRetiro()); else ps.setNull(5, Types.TIMESTAMP);
            ps.setInt(6, r.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM recetas WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Receta findById(int id) throws SQLException {
        String sql = "SELECT id, medico_id, paciente_id, estado, fecha_confeccion, fecha_retiro FROM recetas WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Receta(
                            rs.getInt("id"),
                            rs.getString("medico_id"),
                            rs.getString("paciente_id"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_confeccion"),
                            rs.getTimestamp("fecha_retiro")
                    );
                }
                return null;
            }
        }
    }

    public List<Receta> findAll() throws SQLException {
        String sql = "SELECT id, medico_id, paciente_id, estado, fecha_confeccion, fecha_retiro FROM recetas";
        List<Receta> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Receta(
                        rs.getInt("id"),
                        rs.getString("medico_id"),
                        rs.getString("paciente_id"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_confeccion"),
                        rs.getTimestamp("fecha_retiro")
                ));
            }
        }
        return lista;
    }

    public Map<String, Object> obtenerIndicadoresDashboard() throws SQLException {
        Map<String, Object> res = new HashMap<>();

        // 1) Total de recetas emitidas
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT COUNT(*) FROM recetas");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                res.put("totalRecetasEmitidas", rs.getInt(1));
            }
        }

        // 2) Total de recetas despachadas (ej: estado = 'ENTREGADA')
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT COUNT(*) FROM recetas WHERE estado = 'ENTREGADA'");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                res.put("totalRecetasDespachadas", rs.getInt(1));
            }
        }

        // 3) Total de pacientes atendidos (pacientes distintos con al menos una receta)
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT COUNT(DISTINCT paciente_id) FROM recetas");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                res.put("totalPacientesAtendidos", rs.getInt(1));
            }
        }

        // 4) Top medicamentos más prescritos (ej: top 5)
        List<Map<String, Object>> topMedicamentos = new ArrayList<>();
        String sqlTop = """
                SELECT m.codigo, m.nombre, COUNT(*) AS veces
                FROM receta_preescripciones rp
                JOIN medicamentos m ON rp.medicamento_codigo = m.codigo
                GROUP BY m.codigo, m.nombre
                ORDER BY veces DESC
                LIMIT 5
                """;

        try (PreparedStatement ps = conexion.prepareStatement(sqlTop);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("codigo", rs.getString("codigo"));
                fila.put("nombre", rs.getString("nombre"));
                fila.put("veces", rs.getInt("veces"));
                topMedicamentos.add(fila);
            }
        }

        res.put("topMedicamentos", topMedicamentos);

        return res;
    }

}


