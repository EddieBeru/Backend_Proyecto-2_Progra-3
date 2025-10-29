package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    static final String url = "jdbc:mysql://localhost:3306/recetas";
    static final String usuario = "admin";
    static final String contra = "AdminPass1";

    private static Connection conexion;

    public static Connection getConexion() throws SQLException {
        if (conexion == null) {
            conexion = DriverManager.getConnection(url, usuario, contra);
        }
        return conexion;
    }
}
