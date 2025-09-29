package com.withNoa.controller;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.withNoa.entity.Contact;
import com.withNoa.entity.Invoice;
import com.withNoa.entity.enums.Currency;
import com.withNoa.repository.ContactRepository;
import com.withNoa.repository.InvoiceRepository;
import com.withNoa.service.InvoiceCalculationService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.withNoa.service.PdfInvoiceRenderer;


@Controller
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceCalculationService invoiceCalculationService;
    @Autowired
    private PdfInvoiceRenderer pdfInvoiceRenderer;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // 🔢 Generate invoice number
    private String generateInvoiceNumber() {
        int currentYear = LocalDate.now().getYear();
        String prefix = currentYear + "-";

        List<Invoice> invoicesThisYear = invoiceRepository.findByInvoiceNumberStartingWith(prefix);
        int maxCounter = invoicesThisYear.stream()
                .map(inv -> inv.getInvoiceNumber().split("-"))
                .filter(parts -> parts.length == 2)
                .mapToInt(parts -> {
                    try {
                        return Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(500); // Start from 00501

        int nextCounter = maxCounter + 1;
        return String.format("%d-%05d", currentYear, nextCounter);
    }

    // 🧾 Show invoice form
    @GetMapping("/new")
    public String showInvoiceForm(@RequestParam Long contactId, Model model) {
        Contact contact = contactRepository.findById(contactId).orElseThrow();
        Invoice invoice = new Invoice();

        invoice.setContactId(contact.getId());
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setPaymentInstructions("Transfer to NL30INGB0104244798 of Noa Goldin");
        invoice.setNote("Thank you very much for your trust in me!");
        invoice.setCurrency(Currency.EUR);

        model.addAttribute("contact", contact);
        model.addAttribute("invoice", invoice);

        return "invoice-form";
    }

    // 💾 Save invoice and generate PDF
    @PostMapping("/save")
    public void saveInvoice(@ModelAttribute Invoice invoice, HttpServletResponse response) throws IOException {
        Contact contact = contactRepository.findById(invoice.getContactId()).orElseThrow();
        invoice.setContactId(contact.getId());
        invoiceCalculationService.applyCalculations(invoice);
        invoiceRepository.save(invoice);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        pdfInvoiceRenderer.render(document, writer, invoice, contact);
        document.close();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf");
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }

    @GetMapping("/history")
public String showInvoiceHistory(Model model) {
    List<Invoice> invoices = invoiceRepository.findAll();

    for (Invoice invoice : invoices) {
        Long cid = invoice.getContactId();
        if (cid != null) {
            Contact contact = contactRepository.findById(cid).orElse(null);
            if (contact == null) {
                System.out.println("⚠️ No contact found for invoice ID " + invoice.getId() + " with contactId " + cid);
            }
            invoice.setContact(contact);
        }
    }

    model.addAttribute("invoices", invoices);
    return "invoice-history";
 }
    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Invoice> invoices = invoiceRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Invoice History");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] columns = {
                "Number", "Date", "Name", "Email", "Address", "Description",
                "Units", "Rate", "Subtotal", "VAT", "Total", "Note"
        };

        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Invoice invoice : invoices) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

            Contact contact = contactRepository.findById(invoice.getContactId()).orElse(null);

            row.createCell(0).setCellValue(invoice.getInvoiceNumber());
            row.createCell(1).setCellValue(invoice.getInvoiceDate().toString());
            row.createCell(2).setCellValue(contact != null ? contact.getFirstName() + " " + contact.getLastName() : "Unknown");
            row.createCell(3).setCellValue(contact != null ? contact.getEmail() : "Unknown");
            row.createCell(4).setCellValue(contact != null ? contact.getAddress() : "Unknown");
            row.createCell(5).setCellValue(invoice.getDescription());
            row.createCell(6).setCellValue(invoice.getUnits());
            row.createCell(7).setCellValue(invoice.getRate());
            row.createCell(8).setCellValue(invoice.getSubtotal());
            row.createCell(9).setCellValue(invoice.getVat());
            row.createCell(10).setCellValue(invoice.getTotal());
            row.createCell(11).setCellValue(invoice.getNote());
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=invoice-history.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
