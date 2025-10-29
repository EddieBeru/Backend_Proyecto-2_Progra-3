package Model.Service;

import Model.ClienteHandler;
import Model.Networking.Respuesta;
import Model.Networking.TipoRespuesta;
import Model.Server;


public class ChatService {
    public Respuesta procesarMensaje(ClienteHandler emisor, String receptorId, String mensaje) {
        try {
            ClienteHandler cliente = Server.getClient(receptorId);
            if (cliente == null) {
                return new Respuesta(TipoRespuesta.INVALIDO, "El usuario receptor no está conectado");
            }
            Respuesta paraReceptor = new Respuesta(TipoRespuesta.MENSAJE, new String[]{
                    emisor.getId(), // Emisor
                    mensaje,
                    "false"// Mensaje
            });

            try {
                cliente.send(paraReceptor);
            } catch (Exception e) {
                // si falla el envío, eliminar receptor y notificar al remitente
                Server.removeClient(receptorId);
                return new Respuesta(TipoRespuesta.ERROR_GENERICO, "No se pudo entregar al receptor");
            }

            return new Respuesta(TipoRespuesta.OK, "Enviado");

        } catch (Exception e) {
            return new Respuesta(TipoRespuesta.ERROR_GENERICO, "No se pudo enviar el mensaje");
        }
    }

    public Respuesta procesarMensajeATodos(ClienteHandler emisor, String mensaje) {
        for (ClienteHandler cliente : Server.getAllClients()) {
            Respuesta paraReceptor = new Respuesta(TipoRespuesta.MENSAJE, new String[]{
                    emisor.getId(), // Emisor
                    mensaje,  // Mensaje
                    "true"
            });

            try {
                cliente.send(paraReceptor);
            } catch (Exception e) {
                // si falla el envío, eliminar receptor
                Server.removeClient(cliente.getId());
            }
        }
        return new Respuesta(Model.Networking.TipoRespuesta.OK, mensaje);
    }
}
