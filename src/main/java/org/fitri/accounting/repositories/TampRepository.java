package org.fitri.accounting.repositories;

import org.fitri.accounting.models.TampLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TampRepository extends JpaRepository<TampLogin, Long> {
    TampLogin findByEmail(String email);
}
