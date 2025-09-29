package com.withNoa.entity;

import com.withNoa.entity.enums.Currency;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @SequenceGenerator(name = "invoice_seq", sequenceName = "invoice_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_seq")
    private Long id;

    @Column(name = "contact_id")
    private Long contactId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id", insertable = false, updatable = false)
    private Contact contact;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.EUR;

    private int units;
    private double rate;
    private double subtotal;
    private double vat;
    private boolean outsideEU;
    private double total;
    private String paymentInstructions;
    private String note;

    public Invoice() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getContactId() { return contactId; }
    public void setContactId(Long contactId) { this.contactId = contactId; }

    public Contact getContact() { return contact; }
    public void setContact(Contact contact) { this.contact = contact; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getVat() { return vat; }
    public void setVat(double vat) { this.vat = vat; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getPaymentInstructions() { return paymentInstructions; }
    public void setPaymentInstructions(String paymentInstructions) { this.paymentInstructions = paymentInstructions; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }
    public boolean isOutsideEU() {
        return outsideEU;
    }

    public void setOutsideEU(boolean outsideEU) {
        this.outsideEU = outsideEU;
    }
}
