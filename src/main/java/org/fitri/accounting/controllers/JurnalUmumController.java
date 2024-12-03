package org.fitri.accounting.controllers;

import org.fitri.accounting.models.Transaksi;
import org.fitri.accounting.services.TransaksiService;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.Cell;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/jurnal-umum")
public class JurnalUmumController {

    @Autowired
    private TransaksiService transaksiService;

    @GetMapping
    public String listJurnalUmum(Model model) {
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();

        // Sort transaksiList by date
        transaksiList.sort(Comparator.comparing(Transaksi::getDate, Comparator.nullsLast(Comparator.naturalOrder())));  
        // transaksiList.sort(Comparator.comparing(Transaksi::getDate));  
        // Hitung total nominal untuk debit dan kredit
        double totalDebit = transaksiList.stream()
            .mapToDouble(t -> t.getDebitAkun() != null ? t.getNominal() : 0)
            .sum();
        
        double totalKredit = transaksiList.stream()
            .mapToDouble(t -> t.getKreditAkun() != null ? t.getNominal() : 0)
            .sum();
        
        model.addAttribute("jurnalUmum", transaksiList);
        model.addAttribute("totalDebit", totalDebit); // Kirim total debit ke template
        model.addAttribute("totalKredit", totalKredit);
        return "jurnal-umum"; // Kembalikan nama template untuk halaman jurnal umum
    }

    @PostMapping("/print-pdf")
    public ResponseEntity<byte[]> printPdf(@RequestParam("startDate") String startDateStr,
                                           @RequestParam("endDate") String endDateStr) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        List<Transaksi> filteredTransaksiList = transaksiService.getAllTransaksi().stream()
            .filter(t -> {
                LocalDateTime transactionDateTime = t.getDate(); // Assuming t.getDate() returns LocalDateTime
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
                return transactionDateTime != null && !transactionDateTime.isBefore(startDateTime) && !transactionDateTime.isAfter(endDateTime);
            })
            .collect(Collectors.toList());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

        document.add(new Paragraph("Jurnal Umum")
        .setFontSize(14)
        .setTextAlignment(TextAlignment.CENTER)
        .setMarginBottom(20));
        Table table = new Table(new float[]{2, 4, 2, 2, 2});

        table.addHeaderCell(new Cell().add(new Paragraph("Tanggal")));
        table.addHeaderCell(new Cell().add(new Paragraph("Keterangan")));
        table.addHeaderCell(new Cell().add(new Paragraph("Ref")));
        table.addHeaderCell(new Cell().add(new Paragraph("Debit")));
        table.addHeaderCell(new Cell().add(new Paragraph("Kredit")));

        double totalDebit = 0;
        double totalKredit = 0;

        for (Transaksi transaksi : filteredTransaksiList) {
            table.addCell(new Cell().add(new Paragraph(transaksi.getDate().toString())));
            table.addCell(new Cell().add(new Paragraph(transaksi.getDebitAkun().getNamaAkun() + "\n" + transaksi.getKreditAkun().getNamaAkun())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(transaksi.getDebitAkun().getKodeAkun() + "\n" + transaksi.getKreditAkun().getKodeAkun()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(transaksi.getNominal()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(transaksi.getNominal()))));
             // Accumulate totals
        if (transaksi.getDebitAkun() != null) {
            totalDebit += transaksi.getNominal();
        }
        if (transaksi.getKreditAkun() != null) {
            totalKredit += transaksi.getNominal();
        }
        }
        table.addCell(new Cell(1, 3).add(new Paragraph("Total").setBold()));
    table.addCell(new Cell().add(new Paragraph(String.valueOf(totalDebit))));
    table.addCell(new Cell().add(new Paragraph(String.valueOf(totalKredit))));

        document.add(table);
        document.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "jurnal-umum.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
