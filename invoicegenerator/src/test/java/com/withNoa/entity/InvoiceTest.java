package com.withNoa.entity;

import com.withNoa.entity.enums.Currency;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    @Test
    void whenInvoiceIsCreated_thenFieldsCanBeSetAndRetrieved() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("2025-00501");
        invoice.setInvoiceDate(LocalDate.of(2025, 9, 24));
        invoice.setDescription("Consulting services");
        invoice.setUnits(10);
        invoice.setRate(100.0);
        invoice.setSubtotal(1000.0);
        invoice.setVat(210.0);
        invoice.setTotal(1210.0);
        invoice.setPaymentInstructions("Transfer to NL30INGB0104244798 of Noa Goldin");
        invoice.setNote("Thank you!");
        invoice.setCurrency(Currency.USD);
        invoice.setOutsideEU(true);

        assertEquals(1L, invoice.getId());
        assertEquals("2025-00501", invoice.getInvoiceNumber());
        assertEquals(LocalDate.of(2025, 9, 24), invoice.getInvoiceDate());
        assertEquals("Consulting services", invoice.getDescription());
        assertEquals(10, invoice.getUnits());
        assertEquals(100.0, invoice.getRate());
        assertEquals(1000.0, invoice.getSubtotal());
        assertEquals(210.0, invoice.getVat());
        assertEquals(1210.0, invoice.getTotal());
        assertEquals("Transfer to NL30INGB0104244798 of Noa Goldin", invoice.getPaymentInstructions());
        assertEquals("Thank you!", invoice.getNote());
        assertEquals(Currency.USD, invoice.getCurrency());
        assertTrue(invoice.isOutsideEU());
    }

    @Test
    void whenInvoiceIsCreated_thenDefaultCurrencyIsEUR() {
        Invoice invoice = new Invoice();
        assertEquals(Currency.EUR, invoice.getCurrency());
    }

    @Test
    void whenContactIsAssigned_thenItCanBeRetrieved() {
        Contact contact = new Contact();
        contact.setId(42L);
        contact.setFirstName("Noa");

        Invoice invoice = new Invoice();
        invoice.setContact(contact);
        invoice.setContactId(contact.getId());

        assertEquals(42L, invoice.getContactId());
        assertEquals("Noa", invoice.getContact().getFirstName());
    }
}
