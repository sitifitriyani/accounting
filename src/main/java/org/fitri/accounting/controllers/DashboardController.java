package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Login;
// import org.fitri.accounting.services.LabaRugiService;
import org.fitri.accounting.services.LoginService;
import org.fitri.accounting.services.ProfileService;
// import org.jfree.chart.ChartFactory;
// import org.jfree.chart.ChartUtils;
// import org.jfree.chart.JFreeChart;
// import org.jfree.chart.plot.PlotOrientation;
// import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// import java.io.IOException;

@Controller
public class DashboardController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private LoginService loginService;

    // @Autowired
    // private LabaRugiService labaRugiService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Login login = loginService.getLogin();
        if (login == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("profile", profileService.getByLogin(login));
        return "dashboard";
    }

    // @GetMapping("/dashboard/chart")
    // public void generateChart(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //     DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    //     dataset.addValue(labaRugiService.getTotalPendapatan(), "Pendapatan", "Total Pendapatan");
    //     dataset.addValue(labaRugiService.getTotalBeban(), "Beban", "Total Beban");

    //     JFreeChart barChart = ChartFactory.createBarChart(
    //             "Pendapatan dan Beban",
    //             "Kategori",
    //             "Nilai",
    //             dataset,
    //             PlotOrientation.VERTICAL,
    //             true, true, false);

    //     response.setContentType("image/png");
    //     ChartUtils.writeChartAsPNG(response.getOutputStream(), barChart, 800, 600);
    // }
}
