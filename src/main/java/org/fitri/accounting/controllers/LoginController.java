package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginController {
    
    @Autowired
    private LoginService loginService;

    @Autowired
    private HttpSession httpSession;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/save-login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        String result = loginService.login(email, password);
        if (result.equals("Login berhasil!")) {
            Login login = loginService.findByEmail(email);
            httpSession.setAttribute("login", login);  
            model.addAttribute("message", "Login successful!");  // Menambahkan pesan sukses
            return "redirect:/dashboard";  
        } else {
            model.addAttribute("loginError", result);  
            model.addAttribute("message", "Login failed. Please try again.");  
            return "login"; 
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("regis", new Login());
        return "register"; 
    }

    @PostMapping("/save-register")
    public String register(@ModelAttribute Login login, Model model) {
        String result = loginService.register(login);
        if (result.equals("Registrasi berhasil!")) {
            model.addAttribute("registrationMessage", result);
            return "redirect:/auth/login"; 
        } else {
            model.addAttribute("registrationError", result); 
            return "register"; 
        }
    }
}
