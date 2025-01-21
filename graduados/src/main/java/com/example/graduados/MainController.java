package com.example.graduados;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index"; // Renderiza templates/index.html
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio"; // Renderiza templates/inicio.html
    }

    
    @GetMapping("/admin")
    public String admin() {
        return "admin"; // Renderiza templates/inicio.html
    }

    
    @GetMapping("/actualizar_datos")
    public String actualizar_datos() {
        return "actualizar_datos"; // Renderiza templates/inicio.html
    }

    
    @GetMapping("/registrar")
    public String registrar() {
        return "registrar"; // Renderiza templates/inicio.html
    }


}
