package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.TampLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class LoginController {
    
    @Autowired
    private LoginService loginService;

    @Autowired
    private TampLoginService tampLoginService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/save-login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        String result = loginService.login(email, password);
        // System.out.println(email + " " +result);
        if (result.equals("Login berhasil!")) {
            Login login = loginService.findByEmail(email);
            tampLoginService.saveLogin(login);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("loginError", result);
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("regis", new Login());
        return "register"; 
    }

    @PostMapping("/save-register")
    public String register(@ModelAttribute Login login, @RequestParam String companyName, Model model) {
        // login.setCompanyName(companyName);
        String result = loginService.register(login);
        model.addAttribute("registrationMessage", result);
        return "login"; 
    }
}
