package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Login;
// import org.fitri.accounting.services.LabaRugiService;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.ProfileService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DashboardController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Login login = loginService.getLogin();
        if (login == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getByLogin(login));
        return "dashboard";
    }

    @GetMapping("/dashboard/chart")
    @ResponseBody
    public void generateChart(HttpServletResponse response) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(12, "Pendapatan", "January");
        dataset.addValue(19, "Pendapatan", "February");
        dataset.addValue(3, "Pendapatan", "March");
        dataset.addValue(5, "Pendapatan", "April");
        dataset.addValue(2, "Pendapatan", "May");
        dataset.addValue(3, "Pendapatan", "June");
        dataset.addValue(7, "Pendapatan", "July");

        dataset.addValue(2, "Beban", "January");
        dataset.addValue(3, "Beban", "February");
        dataset.addValue(20, "Beban", "March");
        dataset.addValue(5, "Beban", "April");
        dataset.addValue(1, "Beban", "May");
        dataset.addValue(4, "Beban", "June");
        dataset.addValue(10, "Beban", "July");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Pendapatan dan Beban",
                "Bulan",
                "Jumlah",
                dataset
        );

        response.setContentType("image/png");
        ChartUtils.writeChartAsPNG(response.getOutputStream(), barChart, 800, 600);
    }

}
