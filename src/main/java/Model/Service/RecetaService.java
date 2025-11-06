package Model.Service;

import Model.DAO.RecetaDAO;
import Model.Networking.Respuesta;
import Model.Networking.TipoRespuesta;

import java.sql.SQLException;
import java.util.Map;

public class RecetaService {

    public Respuesta getIndicadoresDashboard() {
        try {
            RecetaDAO dao = new RecetaDAO();
            Map<String, Object> data = dao.obtenerIndicadoresDashboard();
            return new Respuesta(TipoRespuesta.OK, data);
        } catch (SQLException e) {
            return new Respuesta(
                    TipoRespuesta.ERROR_GENERICO,
                    "Error al obtener indicadores: " + e.getMessage()
            );
        }
    }
}
