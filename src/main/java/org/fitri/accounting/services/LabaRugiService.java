package org.fitri.accounting.services;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fitri.accounting.models.Akun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabaRugiService {
    @Autowired
    private BukuBesarService bukuBesarService;

    // Hitung total pendapatan dari saldo terakhir buku besar
    public double getTotalPendapatan() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        double totalPendapatan = 0;

        for (Map.Entry<Akun, Double> entry : saldoBukuBesar.entrySet()) {
            Akun akun = entry.getKey();
            double saldo = entry.getValue();
    
            if ("pendapatan".equalsIgnoreCase(akun.getNamaAkun())) {
                totalPendapatan += saldo;
            }            
        }

        return totalPendapatan;
    }

    public Map<String, Double> getRincianBeban() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        Map<String, Double> rincianBeban = new LinkedHashMap<>();

        for (Map.Entry<Akun, Double> entry : saldoBukuBesar.entrySet()) {
            Akun akun = entry.getKey();
            double saldo = entry.getValue();
            
            // Hanya masukkan akun beban yang memiliki saldo
            if (akun.getNamaAkun().toLowerCase().contains("beban") && saldo > 0) {
                rincianBeban.put(akun.getNamaAkun(), saldo);
            }
        }
        return rincianBeban;
    }

    // Hitung total beban
    public double getTotalBeban() {
        Map<String, Double> rincianBeban = getRincianBeban();
        return rincianBeban.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Hitung laba/rugi bersih
    public double getLabaRugiBersih() {
        return getTotalPendapatan() + getTotalBeban();
    }
    
    public Map<String, Object> getLabaRugiData() {
        Map<String, Object> data = new HashMap<>();

        double totalPendapatan = getTotalPendapatan();
        double totalBeban = getTotalBeban();
        double labaRugiBersih = getLabaRugiBersih();
        Map<String, Double> rincianBeban = getRincianBeban();

        data.put("totalPendapatan", totalPendapatan);
        data.put("rincianBeban", rincianBeban);
        data.put("totalBeban", totalBeban);
        data.put("labaRugiBersih", labaRugiBersih);

        return data;
    }
}
