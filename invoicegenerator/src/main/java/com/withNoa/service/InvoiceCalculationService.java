package com.withNoa.service;

import com.withNoa.entity.Invoice;
import org.springframework.stereotype.Service;

@Service
public class InvoiceCalculationService {
    public void applyCalculations(Invoice invoice) {
        double subtotal = invoice.getUnits() * invoice.getRate();
        double vat = invoice.isOutsideEU() ? 0.0 : subtotal * 0.21;
        double total = subtotal + vat;

        invoice.setSubtotal(subtotal);
        invoice.setVat(vat);
        invoice.setTotal(total);
    }
}