package org.fitri.accounting.services;

import org.fitri.accounting.models.Login;
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

    @Autowired
    private LoginService loginService;

    public List<Profile> getAllProfilees() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.getReferenceById(id);
    }

    public void deleteProfileById(Long id) {
        Profile profile = profileRepository.findById(id).orElse(null);
        if (profile != null) {
            // Hapus akun Login terkait
            if (profile.getLogin() != null) {
                loginRepository.delete(profile.getLogin());
            }
            profileRepository.deleteById(id);
        }
    }

    public Profile getByLogin(Login login) {
        if(profileRepository.findByLogin(login) == null){
            return null;
        }
        return profileRepository.findByLogin(login);
    }
    public void saveProfile(Profile profile){
        profile.setLogin(loginService.getLogin());
        profileRepository.save(profile);
    }
}
