package org.fitri.accounting.controllers;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.models.Profile;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService loginService;

    @GetMapping
    public String getProfile(Model model) {
        Login login = loginService.getLogin();
        if (login == null) {
            return "redirect:/auth/login";
        }
    
        Profile profile = profileService.getByLogin(login);
        if (profile == null) {
            profile = new Profile();
            profile.setLogin(login); 
        }
    
        model.addAttribute("profile", profile);
        return "profile";
    }
    

@GetMapping("/create")
public String createProfile(Model model) {
    Login login = loginService.getLogin();
    System.out.println(login);
    if (login == null) {
        return "redirect:/auth/login";
    }
    Profile profile = profileService.getByLogin(login);
    if (profile == null) {
        profile = new Profile();
        profile.setLogin(login);
    }
    // model.addAttribute("login", profile.getLogin());
    model.addAttribute("profile", profile);
    return "edit-profile";
}
    // @PostMapping("/save-profile")
    // public String saveProfile(@ModelAttribute Profile profile, @RequestParam("profileImageFile") MultipartFile file) {
    //     try {
    //         if (!file.isEmpty()) {
    //             Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "images", file.getOriginalFilename());
    //             file.transferTo(path.toFile());
    //             String imageUrl = "/images/" + file.getOriginalFilename();
    //             profile.setProfileImage(imageUrl);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     profileService.saveProfile(profile);
    //     return "redirect:/dashboard";
    // }
    // @PostMapping("/save-profile")
    // public String saveProfile(@ModelAttribute Profile profile) {
    //     // Simpan atau perbarui login
    //     loginService.UpdateLogin(profile.getLogin());

    //     // Simpan profil
    //     profileService.saveProfile(profile);
    //     return "redirect:/profile";
    // }

    // @PostMapping("/delete/{id}")
    // public String deleteProfile(@PathVariable Long id) {
    //     profileService.deleteProfileById(id);
    //     return "redirect:/login";
    // }

    @PostMapping("/save-profile")
    public String saveProfile(@ModelAttribute Profile profile, @RequestParam("profileImageFile") MultipartFile file, Model model) {
        if (profile.getLogin() == null || profile.getLogin().getId() == null) {
            model.addAttribute("errorMessage", "Login ID tidak boleh kosong.");
            return "edit-profile";
        }
        try {
            if (!file.isEmpty()) {
                Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "image", file.getOriginalFilename());
                file.transferTo(path.toFile());
                String imageUrl = "/image/" + file.getOriginalFilename();
                profile.setProfileImage(imageUrl);
            }
            profileService.saveProfile(profile);
            return "redirect:/profile";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading file: " + e.getMessage());
            return "edit-profile";
    }
}
}
