package org.fitri.accounting.controllers;

import java.util.List;

import org.fitri.accounting.models.Akun;
import org.fitri.accounting.services.AkunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/akun")
public class AkunController {
    @Autowired
    private AkunService akunService;

    @GetMapping
    public String getAllAkun(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Akun> akunList;
        if (category == null || category.isEmpty()) {
            akunList = akunService.getAllAkun();
        } else {
            akunList = akunService.getAkunByCategory(category);
        }
        model.addAttribute("akun", akunList);
        model.addAttribute("selectedCategory", category);
        return "akun";
    }

    @GetMapping("/add-akun")
    public String addAkun(Model model) {
        model.addAttribute("akun", new Akun());
        return "add-akun";
    }

    @PostMapping("/add-akun")
    public String showAkun(@ModelAttribute Akun akun, Model model) {
        try {
            akunService.saveAkun(akun);
            return "redirect:/akun";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "add-akun"; 
        }
    }

    @GetMapping("/edit/{id}")
    public String editAkun(@PathVariable Long id, Model model) {
        Akun akun = akunService.getAkunById(id);
        model.addAttribute("akun", akun);
        return "add-akun";
    }

    @PostMapping("/edit/{id}")
    public String updateAkun(@PathVariable Long id, @ModelAttribute Akun akun, Model model) {
        akunService.updateAkun(id, akun);
        return "redirect:/akun";
    }

    @GetMapping("/delete/{id}")
    public String deleteAkun(@PathVariable Long id) {
        akunService.deleteAkun(id);
        return "redirect:/akun";
    }

    @GetMapping("/search")
    public String searchAkun(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("akun", akunService.searchAkun(keyword));
        return "akun";
    }
}
