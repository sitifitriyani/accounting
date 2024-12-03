package org.fitri.accounting.controllers;

import org.fitri.accounting.services.LabaRugiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/laba-rugi")
public class LabaRugiController {
    @Autowired
    private LabaRugiService labaRugiService;

    @GetMapping
    public String getLaporanLabaRugi(Model model) {
        model.addAttribute("totalPendapatan", labaRugiService.getTotalPendapatan());
        model.addAttribute("rincianBeban", labaRugiService.getRincianBeban());
        model.addAttribute("totalBeban", labaRugiService.getTotalBeban());
        model.addAttribute("labaRugiBersih", labaRugiService.getLabaRugiBersih());
        return "laba-rugi";
    }

    @PostMapping("/print-pdf")
    public ResponseEntity<byte[]> printPdf() throws IOException {
        Map<String, Object> data = labaRugiService.getLabaRugiData();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        // Add title
        document.add(new Paragraph("Laporan Laba Rugi")
            .setFontSize(14)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20));

        // Define a table with 2 columns
        Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Define colors
        Color headerColor = new DeviceRgb(63, 81, 181);
        Color cellColor = new DeviceRgb(224, 224, 224);

        // Add table headers
        table.addHeaderCell(new Cell().add(new Paragraph("Jenis Akun")).setBackgroundColor(headerColor));
        table.addHeaderCell(new Cell().add(new Paragraph("Nominal")).setBackgroundColor(headerColor));

        // Add total pendapatan
        table.addCell(new Cell().add(new Paragraph("Total Pendapatan")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(data.get("totalPendapatan").toString())).setBackgroundColor(cellColor));

        // Add rincian beban
        table.addCell(new Cell(1, 2).add(new Paragraph("Rincian Beban")).setBackgroundColor(headerColor));
        Map<String, Double> rincianBeban = (Map<String, Double>) data.get("rincianBeban");
        for (Map.Entry<String, Double> entry : rincianBeban.entrySet()) {
            table.addCell(new Cell().add(new Paragraph(entry.getKey())).setBackgroundColor(cellColor));
            table.addCell(new Cell().add(new Paragraph(entry.getValue().toString())).setBackgroundColor(cellColor));
        }

        // Add total beban
        table.addCell(new Cell().add(new Paragraph("Total Beban")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(data.get("totalBeban").toString())).setBackgroundColor(cellColor));

        // Add laba/rugi bersih
        table.addCell(new Cell().add(new Paragraph("Laba/Rugi Bersih")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph(data.get("labaRugiBersih").toString())).setBackgroundColor(headerColor));

        document.add(table);
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "laba-rugi.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
