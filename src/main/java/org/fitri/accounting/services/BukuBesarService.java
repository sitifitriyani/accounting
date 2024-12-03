package org.fitri.accounting.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fitri.accounting.models.Akun;
import org.fitri.accounting.models.Transaksi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BukuBesarService {
    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private AkunService akunService;

    public Map<Akun, List<Map<String, Object>>> getBukuBesar() {
        List<Akun> akunList = akunService.getAllAkun();
        Map<Akun, List<Map<String, Object>>> bukuBesar = new LinkedHashMap<>();

        for (Akun akun : akunList) {
            List<Transaksi> transaksiList = transaksiService.getTransaksiByAkunId(akun.getId());
            List<Map<String, Object>> transaksiWithSaldo = new ArrayList<>();
            double saldo = 0;

            for (Transaksi transaksi : transaksiList) {
                Map<String, Object> transaksiData = new HashMap<>();
                transaksiData.put("tanggal", transaksi.getDate());
                transaksiData.put("notes", transaksi.getNotes());

                double debit = 0.0;
                double kredit = 0.0;

                // Tentukan posisi debit/kredit berdasarkan akun transaksi
                if (transaksi.getDebitAkun() != null && transaksi.getDebitAkun().getId().equals(akun.getId())) {
                    debit = transaksi.getNominal();
                    saldo += debit;
                } else if (transaksi.getKreditAkun() != null && transaksi.getKreditAkun().getId().equals(akun.getId())) {
                    kredit = transaksi.getNominal();
                    saldo -= kredit;
                }

                // Simpan data mutasi dan saldo
                transaksiData.put("debit", debit);
                transaksiData.put("kredit", kredit);

                // Tentukan sisi saldo
                transaksiData.put("saldoDebit", saldo > 0 ? saldo : 0.0);
                transaksiData.put("saldoKredit", saldo < 0 ? Math.abs(saldo) : 0.0);

                transaksiWithSaldo.add(transaksiData);
            }

            bukuBesar.put(akun, transaksiWithSaldo);
        }

        return bukuBesar;
    }

    public Map<Akun, Double> getSaldoBukuBesar() {
        List<Akun> akunList = akunService.getAllAkun();
        Map<Akun, Double> saldoBukuBesar = new LinkedHashMap<>();

        for (Akun akun : akunList) {
            double saldo = transaksiService.calculateSaldoAkun(akun.getId());
            saldoBukuBesar.put(akun, saldo);
        }

        return saldoBukuBesar;
    }

}
