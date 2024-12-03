package org.fitri.accounting.controllers;

import javax.validation.Valid;

import org.fitri.accounting.models.Transaksi;
import org.fitri.accounting.services.AkunService;
import org.fitri.accounting.services.TransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transaksi")
public class TransaksiController {
    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private AkunService akunService;

    @GetMapping
    public String getAllTransaksi(Model model) {
        model.addAttribute("transaksi", transaksiService.getAllTransaksi());
        return "transaksi";
    }

    @GetMapping("/add-transaksi")
    public String showAddTransaksiForm(Model model) {
        Transaksi transaksi = new Transaksi();
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("akunList", akunService.getAllAkun());
        return "add-transaksi";
    }

    @PostMapping("/save")
    public String saveTransaksi(@ModelAttribute Transaksi transaksi) {
        transaksiService.saveTransaksi(transaksi);
        return "redirect:/transaksi"; 
    }

    @GetMapping("/edit/{id}")
    public String editTransaksi(@PathVariable Long id, Model model) {
        Transaksi transaksi = transaksiService.getTransaksiById(id);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("akunList", akunService.getAllAkun());

        return "add-transaksi";
    }

    @PostMapping("/edit/{id}")
    public String updateTransaksi(@PathVariable Long id, @Valid @ModelAttribute Transaksi transaksi) {
        transaksi.setId(id);
        transaksiService.saveTransaksi(transaksi);
        return "redirect:/transaksi";
    }

    @GetMapping("/delete/{id}")
    public String deleteTransaksi(@PathVariable Long id) {
        transaksiService.deleteTransaksi(id);
        return "redirect:/transaksi";
    }

    @GetMapping("/search")
    public String searchTransaksi(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("transaksi", transaksiService.searchTransaksi(keyword));
        return "transaksi";
    }

}
