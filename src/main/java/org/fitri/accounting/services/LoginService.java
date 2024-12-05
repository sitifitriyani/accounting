package org.fitri.accounting.services;

import java.util.List;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
    
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private HttpSession httpSession;

    public Login getLogin() {
        return (Login) httpSession.getAttribute("login");
    }

    public String register(Login login) {
        // Add logging to trace the registration process
        // System.out.println("Attempting to register user: " + login.getUsername());

        // Validasi email
        if (login.getEmail() == null || login.getEmail().isEmpty()) {
            return "Email tidak boleh kosong!";
        }
        // Validasi password
        if (login.getPassword() == null || login.getPassword().length() < 6) {
            return "Password harus minimal 6 karakter!";
        }
        // Validasi username
        if (login.getUsername() == null || login.getUsername().length() < 3 || login.getUsername().length() > 15) {
            return "Username harus minimal 3 karakter dan maksimal 15 karakter!";
        }

          // Cek apakah email sudah ada
        if (loginRepository.findByEmail(login.getEmail()) != null) {
            return "Email sudah digunakan!";
        }
        // Log before saving
        // System.out.println("Saving user to database: " + login.getEmail());
        loginRepository.save(login);
        return "Registrasi berhasil!";
    }

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

    public void UpdateLogin(Login login) {
        // Cek apakah login sudah ada berdasarkan email
        Login existingLogin = loginRepository.findByEmail(login.getEmail());
        if (existingLogin != null) {
            // Update data yang ada
            existingLogin.setUsername(login.getUsername());
            existingLogin.setPassword(login.getPassword());
            loginRepository.save(existingLogin);
    }

}}
