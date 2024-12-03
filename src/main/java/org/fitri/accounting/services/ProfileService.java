package org.fitri.accounting.services;

import org.fitri.accounting.models.Profile;
import org.fitri.accounting.repositories.LoginRepository;
import org.fitri.accounting.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LoginRepository loginRepository;

    public List<Profile> getAllProfilees() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public void deleteProfileById(Long id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        if (profile != null) {
            // Hapus akun Login terkait
            if (profile.getEmail() != null) {
                loginRepository.delete(profile.getEmail());
            }
            profileRepository.deleteById(id);
        }
    }
}
