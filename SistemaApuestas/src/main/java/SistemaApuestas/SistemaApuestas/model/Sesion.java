package SistemaApuestas.SistemaApuestas.model;

public class Sesion {
    private static String usuario; // Nombre o correo del usuario que inició sesión

    public Sesion(String nombreUsuario) {
        usuario = nombreUsuario; // Establecer el usuario actual
    }

    public static String getUsuario() {
        return usuario;
    }

    public static boolean sesionIniciada() {
        return usuario != null; // Devuelve true si hay un usuario en sesión
    }

    public static void cerrarSesion() {
        usuario = null; // Cierra la sesión actual
    }

    @Override
    public String toString() {
        return usuario != null ? "Usuario en sesión: " + usuario : "No hay sesión activa";
    }
}
