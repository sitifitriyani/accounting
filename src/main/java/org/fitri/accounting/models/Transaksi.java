package org.fitri.accounting.models;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Data
public class Transaksi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notes;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "debit_akun_id")
    private Akun debitAkun;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kredit_akun_id")
    private Akun kreditAkun;

    private Double nominal;

    public String getKodeAkun() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getKodeAkun'");
    }
}
