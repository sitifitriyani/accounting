package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Login login = loginService.getLogin();
        System.out.println("Login : " + login);
        model.addAttribute("profile", profileService.getByLogin(login));
        return "dashboard";
    }
}
