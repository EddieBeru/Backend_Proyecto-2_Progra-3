package Model.Service;

import Model.DAO.MedicamentoDAO;
import Model.Medicamento;
import Model.Networking.Respuesta;
import Model.Networking.TipoRespuesta;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoService {
    public MedicamentoService(){}

    public Respuesta getMedicamentos(){
        try {
            return new Respuesta(TipoRespuesta.OK, new MedicamentoDAO().findAll());
        } catch (Exception ex) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Error al obtener los medicamentos: " + ex.getMessage());
        }
    }

    public Respuesta addMedicamento(Medicamento medicamento){
        try {
            new MedicamentoDAO().insert(medicamento);
            return new Respuesta(TipoRespuesta.OK, medicamento);
        } catch (Exception ex) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Error al agregar el medicamento: " + ex.getMessage());
        }
    }

    public Respuesta removeMedicamento(String codigo){
        try {
            new MedicamentoDAO().delete(codigo);
            return new Respuesta(TipoRespuesta.OK, null);
        } catch (Exception ex) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Error al eliminar el medicamento: " + ex.getMessage());
        }
    }

    public Respuesta updateMedicamento(Medicamento medicamento){
        try {
            new MedicamentoDAO().update(medicamento);
            return new Respuesta(TipoRespuesta.OK, medicamento);
        } catch (Exception ex) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "Error al actualizar el medicamento: " + ex.getMessage());
        }
    }
}
