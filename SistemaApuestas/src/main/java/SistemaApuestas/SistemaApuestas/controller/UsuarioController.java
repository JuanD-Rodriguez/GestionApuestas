package SistemaApuestas.SistemaApuestas.controller;

import SistemaApuestas.SistemaApuestas.service.UsuarioService;
import SistemaApuestas.SistemaApuestas.model.Sesion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam("nombre") String nombre,
                                   @RequestParam("correo") String correo,
                                   @RequestParam("contraseña") String contraseña,
                                   Model model) {
        boolean registrado = usuarioService.registrarUsuario(nombre, correo, contraseña);
        if (registrado) {
            model.addAttribute("success", "Usuario registrado con éxito.");
            return "inicioSesion";
        } else {
            model.addAttribute("error", "Error al registrar el usuario. Verifica los datos.");
            return "registro";
        }
    }

    @GetMapping("/iniciar-sesion")
    public String mostrarInicioSesion() {
        return "inicioSesion";
    }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam("correo") String correo,
                                @RequestParam("contraseña") String contraseña,
                                Model model) {
        boolean exitoso = usuarioService.iniciarSesion(correo, contraseña);
        if (exitoso) {
            model.addAttribute("usuario", Sesion.getUsuario());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "inicioSesion";
        }
    }
}