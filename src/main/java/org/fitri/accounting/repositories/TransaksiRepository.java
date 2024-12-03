package org.fitri.accounting.repositories;

import java.util.List;

import org.fitri.accounting.models.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaksiRepository extends JpaRepository<Transaksi,Long>{
    @Query("SELECT t FROM Transaksi t WHERE t.debitAkun.id = :akunId OR t.kreditAkun.id = :akunId")
    List<Transaksi> findByAkunId(@Param("akunId") Long akunId);

    List<Transaksi> findByDebitAkun_IdOrKreditAkun_Id(Long debitAkunId, Long kreditAkunId);    

    List<Transaksi> findByNotesContainingIgnoreCase(String notes);

    List<Transaksi> findByNominal(Double nominal);
} 