package SistemaApuestas.SistemaApuestas.controller;

import SistemaApuestas.SistemaApuestas.service.UsuarioService;
import SistemaApuestas.SistemaApuestas.model.Sesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class SaldoController {

    private final UsuarioService usuarioService;

    public SaldoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/recargar-saldo")
    public String mostrarRecargarSaldo() {
        return "recargarSaldo";
    }

    @PostMapping("/recargar-saldo")
    public String recargarSaldo(@RequestParam("monto") double monto, Model model) {
        if (!Sesion.sesionIniciada()) {
            model.addAttribute("error", "Debes iniciar sesión primero.");
            return "inicioSesion";
        }
        if (monto <= 0) {
            model.addAttribute("error", "El monto debe ser mayor a 0.");
            return "recargarSaldo";
        }
        usuarioService.recargarSaldo(Sesion.getUsuario(), monto);
        model.addAttribute("success", "Saldo recargado con éxito.");
        return "recargarSaldo";
    }

}