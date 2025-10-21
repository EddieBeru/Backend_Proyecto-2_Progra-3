package model.DAO;

import model.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;

public class RecetaDAO {
    Connection conexion;
    public RecetaDAO() throws SQLException {
        conexion = ConexionDB.getConexion();
    }

    //Terminar de implementar
    public void insert() {

    }
    public void update() {

    }
    public void delete() {

    }
    public void selectById() {

    }
    public void selectAll() {

    }
}
