package org.fitri.accounting.repositories;

import org.fitri.accounting.models.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Login findByEmail(String email);
}
