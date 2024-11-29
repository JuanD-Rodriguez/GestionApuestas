package SistemaApuestas.SistemaApuestas.service;

import SistemaApuestas.SistemaApuestas.model.Apuesta;
import SistemaApuestas.SistemaApuestas.model.Sesion;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApuestaService {

    private final List<Apuesta> apuestas = new ArrayList<>();
    private final Map<String, Map<String, Integer>> resultados = new HashMap<>();
    private final UsuarioService usuarioService;

    // Constructor con inyección de dependencias
    public ApuestaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        // Resultados predeterminados de los eventos
        Map<String, Integer> colombiaVsEcuador = new HashMap<>();
        colombiaVsEcuador.put("totalGolesColombia", 2);
        colombiaVsEcuador.put("cantidadTarjetas", 3);

        Map<String, Integer> argentinaVsBrasil = new HashMap<>();
        argentinaVsBrasil.put("totalGolesArgentina", 1);
        argentinaVsBrasil.put("cantidadTarjetas", 4);

        Map<String, Integer> uruguayVsChile = new HashMap<>();
        uruguayVsChile.put("totalGolesUruguay", 3);
        uruguayVsChile.put("cantidadTarjetas", 2);

        resultados.put("Colombia vs Ecuador", colombiaVsEcuador);
        resultados.put("Argentina vs Brasil", argentinaVsBrasil);
        resultados.put("Uruguay vs Chile", uruguayVsChile);
    }

    public List<String> obtenerEventos() {
        List<String> eventos = new ArrayList<>();
        eventos.add("Colombia vs Ecuador");
        eventos.add("Argentina vs Brasil");
        eventos.add("Uruguay vs Chile");
        return eventos;
    }

    public List<String> obtenerTiposDeApuesta() {
        List<String> tiposDeApuesta = new ArrayList<>();
        tiposDeApuesta.add("Total de goles");
        tiposDeApuesta.add("Cantidad de tarjetas");
        return tiposDeApuesta;
    }
    public boolean realizarApuesta(String evento, String equipo, String tipo, double monto, String detalle) {
        if (!Sesion.sesionIniciada()) {
            return false; // No hay sesión activa
        }

        String correo = Sesion.getUsuario();

        // Verificar saldo disponible
        if (usuarioService.obtenerSaldo(correo) < monto) {
            return false; // Saldo insuficiente
        }

        // Descontar saldo al realizar la apuesta
        usuarioService.recargarSaldo(correo, -monto);

        // Crear y registrar la apuesta
        Apuesta nuevaApuesta = new Apuesta(evento, equipo, tipo, monto, detalle);
        apuestas.add(nuevaApuesta);
        return true;
    }
    public void procesarApuestas(String correo) {
        for (Apuesta apuesta : apuestas) {
            boolean ganadora = evaluarApuesta(apuesta);

            if (ganadora) {
                // Si gana, sumar el doble del monto apostado al saldo
                usuarioService.recargarSaldo(correo, apuesta.getMonto() * 2);
            }
            // Si pierde, no hacemos nada porque ya se descontó al realizar la apuesta
        }
    }


    public boolean evaluarApuesta(Apuesta apuesta) {
        Map<String, Integer> resultadoEvento = resultados.get(apuesta.getEvento());

        if (resultadoEvento == null) {
            return false; // Evento no encontrado
        }

        String claveResultado;
        if (apuesta.getTipo().equals("Total de goles")) {
            claveResultado = "totalGoles" + apuesta.getEquipo();
        } else if (apuesta.getTipo().equals("Cantidad de tarjetas")) {
            claveResultado = "cantidadTarjetas";
        } else {
            return false; // Tipo de apuesta no válida
        }

        int resultado = resultadoEvento.getOrDefault(claveResultado, -1);
        return resultado == Integer.parseInt(apuesta.getDetalle());
    }

    public List<Apuesta> obtenerHistorial() {
        return apuestas;
    }

    public Map<String, Map<String, Integer>> obtenerResultados() {
        return resultados; // Devuelve los resultados configurados
    }
}
