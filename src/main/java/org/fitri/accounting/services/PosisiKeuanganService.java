package org.fitri.accounting.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.fitri.accounting.models.Akun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PosisiKeuanganService {

    @Autowired
    private BukuBesarService bukuBesarService;

    @Autowired
    private PerubahanEkuitasService perubahanEkuitasService;

    public Map<String, Double> getAsetList() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        Map<String, Double> rincianAset = new LinkedHashMap<>();

        for (Map.Entry<Akun, Double> entry : saldoBukuBesar.entrySet()) {
            Akun akun = entry.getKey();
            double saldo = entry.getValue();
            
            if (akun.getKodeAkun().toString().startsWith("1") && saldo > 0) {
                rincianAset.put(akun.getNamaAkun(), saldo);
            }
        }
        return rincianAset;
    }

    public Map<String, Double> getLiabilitasList() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        System.out.println(saldoBukuBesar);
        Map<String, Double> rincianLiabilitas = new LinkedHashMap<>();

        for (Map.Entry<Akun, Double> entry : saldoBukuBesar.entrySet()) {
            Akun akun = entry.getKey();
            double saldo = entry.getValue();
            
            if (akun.getKodeAkun().toString().startsWith("2") && saldo > 0) {
                rincianLiabilitas.put(akun.getNamaAkun(), saldo);
            }
        }
        System.out.println(rincianLiabilitas);
        return rincianLiabilitas;
    }

    public Map<String, Double> getEkuitasList() {
        Map<String, Double> rincianEkuitas = new LinkedHashMap<>();
        
        // Ambil nilai ekuitas akhir per 31 Desember dari PerubahanEkuitasService
        double ekuitasPer31Desember = perubahanEkuitasService.calculateEkuitasPer31Desember(
            perubahanEkuitasService.getModalAwal(),
            perubahanEkuitasService.totalKenaikanEkuitas(
                perubahanEkuitasService.penambahanEkuitas(),
                perubahanEkuitasService.getLabaBersih()) - perubahanEkuitasService.getPrive()
        );

        rincianEkuitas.put("Ekuitas per 31 Desember", ekuitasPer31Desember);
        return rincianEkuitas;
    }

    public double getTotalAset() {
        return getAsetList().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalLiabilitas() {
        return getLiabilitasList().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalEkuitas() {
        return getEkuitasList().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalKewajiban() {
        return getTotalLiabilitas() + getTotalEkuitas();
    }
}
