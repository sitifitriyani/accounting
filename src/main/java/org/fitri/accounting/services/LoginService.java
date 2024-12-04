package org.fitri.accounting.services;

import java.util.List;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.models.TampLogin;
import org.fitri.accounting.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private TampLoginService tampLoginService;

    public String register(Login login) {
           // Validasi email
        if (login.getEmail() == null || login.getEmail().isEmpty()) {
            return "Email tidak boleh kosong!";
        }
        // Validasi password
        if (login.getPassword() == null || login.getPassword().length() < 6) {
            return "Password harus minimal 6 karakter!";
        }

        Login existingLogin = loginRepository.findByEmail(login.getEmail());
        if (existingLogin != null) {
            return "Email sudah digunakan!";
        }
        loginRepository.save(login);
        return "Registrasi berhasil!";
    }

    int a;
    public String login(String email, String password) {
        Login login = loginRepository.findByEmail(email);
        if (login == null) {
            return "Email tidak ditemukan!";
        }
        if (!password.equals(login.getPassword())) {
             return "Password salah!";
        }
        return "Login berhasil!";
    }
    public List<Login> findAll() {
        return loginRepository.findAll();
    }

    public Login findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    public Login getLogin(){
        TampLogin tampLogin = tampLoginService.getTampLogin();
        return loginRepository.findByEmail(tampLogin.getEmail());
    }
}
