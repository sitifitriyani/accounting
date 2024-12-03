package org.fitri.accounting.services;

import java.util.List;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    
    @Autowired
    private LoginRepository loginRepository;

    public String register(Login login) {
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

    // public String getCompanyName(Login login) {
    //     // Implement the logic to retrieve the company name
    //     // For example, if you have a database or a map:
    //     // return database.getCompanyNameByLoginId(login.getId());

    //     // Placeholder implementation
    //     return loginRepository.findCompanyNameByLoginId(login.getId());
    //    }

    public String getCompanyNameByEmail(String email) {
        Login login = loginRepository.findByEmail(email);
        if (login != null) {
            String companyName = login.getCompanyName();
            return companyName;
        }
        return null;
    }
}
