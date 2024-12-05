package org.fitri.accounting.services;

import java.util.List;
import java.util.stream.Collectors;

import org.fitri.accounting.models.Akun;
import org.fitri.accounting.models.Transaksi;
import org.fitri.accounting.repositories.AkunRepository;
import org.fitri.accounting.repositories.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AkunService {
      @Autowired
    private AkunRepository akunRepository;

    @Autowired
    private TransaksiRepository transaksiRepository;

    public List<Akun> getAllAkun() {
        return akunRepository.findAll();
    }
    public Akun saveAkun(Akun akun){
        String kodeAkun = String.valueOf(akun.getKodeAkun());
        if (kodeAkun.length() < 2 || kodeAkun.length() > 4) {
            throw new IllegalArgumentException("Kode Akun harus terdiri dari 2 hingga 4 digit.");
        }
        if (akunRepository.existsByKodeAkun(akun.getKodeAkun()) || akunRepository.existsByNamaAkun(akun.getNamaAkun())) {
            throw new IllegalArgumentException("Akun sudah ada !!!");
        }
        if (containsDigit(akun.getNamaAkun())) {
            throw new IllegalArgumentException("Nama Akun tidak boleh mengandung angka.");
        }
        if (containsThreeConsecutiveIdenticalLetters(akun.getNamaAkun())) {
            throw new IllegalArgumentException("Nama Akun tidak boleh mengandung tiga huruf yang sama berjejer.");
        }
        return akunRepository.save(akun);    
    }

    public Akun getAkunById(Long id){
        return akunRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("akun tidak ditemukan !!!"));

    }

    public void updateAkun(Long id, Akun updatedAkun) {
        if (containsDigit(updatedAkun.getNamaAkun())) {
            throw new IllegalArgumentException("Nama Akun tidak boleh mengandung angka.");
        }
        if (containsThreeConsecutiveIdenticalLetters(updatedAkun.getNamaAkun())) {
            throw new IllegalArgumentException("Nama Akun tidak boleh mengandung tiga huruf yang sama berjejer.");
        }
        Akun existingAkun = getAkunById(id);
        existingAkun.setKodeAkun(updatedAkun.getKodeAkun());
        existingAkun.setNamaAkun(updatedAkun.getNamaAkun());
        existingAkun.setSaldoNormal(updatedAkun.getSaldoNormal());
        akunRepository.save(existingAkun);

        // Update related transactions
        List<Transaksi> transaksiList = transaksiRepository.findByAkunId(id);
        for (Transaksi transaksi : transaksiList) {
            if (transaksi.getDebitAkun().getId().equals(id)) {
                transaksi.setDebitAkun(existingAkun);
            }
            if (transaksi.getKreditAkun().getId().equals(id)) {
                transaksi.setKreditAkun(existingAkun);
            }
            transaksiRepository.save(transaksi);
        }
    }

    public void deleteAkun(Long id) {
        if (!akunRepository.existsById(id)) {
            throw new RuntimeException("Akun not found");
        }
        // Delete related transactions
        List<Transaksi> transaksiList = transaksiRepository.findByAkunId(id);
        transaksiRepository.deleteAll(transaksiList);

        // Delete the account
        akunRepository.deleteById(id);
    }
    public List<Akun> getAkunBySaldoNormal(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'getAkunBySaldoNormal'");
    }

    public List<Akun> searchAkun(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return akunRepository.findAll();
        }
        try {
            // Coba parsing keyword sebagai Integer untuk pencarian kode akun
            Integer kodeAkun = Integer.parseInt(keyword);
            return akunRepository.findByKodeAkun(kodeAkun);
        } catch (NumberFormatException e) {
            // Jika parsing gagal, lakukan pencarian berdasarkan nama akun
            return akunRepository.findByNamaAkunContainingIgnoreCase(keyword);
        }
    }

    public List<Akun> getAkunByCategory(String category) {
        return akunRepository.findAll().stream()
            .filter(akun -> {
                String kodeAkun = akun.getKodeAkun().toString();
                switch (category) { 
                    case "1":
                        return kodeAkun.startsWith("1");
                    case "2":
                        return kodeAkun.startsWith("2");
                    case "3":
                        return kodeAkun.startsWith("3");
                    case "4":
                        return kodeAkun.startsWith("4");
                    case "5":
                        return kodeAkun.startsWith("5");
                    default:
                        return false;  
                }
            })
            .collect(Collectors.toList());
    }

    private boolean containsDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsThreeConsecutiveIdenticalLetters(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            if (str.charAt(i) == str.charAt(i + 1) && str.charAt(i) == str.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}
