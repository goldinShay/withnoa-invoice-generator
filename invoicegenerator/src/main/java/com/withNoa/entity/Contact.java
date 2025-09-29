package com.withNoa.entity;

import com.withNoa.entity.enums.ContactType;
import com.withNoa.entity.enums.Language;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact")
//@NoArgsConstructor
public class Contact {

    @Id
    @SequenceGenerator(name = "contact_seq", sequenceName = "contact_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_seq")
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String companyName;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    private Language preferredLanguage;

    private String notes;

    // ✅ Added constructor for test compatibility
    public Contact(Long id, String firstName, String lastName, String email, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }
    public Contact() {}

    // Getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public ContactType getContactType() { return contactType; }
    public void setContactType(ContactType contactType) { this.contactType = contactType; }

    public Language getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(Language preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
