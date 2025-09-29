package com.withNoa.service;

import com.withNoa.entity.Contact;
import com.withNoa.entity.Invoice;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.IOException;

@Component
public class PdfInvoiceRenderer {

    public void render(Document document, PdfWriter writer, Invoice invoice, Contact contact) throws DocumentException, IOException {
        // 🎨 Background
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.setColorFill(new Color(230, 230, 230));
        canvas.rectangle(document.left(), document.bottom(), document.right() - document.left(), document.top() - document.bottom());
        canvas.fill();

        // 🖋 Fonts
        BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font normalFont = new Font(baseFont, 12);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font boldFont = new Font(baseFont, 12, Font.BOLD);
        Font totalFont = new Font(baseFont, 12, Font.BOLD);
        Font withNoaFont = new Font(baseFont, 22, Font.BOLDITALIC, new Color(0, 100, 0));
        Font numberFont = new Font(baseFont, 18, Font.BOLD);
        Color headerBg = new Color(200, 230, 201);
        Color totalBg = new Color(150, 200, 150);
        String symbol = invoice.getCurrency() != null ? invoice.getCurrency().getSymbol() : "€";

        document.add(new Paragraph(" "));

        // 🧾 Title
        Paragraph title = new Paragraph();
        title.add(new Chunk("With Noa ", withNoaFont));
        title.add(new Chunk("Invoice — No. " + invoice.getInvoiceNumber(), numberFont));
        document.add(title);

        // 🔗 Info Table
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(15f);
        infoTable.setWidths(new float[]{1f, 1f});

        PdfPCell noaCell = new PdfPCell();
        noaCell.setBorder(Rectangle.NO_BORDER);

// 🧾 Business name
        noaCell.addElement(new Paragraph("With Noa", normalFont));

// 📧 Email link
        Anchor emailLink = new Anchor("noa@withnoa.com", normalFont);
        emailLink.setReference("mailto:noa@withnoa.com");
        Phrase emailPhrase = new Phrase();
        emailPhrase.add(emailLink);
        noaCell.addElement(emailPhrase);

// 🌐 Website link
        Anchor websiteLink = new Anchor("www.withnoa.com", normalFont);
        websiteLink.setReference("https://www.withnoa.com");
        Phrase websitePhrase = new Phrase();
        websitePhrase.add(websiteLink);
        noaCell.addElement(websitePhrase);

// 🏠 Address
        noaCell.addElement(new Paragraph("Address: Oude Vest 211, 2312XZ Leiden", normalFont));


        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.addElement(new Paragraph("Client: " + contact.getFirstName() + " " + contact.getLastName(), normalFont));
        clientCell.addElement(new Paragraph("Email: " + contact.getEmail(), normalFont));
        clientCell.addElement(new Paragraph("Address: " + contact.getAddress(), normalFont));

        infoTable.addCell(noaCell);
        infoTable.addCell(clientCell);
        document.add(infoTable);

        Paragraph spacer = new Paragraph(" ");
        spacer.setSpacingBefore(20f);
        document.add(spacer);

        // 📊 Invoice Table
        PdfPTable invoiceTable = new PdfPTable(4);
        invoiceTable.setWidthPercentage(100);
        invoiceTable.setSpacingBefore(10f);
        invoiceTable.setWidths(new float[]{2f, 0.5f, 1f, 1f});

        String[] headers = {"Description", "Units", "Cost per unit", "Amount"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerBg);
            invoiceTable.addCell(cell);
        }

        PdfPCell descCell = new PdfPCell(new Phrase(invoice.getDescription(), normalFont));
        descCell.setMinimumHeight(100f);

        PdfPCell unitsCell = new PdfPCell(new Phrase(String.valueOf(invoice.getUnits()), normalFont));
        PdfPCell costCell = new PdfPCell(new Phrase(symbol + String.format("%.2f", invoice.getRate()), normalFont));
        PdfPCell amountCell = new PdfPCell(new Phrase(symbol + String.format("%.2f", invoice.getSubtotal()), normalFont));

        unitsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        costCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        invoiceTable.addCell(descCell);
        invoiceTable.addCell(unitsCell);
        invoiceTable.addCell(costCell);
        invoiceTable.addCell(amountCell);
        document.add(invoiceTable);

        Paragraph spacer30 = new Paragraph(" ");
        spacer.setSpacingBefore(30f);
        document.add(spacer);

        // 📋 Breakdown Table
        PdfPTable breakdownTable = new PdfPTable(2);
        breakdownTable.setWidthPercentage(100);
        breakdownTable.setSpacingBefore(10f);
        breakdownTable.setWidths(new float[]{3.5f, 1f});

        addRow(breakdownTable, "Subtotal", symbol + String.format("%.2f", invoice.getSubtotal()), normalFont);

        addRow(breakdownTable, "VAT Outside EU", "0%", normalFont);
        addRow(breakdownTable, "VAT Value", symbol + String.format("%.2f", invoice.getVat()), normalFont);
        addRow(breakdownTable, "Other", symbol + "0.00", normalFont);
        document.add(breakdownTable);

        // 💳 Payment Instructions
        Paragraph payment = new Paragraph();
        payment.setSpacingBefore(10f);
        payment.add(new Phrase("Payment Instructions: Transfer to NL30INGB0104244798 of ", normalFont));
        payment.add(new Phrase("Noa Goldin", boldFont));
        document.add(payment);

        // 💰 Total Table
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setSpacingBefore(10f);
        totalTable.setWidths(new float[]{3.5f, 1f});

        PdfPCell totalLabel = new PdfPCell(new Phrase("Total", totalFont));
        PdfPCell totalValue = new PdfPCell(new Phrase(symbol + String.format("%.2f", invoice.getTotal()), totalFont));

        totalLabel.setBackgroundColor(totalBg);
        totalValue.setBackgroundColor(totalBg);
        totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell(totalLabel);
        totalTable.addCell(totalValue);
        document.add(totalTable);

        // 🙏 Closing Note
        Paragraph spacer100 = new Paragraph(" ");
        spacer.setSpacingBefore(100f);
        document.add(spacer);
        document.add(new Paragraph(invoice.getNote(), normalFont));
    }

    private void addRow(PdfPTable table, String label, String value, Font font) {
        table.addCell(new Phrase(label, font));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }
}

