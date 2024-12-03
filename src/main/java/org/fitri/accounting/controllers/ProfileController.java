package org.fitri.accounting.controllers;

import java.io.IOException;

import org.fitri.accounting.models.Profile;
import org.fitri.accounting.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profilService;

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfil(@PathVariable Long id, @RequestBody Profile profil) {
        Profile existingProfil = profilService.getProfileById(id);
        if (existingProfil == null) {
            return ResponseEntity.notFound().build();
        }
        profil.setId(id);
        Profile updatedProfil = profilService.saveProfile(profil);
        return ResponseEntity.ok(updatedProfil);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProfil(@PathVariable Long id) {
        profilService.deleteProfileById(id);
        return ResponseEntity.ok("Profil dan akun login berhasil dihapus.");
    }
    
    @PostMapping("/upload/{id}")
public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
    try {
        Profile profile = profilService.getProfileById(id);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        profile.setProfileImage(file.getBytes());
        profilService.saveProfile(profile);
        return ResponseEntity.ok("Image uploaded successfully.");
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
    }
}
}
