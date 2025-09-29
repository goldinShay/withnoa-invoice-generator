package com.withNoa.repository;

import com.withNoa.entity.Invoice;
import com.withNoa.entity.enums.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository repository;

    @Test
    void whenInvoiceIsSaved_thenCanBeRetrievedById() {
        repository.deleteAll();

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("2025-00501");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCurrency(Currency.EUR);
        invoice.setDescription("Legacy service");
        invoice.setUnits(2);
        invoice.setRate(100.0);
        invoice.setSubtotal(200.0);
        invoice.setVat(42.0);
        invoice.setTotal(242.0);

        Invoice saved = repository.save(invoice);

        Optional<Invoice> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("2025-00501", found.get().getInvoiceNumber());
    }

    @Test
    void whenInvoiceNumberStartsWithPrefix_thenQueryReturnsMatchingInvoices() {
        repository.deleteAll();

        Invoice i1 = new Invoice();
        i1.setInvoiceNumber("2025-00501");
        i1.setInvoiceDate(LocalDate.now());
        i1.setCurrency(Currency.EUR);

        Invoice i2 = new Invoice();
        i2.setInvoiceNumber("2025-00502");
        i2.setInvoiceDate(LocalDate.now());
        i2.setCurrency(Currency.EUR);

        Invoice i3 = new Invoice();
        i3.setInvoiceNumber("2024-00499");
        i3.setInvoiceDate(LocalDate.now());
        i3.setCurrency(Currency.EUR);

        repository.saveAll(List.of(i1, i2, i3));

        List<Invoice> result = repository.findByInvoiceNumberStartingWith("2025-");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(inv -> inv.getInvoiceNumber().startsWith("2025-")));
    }

    @Test
    void whenInvoiceIsDeleted_thenCannotBeRetrieved() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("2025-00999");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCurrency(Currency.EUR);

        Invoice saved = repository.save(invoice);
        repository.deleteById(saved.getId());

        Optional<Invoice> found = repository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
