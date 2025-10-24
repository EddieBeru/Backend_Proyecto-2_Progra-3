package model;

import model.Networking.Respuesta;
import model.Networking.Solicitud;
import model.Networking.TipoSolicitud;

import java.io.*;
import java.net.*;
import java.util.Scanner;

// Solo un ejemplo; adaptá según tu clase Solicitud y Respuesta
public class Test {

    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 1928;

        try (Socket socket = new Socket(host, puerto)) {

            // Primero el ObjectOutputStream, luego InputStream
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // importante
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Conectado al servidor en " + host + ":" + puerto);

            boolean salir = false;
            while (!salir) {
                System.out.print("Ingrese mensaje para enviar al servidor (o 'salir' para terminar): ");
                String tipo = scanner.nextLine();

                if (tipo.equalsIgnoreCase("salir")) {
                    salir = true;
                    continue;
                }

                String mensaje = scanner.nextLine();

                // Creamos la solicitud según lo que tu handler espera
                Solicitud soli = new Solicitud(TipoSolicitud.valueOf(tipo), mensaje); // ajustá el constructor
                out.writeObject(soli);
                out.flush();

                // Esperamos la respuesta del servidor
                Respuesta resp = (Respuesta) in.readObject();
                System.out.println("Servidor respondió: " + resp.toString());
            }

            System.out.println("Cliente finalizado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

