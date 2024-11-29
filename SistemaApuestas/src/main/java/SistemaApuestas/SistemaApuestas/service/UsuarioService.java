package SistemaApuestas.SistemaApuestas.service;

import SistemaApuestas.SistemaApuestas.model.Sesion;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsuarioService {

    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private Map<String, Double> saldos = new HashMap<>();

    public UsuarioService() {
        cargarSaldos();
    }

    public boolean registrarUsuario(String nombre, String correo, String contraseña) {
        if (!esCorreoValido(correo)) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_USUARIOS, true))) {
            writer.write(nombre + "," + correo + "," + contraseña);
            writer.newLine();
            saldos.put(correo, 0.0); // Inicializar saldo en 0 para nuevos usuarios
            guardarSaldos();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean iniciarSesion(String correo, String contraseña) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[1].equals(correo) && datos[2].equals(contraseña)) {
                    new Sesion(datos[0]); // Iniciar la sesión con el nombre del usuario
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void recargarSaldo(String correo, double monto) {
        if (!saldos.containsKey(correo)) {
            saldos.put(correo, 0.0);
        }
        saldos.put(correo, saldos.get(correo) + monto);
        guardarSaldos();
    }


    public double obtenerSaldo(String correo) {
        return saldos.getOrDefault(correo, 0.0);
    }

    private boolean esCorreoValido(String correo) {
        String regexCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return correo.matches(regexCorreo);
    }

    private void cargarSaldos() {
        File archivo = new File("saldos.txt");
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split(",");
                    saldos.put(datos[0], Double.parseDouble(datos[1]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarSaldos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saldos.txt"))) {
            for (Map.Entry<String, Double> entrada : saldos.entrySet()) {
                writer.write(entrada.getKey() + "," + entrada.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
