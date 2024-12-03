package org.fitri.accounting.controllers;

import org.fitri.accounting.services.PosisiKeuanganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posisi-keuangan")
public class PosisiKeuanganController {

    @Autowired
    private PosisiKeuanganService posisiKeuanganService;

    @GetMapping
    public String getPosisiKeuangan(Model model) {
        model.addAttribute("asetList", posisiKeuanganService.getAsetList());
        model.addAttribute("liabilitasList", posisiKeuanganService.getLiabilitasList());
        model.addAttribute("ekuitasList", posisiKeuanganService.getEkuitasList());

        model.addAttribute("totalAset", posisiKeuanganService.getTotalAset());
        model.addAttribute("totalLiabilitas", posisiKeuanganService.getTotalLiabilitas());
        model.addAttribute("totalEkuitas", posisiKeuanganService.getTotalEkuitas());
        model.addAttribute("totalKewajiban", posisiKeuanganService.getTotalKewajiban());

        return "posisi-keuangan";
    }
}
