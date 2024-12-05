package org.fitri.accounting.services;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.models.Profile;
import org.fitri.accounting.repositories.LoginRepository;
import org.fitri.accounting.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LoginRepository loginRepository;

    public Profile getByLogin(Login login) {
        Profile profile = profileRepository.findByLogin(login);
        if (profile == null) {
            return null; 
        }
        return profile;
        }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    public void deleteProfileById(Long id) {
        // Profile profile = profileRepository.findById(id).orElse(null);
        // if (profile != null && profile.getLogin() != null) {
        //     loginRepository.delete(profile.getLogin());
        // }
        // profileRepository.deleteById(id);
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found");
        }
        profileRepository.deleteById(id);
    }
}
