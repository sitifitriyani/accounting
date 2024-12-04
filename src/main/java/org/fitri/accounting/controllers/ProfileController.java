package org.fitri.accounting.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.fitri.accounting.models.Profile;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profilService;

    @Autowired
    private LoginService loginService;


    // @PutMapping("/{id}")
    // public ResponseEntity<Profile> updateProfil(@PathVariable Long id, @RequestBody Profile profil) {
    //     Profile existingProfil = profilService.getProfileById(id);
    //     if (existingProfil == null) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     profil.setId(id);
    //     Profile updatedProfil = profilService.saveProfile(profil);
    //     return ResponseEntity.ok(updatedProfil);
    // }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProfil(@PathVariable Long id) {
        profilService.deleteProfileById(id);
        return ResponseEntity.ok("Profil dan akun login berhasil dihapus.");
    }
    
    @PostMapping("/upload/{id}")
public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestPart("image") MultipartFile file) {
    try {
        Profile profile = profilService.getProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        Path path = Paths.get(System.getProperty("user.dir"),"src","main","resources", "static", file.getOriginalFilename());
        file.transferTo(path.toFile());
        String url = "http://localhost:8080/"+file.getOriginalFilename();
        profile.setProfileImage(url);
        profilService.saveProfile(profile);
        return ResponseEntity.ok("Image uploaded successfully.");
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
    }
}

    @GetMapping("/create")
    public String createProfile(Model model){
        Profile profile = profilService.getByLogin(loginService.getLogin());
        if(profile == null){
            model.addAttribute("profile", new Profile());
        }else{
            model.addAttribute("profile", profile);
        }
        return "profile";
    }
    @PostMapping("/save-profile")
    public String saveProfile(Profile profile){
            profilService.saveProfile(profile);
        return "redirect:/dashboard";
    }
}
