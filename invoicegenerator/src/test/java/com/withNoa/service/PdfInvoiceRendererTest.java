package com.withNoa.service;

import com.withNoa.entity.Contact;
import com.withNoa.entity.Invoice;
import com.withNoa.entity.enums.Currency;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class PdfInvoiceRendererTest {

    private final PdfInvoiceRenderer renderer = new PdfInvoiceRenderer();

    private Invoice sampleInvoice() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("2025-00501");
        invoice.setDescription("Consulting services");
        invoice.setUnits(10);
        invoice.setRate(100.0);
        invoice.setSubtotal(1000.0);
        invoice.setVat(210.0);
        invoice.setTotal(1210.0);
        invoice.setNote("Thank you!");
        invoice.setCurrency(Currency.EUR);
        return invoice;
    }

    private Contact sampleContact() {
        Contact contact = new Contact();
        contact.setFirstName("Noa");
        contact.setLastName("Goldin");
        contact.setEmail("noa@withnoa.com");
        contact.setAddress("Oude Vest 211, 2312XZ Leiden");
        return contact;
    }

    private String extractTextFromPdf(byte[] pdfBytes) throws Exception {
        try (PDDocument pdfDoc = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdfDoc);
        }
    }

    @Test
    void whenRenderCalledWithValidInvoice_thenPdfIsGeneratedSuccessfully() throws Exception {
        // Arrange
        Invoice invoice = sampleInvoice();
        Contact contact = sampleContact();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Act
        renderer.render(document, writer, invoice, contact);
        document.close();

        // Assert
        byte[] pdfBytes = baos.toByteArray();
        assertTrue(pdfBytes.length > 0, "Generated PDF should not be empty");
    }

    @Test
    void whenRenderCalled_thenInvoiceNumberIsIncludedInPdf() throws Exception {
        // Arrange
        Invoice invoice = sampleInvoice();
        Contact contact = sampleContact();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        renderer.render(document, writer, invoice, contact);
        document.close();

        // Assert
        String text = extractTextFromPdf(baos.toByteArray());
        assertTrue(text.contains("2025-00501"), "Invoice number should appear in PDF");
    }

    @Test
    void whenRenderCalled_thenClientDetailsAreIncludedInPdf() throws Exception {
        Invoice invoice = sampleInvoice();
        Contact contact = sampleContact();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        renderer.render(document, writer, invoice, contact);
        document.close();

        String text = extractTextFromPdf(baos.toByteArray());
        assertTrue(text.contains("Noa Goldin"), "Client name should appear in PDF");
        assertTrue(text.contains("noa@withnoa.com"), "Client email should appear in PDF");
        assertTrue(text.contains("Oude Vest 211"), "Client address should appear in PDF");
    }

    @Test
    void whenRenderCalled_thenFinancialBreakdownIsCorrect() throws Exception {
        Invoice invoice = sampleInvoice();
        Contact contact = sampleContact();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        renderer.render(document, writer, invoice, contact);
        document.close();

        String text = extractTextFromPdf(baos.toByteArray());
        assertTrue(text.contains("1000.00"), "Subtotal should appear in PDF");
        assertTrue(text.contains("210.00"), "VAT should appear in PDF");
        assertTrue(text.contains("1210.00"), "Total should appear in PDF");
    }

    @Test
    void whenRenderCalled_thenNoteIsIncludedInPdf() throws Exception {
        Invoice invoice = sampleInvoice();
        Contact contact = sampleContact();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        renderer.render(document, writer, invoice, contact);
        document.close();

        String text = extractTextFromPdf(baos.toByteArray());
        assertTrue(text.contains("Thank you!"), "Note should appear in PDF");
    }
}
