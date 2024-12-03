package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Profile;
import org.fitri.accounting.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String showDashboard(Model model) {
        Long profileId = 1L; // Ganti dengan logika untuk mendapatkan ID profil yang benar
        Profile profile = profileService.getProfileById(profileId);
        if (profile != null && profile.getProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getProfileImage());
            model.addAttribute("profileImage", base64Image);
        }
        model.addAttribute("profile", profile);
        return "dashboard"; // Nama template Thymeleaf untuk dashboard
    }
}
