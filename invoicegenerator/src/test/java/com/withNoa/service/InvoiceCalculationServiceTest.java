package com.withNoa.service;

import com.withNoa.entity.Invoice;
import com.withNoa.entity.enums.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceCalculationServiceTest {

    private final InvoiceCalculationService service = new InvoiceCalculationService();

    @Test
    void whenInvoiceIsInsideEU_thenVatIsApplied() {
        Invoice invoice = new Invoice();
        invoice.setUnits(10);
        invoice.setRate(100.0);
        invoice.setOutsideEU(false);

        service.applyCalculations(invoice);

        assertEquals(1000.0, invoice.getSubtotal(), 0.01);
        assertEquals(210.0, invoice.getVat(), 0.01);
        assertEquals(1210.0, invoice.getTotal(), 0.01);
    }

    @Test
    void whenInvoiceIsOutsideEU_thenVatIsZero() {
        Invoice invoice = new Invoice();
        invoice.setUnits(5);
        invoice.setRate(200.0);
        invoice.setOutsideEU(true);

        service.applyCalculations(invoice);

        assertEquals(1000.0, invoice.getSubtotal(), 0.01);
        assertEquals(0.0, invoice.getVat(), 0.01);
        assertEquals(1000.0, invoice.getTotal(), 0.01);
    }

    @Test
    void whenUnitsOrRateAreZero_thenSubtotalAndTotalAreZero() {
        Invoice invoice = new Invoice();
        invoice.setUnits(0);
        invoice.setRate(0.0);
        invoice.setOutsideEU(false);

        service.applyCalculations(invoice);

        assertEquals(0.0, invoice.getSubtotal(), 0.01);
        assertEquals(0.0, invoice.getVat(), 0.01);
        assertEquals(0.0, invoice.getTotal(), 0.01);
    }

    @Test
    void whenCurrencyIsSet_thenItRemainsUnchanged() {
        Invoice invoice = new Invoice();
        invoice.setUnits(1);
        invoice.setRate(100.0);
        invoice.setOutsideEU(false);
        invoice.setCurrency(Currency.USD);

        service.applyCalculations(invoice);

        assertEquals(Currency.USD, invoice.getCurrency());
    }
}
