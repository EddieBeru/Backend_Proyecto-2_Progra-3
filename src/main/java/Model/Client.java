package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        try (Socket socket = new Socket(host, port);
             BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
             Scanner console = new Scanner(System.in)) {

            // leer prompt inicial del servidor
            String serverMsg = serverIn.readLine();
            if (serverMsg != null) System.out.println("[Servidor] " + serverMsg);

            // enviar nombre de usuario
            System.out.print("Ingrese su nombre de usuario: ");
            String username = console.nextLine().trim();
            serverOut.println(username);

            // leer ack del servidor
            String ack = serverIn.readLine();
            if (ack != null) System.out.println("[Servidor] " + ack);

            // hilo que escucha mensajes entrantes desde el servidor
            Thread readerThread = new Thread(() -> {
                try {
                    String s;
                    while ((s = serverIn.readLine()) != null) {
                        System.out.println("[RECIBIDO] " + s);
                    }
                } catch (IOException ignored) {}
            });
            readerThread.setDaemon(true);
            readerThread.start();

            System.out.println("Escriba mensajes en formato destinatario:contenido  (escriba /quit para salir)");
            while (true) {
                String line = console.nextLine();
                if (line == null) break;
                if (line.equalsIgnoreCase("/quit")) {
                    break;
                }
                serverOut.println(line);
            }
        } catch (IOException e) {
            System.err.println("No se pudo conectar: " + e.getMessage());
        }
    }
}