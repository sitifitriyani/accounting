package org.fitri.accounting.controllers;

import java.util.Map;
import java.util.List;

import org.fitri.accounting.models.Akun;
import org.fitri.accounting.services.BukuBesarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/buku-besar")
public class BukuBesarController {
    @Autowired
    private BukuBesarService bukuBesarService;

    @GetMapping
    public String getAllBukuBesar(Model model) {
        Map<Akun, List<Map<String, Object>>> bukuBesar = bukuBesarService.getBukuBesar();
        model.addAttribute("bukuBesar", bukuBesar);
        return "buku-besar";
    }
}
