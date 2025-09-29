package com.withNoa.controller;

import com.withNoa.entity.Contact;
import com.withNoa.entity.Invoice;
import com.withNoa.entity.enums.Currency;
import com.withNoa.repository.ContactRepository;
import com.withNoa.repository.InvoiceRepository;
import com.withNoa.service.InvoiceCalculationService;
import com.withNoa.service.PdfInvoiceRenderer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private ContactRepository contactRepository;

    @MockBean
    private InvoiceCalculationService invoiceCalculationService;

    @MockBean
    private PdfInvoiceRenderer pdfInvoiceRenderer;

    @WithMockUser
    @Test
    void whenExportInvoices_thenExcelIsGeneratedWithCorrectHeadersAndData() throws Exception {
        Contact contact = new Contact();
        contact.setFirstName("Noa");
        contact.setLastName("Goldin");
        contact.setEmail("noa@withnoa.com");
        contact.setAddress("Oude Vest 211");

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("2025-00501");
        invoice.setInvoiceDate(LocalDate.of(2025, 9, 24));
        invoice.setContactId(1L);
        invoice.setDescription("Consulting");
        invoice.setUnits(10);
        invoice.setRate(100.0);
        invoice.setSubtotal(1000.0);
        invoice.setVat(210.0);
        invoice.setTotal(1210.0);
        invoice.setNote("Thank you!");
        invoice.setCurrency(Currency.EUR);

        when(invoiceRepository.findAll()).thenReturn(Collections.singletonList(invoice));
        when(contactRepository.findById(1L)).thenReturn(java.util.Optional.of(contact));

        byte[] responseBytes = mockMvc.perform(get("/invoices/export"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=invoice-history.xlsx"))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        Sheet sheet = WorkbookFactory.create(new ByteArrayInputStream(responseBytes)).getSheet("Invoice History");
        Row header = sheet.getRow(0);
        Row data = sheet.getRow(1);

        assert header.getCell(0).getStringCellValue().equals("Number");
        assert data.getCell(0).getStringCellValue().equals("2025-00501");
        assert data.getCell(2).getStringCellValue().equals("Noa Goldin");
        assert data.getCell(10).getNumericCellValue() == 1210.0;
    }

    @WithMockUser
    @Test
    void whenShowInvoiceHistory_thenModelContainsInvoices() throws Exception {
        when(invoiceRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/invoices/history"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("invoices"))
                .andExpect(view().name("invoice-history"));
    }

    @WithMockUser
    @Test
    void whenShowInvoiceForm_thenModelContainsContactAndInvoice() throws Exception {
        Contact contact = new Contact();
        contact.setId(1L);
        contact.setFirstName("Noa");

        when(contactRepository.findById(1L)).thenReturn(java.util.Optional.of(contact));

        mockMvc.perform(get("/invoices/new").param("contactId", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attributeExists("invoice"))
                .andExpect(view().name("invoice-form"));
    }
}
