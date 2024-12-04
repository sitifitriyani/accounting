package org.fitri.accounting.services;

import org.fitri.accounting.models.Login;
import org.fitri.accounting.models.TampLogin;
import org.fitri.accounting.repositories.TampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TampLoginService {
    @Autowired
    private TampRepository tampRepository;

    public void saveLogin(Login login) {
        tampRepository.deleteAll();
        TampLogin tampLogin = new TampLogin();
        tampLogin.setEmail(login.getEmail());
        tampLogin.setPassword(login.getPassword());
        tampLogin.setUserName(login.getUsername());
        tampRepository.save(tampLogin);
    }

    public TampLogin getTampLogin(){
        return tampRepository.findAll().get(0);
    }
}
