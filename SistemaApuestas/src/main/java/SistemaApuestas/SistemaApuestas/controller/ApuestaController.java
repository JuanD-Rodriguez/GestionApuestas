package SistemaApuestas.SistemaApuestas.controller;

import SistemaApuestas.SistemaApuestas.model.Apuesta;
import SistemaApuestas.SistemaApuestas.model.Sesion;
import SistemaApuestas.SistemaApuestas.service.ApuestaService;
import SistemaApuestas.SistemaApuestas.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ApuestaController {

    private final ApuestaService apuestaService;
    private final UsuarioService usuarioService; // Inyectamos UsuarioService

    public ApuestaController(ApuestaService apuestaService, UsuarioService usuarioService) {
        this.apuestaService = apuestaService;
        this.usuarioService = usuarioService; // Inicializamos el servicio
    }

    @GetMapping("/realizar-apuesta")
    public String mostrarRealizarApuesta(Model model) {
        if (!Sesion.sesionIniciada()) {
            model.addAttribute("error", "Debes iniciar sesión para realizar apuestas.");
            return "inicioSesion";
        }
        // Agrega eventos y tipos al modelo
        model.addAttribute("eventos", apuestaService.obtenerEventos());
        model.addAttribute("tipos", apuestaService.obtenerTiposDeApuesta());
        return "realizarApuesta";
    }

    @PostMapping("/realizar-apuesta")
    public String realizarApuesta(@RequestParam("evento") String evento,
                                  @RequestParam("equipo") String equipo,
                                  @RequestParam("tipo") String tipo,
                                  @RequestParam("monto") double monto,
                                  @RequestParam("detalle") String detalle,
                                  Model model) {
        if (!Sesion.sesionIniciada()) {
            model.addAttribute("error", "Debes iniciar sesión para realizar apuestas.");
            return "inicioSesion";
        }
        boolean realizada = apuestaService.realizarApuesta(evento, equipo, tipo, monto, detalle);
        if (realizada) {
            model.addAttribute("success", "Apuesta realizada con éxito.");
        } else {
            model.addAttribute("error", "No se pudo realizar la apuesta. Verifica tu saldo.");
        }
        return "realizarApuesta";
    }
    @GetMapping("/resultados")
    public String mostrarResultados(Model model) {
        if (!Sesion.sesionIniciada()) {
            model.addAttribute("error", "Debes iniciar sesión para ver los resultados.");
            return "inicioSesion";
        }

        String correo = Sesion.getUsuario();

        // Procesar las apuestas para actualizar el saldo
        apuestaService.procesarApuestas(correo);

        // Obtener las apuestas, resultados y saldo actualizado
        List<Apuesta> apuestas = apuestaService.obtenerHistorial();
        Map<String, Map<String, Integer>> resultados = apuestaService.obtenerResultados();
        double saldo = usuarioService.obtenerSaldo(correo);

        // Evaluar si las apuestas fueron ganadoras o no
        for (Apuesta apuesta : apuestas) {
            boolean ganadora = apuestaService.evaluarApuesta(apuesta);
            apuesta.setGanadora(ganadora); // Actualizar estado de la apuesta
        }

        // Pasar los datos al modelo
        model.addAttribute("saldo", saldo);
        model.addAttribute("resultados", resultados);
        model.addAttribute("apuestas", apuestas);
        return "resultados";
    }


    @GetMapping("/ver-apuestas")
    public String verApuestas(Model model) {
        if (!Sesion.sesionIniciada()) {
            return "inicioSesion";
        }
        model.addAttribute("apuestas", apuestaService.obtenerHistorial());
        return "verApuestas";
    }
}
