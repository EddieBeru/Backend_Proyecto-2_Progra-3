package Model;

public class Main {
    public static void main(String[] args) {
        System.out.println("Aplicación iniciada");

        try {
            Server server = new Server();
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
