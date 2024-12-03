package org.fitri.accounting.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class Akun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer kodeAkun;
    private String namaAkun;
    private String saldoNormal;

    @OneToMany(mappedBy = "debitAkun", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Transaksi> debitTransaksiList;

    @OneToMany(mappedBy = "kreditAkun", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Transaksi> kreditTransaksiList;
}

