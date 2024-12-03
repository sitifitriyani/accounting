package org.fitri.accounting.repositories;

import org.fitri.accounting.models.Akun;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AkunRepository extends JpaRepository<Akun, Long> {
    boolean existsByKodeAkun(Integer kodeAkun);
    boolean existsByNamaAkun(String namaAkun);

    // Query untuk mencari akun berdasarkan nama
    List<Akun> findByNamaAkunContainingIgnoreCase(String namaAkun);

    // Query untuk mencari akun berdasarkan kode akun
    List<Akun> findByKodeAkun(Integer kodeAkun);
}

 