package org.fitri.accounting.controllers;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.fitri.accounting.services.PosisiKeuanganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    @PostMapping("/print-pdf")
    public ResponseEntity<byte[]> printPdf() throws IOException {
        // Map<String, Object> data = posisiKeuanganService.getPosisiKeuanganData();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        Document document = new Document(new PdfDocument(writer));

        // Add title
        document.add(new Paragraph("Laporan Posisi Keuangan")
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

        // Add Aset
        table.addCell(new Cell(1, 2).add(new Paragraph("Aset")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph("asetList")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getAsetList()))).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph("Total Aset")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getTotalAset()))).setBackgroundColor(cellColor));

        // Add Liabilitas
        table.addCell(new Cell(1, 2).add(new Paragraph("Liabilitas")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph("liabilitasList")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getLiabilitasList()))).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph("Total Liabilitas")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getTotalLiabilitas()))).setBackgroundColor(cellColor));

        // Add Ekuitas
        table.addCell(new Cell(1, 2).add(new Paragraph("Ekuitas")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph("ekuitasList")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getEkuitasList()))).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph("Total Ekuitas")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getTotalEkuitas()))).setBackgroundColor(cellColor));

        // Add Total Kewajiban
        table.addCell(new Cell().add(new Paragraph("Total Kewajiban")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(posisiKeuanganService.getTotalKewajiban()))).setBackgroundColor(headerColor));

        document.add(table);
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "posisi-keuangan.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
