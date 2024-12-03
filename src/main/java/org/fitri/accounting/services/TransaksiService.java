package org.fitri.accounting.services;

import java.util.List;

import org.fitri.accounting.models.Transaksi;
import org.fitri.accounting.repositories.TransaksiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TransaksiService {
      @Autowired
    private TransaksiRepository transaksiRepository;
    
    public List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAll();
    }
    public Transaksi saveTransaksi(Transaksi transaksi){
        return transaksiRepository.save(transaksi);
    }
    public List<Transaksi> getTransaksiByAkunId(Long akunId) {
        return transaksiRepository.findByAkunId(akunId);
    }

    public Transaksi getTransaksiById(Long id){
        return transaksiRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Transaksi not found"));

    }
    public void deleteTransaksi(Long id) {
        transaksiRepository.deleteById(id);
    }
    public double calculateSaldoAkun(Long akunId) {
        List<Transaksi> transaksiList = transaksiRepository.findByAkunId(akunId);
        double saldo = 0.0;

        for (Transaksi transaksi : transaksiList) {
            if (transaksi.getDebitAkun() != null && transaksi.getDebitAkun().getId().equals(akunId)) {
                saldo += transaksi.getNominal(); // Tambah nominal ke saldo jika debit
            } else if (transaksi.getKreditAkun() != null && transaksi.getKreditAkun().getId().equals(akunId)) {
                saldo -= transaksi.getNominal(); // Kurangi nominal dari saldo jika kredit
            }
        }

        return saldo;
    }

    public List<Transaksi> searchTransaksi(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return transaksiRepository.findAll();
        }
        try {
            // Coba parsing keyword sebagai Double untuk pencarian nominal
            Double nominal = Double.parseDouble(keyword);
            return transaksiRepository.findByNominal(nominal);
        } catch (NumberFormatException e) {
            // Jika parsing gagal, lakukan pencarian berdasarkan catatan
            return transaksiRepository.findByNotesContainingIgnoreCase(keyword);
        }
    }

}
