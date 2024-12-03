package org.fitri.accounting.controllers;

import org.fitri.accounting.services.PerubahanEkuitasService;
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

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller 
@RequestMapping("/perubahan-ekuitas")
public class PerubahanEkuitasController {
    @Autowired
    private PerubahanEkuitasService perubahanEkuitasService;

    @GetMapping
    public String getPerubahanEkuitas(Model model) {
        double modalAwal = perubahanEkuitasService.getModalAwal();
        double labaBersih = perubahanEkuitasService.getLabaBersih();
        double penambahanModal = perubahanEkuitasService.penambahanEkuitas();
        double prive = perubahanEkuitasService.getPrive();

        double kenaikanEkuitas = perubahanEkuitasService.totalKenaikanEkuitas(penambahanModal, labaBersih);
        // double totalKenaikan = kenaikanEkuitas;
        double kenaikanEkuitasSetelahPrive = kenaikanEkuitas - prive;
        double ekuitasPer31Desember = perubahanEkuitasService.calculateEkuitasPer31Desember(modalAwal, kenaikanEkuitasSetelahPrive);

        // Menambahkan data ke model untuk digunakan di HTML
        model.addAttribute("ekuitasPemilik", modalAwal);
        model.addAttribute("kenaikanEkuitas", penambahanModal);
        model.addAttribute("labaBersih", labaBersih);
        model.addAttribute("totalKenaikan", kenaikanEkuitas);
        model.addAttribute("prive", prive);
        model.addAttribute("kenaikanEkuitasSetelahPrive", kenaikanEkuitasSetelahPrive);
        model.addAttribute("ekuitasPer31Desember", ekuitasPer31Desember);

        // Mengembalikan nama file HTML
        return "perubahan-ekuitas";
    }

    @PostMapping("/print-pdf")
    public ResponseEntity<byte[]> printPdf() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        // Add title
        document.add(new Paragraph("Laporan Perubahan Ekuitas")
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

        // Add data to the table
        table.addCell(new Cell().add(new Paragraph("Ekuitas Pemilik")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.getModalAwal()))).setBackgroundColor(cellColor));

        table.addCell(new Cell().add(new Paragraph("Kenaikan Ekuitas")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.penambahanEkuitas()))).setBackgroundColor(cellColor));

        table.addCell(new Cell().add(new Paragraph("Laba Bersih")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.getLabaBersih()))).setBackgroundColor(cellColor));

        table.addCell(new Cell().add(new Paragraph("Total Kenaikan")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.totalKenaikanEkuitas(
            perubahanEkuitasService.penambahanEkuitas(), perubahanEkuitasService.getLabaBersih())))).setBackgroundColor(headerColor));

        table.addCell(new Cell().add(new Paragraph("Penurunan (Prive)")).setBackgroundColor(cellColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.getPrive()))).setBackgroundColor(cellColor));

        table.addCell(new Cell().add(new Paragraph("Kenaikan Ekuitas Setelah Prive")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.totalKenaikanEkuitas(
            perubahanEkuitasService.penambahanEkuitas(), perubahanEkuitasService.getLabaBersih()) - perubahanEkuitasService.getPrive()))).setBackgroundColor(headerColor));

        table.addCell(new Cell().add(new Paragraph("Ekuitas per 31 Desember")).setBackgroundColor(headerColor));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(perubahanEkuitasService.calculateEkuitasPer31Desember(
            perubahanEkuitasService.getModalAwal(), perubahanEkuitasService.totalKenaikanEkuitas(
            perubahanEkuitasService.penambahanEkuitas(), perubahanEkuitasService.getLabaBersih()) - perubahanEkuitasService.getPrive())))).setBackgroundColor(headerColor));

        document.add(table);
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "perubahan-ekuitas.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
