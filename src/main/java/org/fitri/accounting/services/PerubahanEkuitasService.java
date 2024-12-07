package org.fitri.accounting.services;

import java.util.Map;

import org.fitri.accounting.models.Akun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerubahanEkuitasService {

    @Autowired
    private BukuBesarService bukuBesarService;

    @Autowired
    private LabaRugiService labaRugiService;

    public double getModalAwal() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        return saldoBukuBesar.entrySet().stream()
                .filter(entry -> entry.getKey().getNamaAkun().equalsIgnoreCase("Modal"))
                .findFirst() 
                .map(Map.Entry::getValue) 
                .orElse(0.0); 
              
    }

    public double getLabaBersih() {
        return labaRugiService.getLabaRugiBersih();
    }

    public double penambahanEkuitas() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        double totalModal = saldoBukuBesar.entrySet().stream()
                .filter(entry -> entry.getKey().getNamaAkun().equalsIgnoreCase("Modal"))
                .skip(1)
                .mapToDouble(Map.Entry::getValue) 
                .sum();
                return totalModal;
    }
    public double getPrive() {
        Map<Akun, Double> saldoBukuBesar = bukuBesarService.getSaldoBukuBesar();
        return saldoBukuBesar.entrySet().stream()
                .filter(entry -> entry.getKey().getNamaAkun().equalsIgnoreCase("Prive"))
                .mapToDouble(Map.Entry::getValue)
                .sum();
    }
    public double totalKenaikanEkuitas(double penambahanEkuitas, double labaBersih) {
        return penambahanEkuitas - labaBersih;
    }
    public double kenaikanEkuitasSetelahPrive(double totalKenaikanEkuitas, double getPrive) {
        return totalKenaikanEkuitas - getPrive;
    }
    public double calculateEkuitasPer31Desember(double getModalAwal, double kenaikanEkuitasSetelahPrive) {
        return - getModalAwal + kenaikanEkuitasSetelahPrive;
    }
}
