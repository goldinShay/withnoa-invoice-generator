package com.withNoa.entity;

import com.withNoa.entity.enums.ContactType;
import com.withNoa.entity.enums.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void whenContactIsCreated_thenFieldsCanBeSetAndRetrieved() {
        Contact contact = new Contact();
        contact.setId(42L);
        contact.setFirstName("Noa");
        contact.setMiddleName("L.");
        contact.setLastName("Goldin");
        contact.setEmail("noa@withnoa.com");
        contact.setPhoneNumber("123456789");
        contact.setAddress("Oude Vest 211");
        contact.setCompanyName("With Noa");
        contact.setContactType(ContactType.BUSINESS);
        contact.setPreferredLanguage(Language.ENGLISH);
        contact.setNotes("Legacy builder");

        assertEquals(42L, contact.getId());
        assertEquals("Noa", contact.getFirstName());
        assertEquals("L.", contact.getMiddleName());
        assertEquals("Goldin", contact.getLastName());
        assertEquals("noa@withnoa.com", contact.getEmail());
        assertEquals("123456789", contact.getPhoneNumber());
        assertEquals("Oude Vest 211", contact.getAddress());
        assertEquals("With Noa", contact.getCompanyName());
        assertEquals(ContactType.BUSINESS, contact.getContactType());
        assertEquals(Language.ENGLISH, contact.getPreferredLanguage());
        assertEquals("Legacy builder", contact.getNotes());
    }

    @Test
    void whenContactIsCreatedWithoutMiddleName_thenMiddleNameIsNull() {
        Contact contact = new Contact();
        contact.setFirstName("Noa");
        assertNull(contact.getMiddleName());
    }

    @Test
    void whenContactTypeIsSet_thenItCanBeRetrieved() {
        Contact contact = new Contact();
        contact.setContactType(ContactType.BUSINESS);
        assertEquals(ContactType.BUSINESS, contact.getContactType());
    }

    @Test
    void whenPreferredLanguageIsSet_thenItCanBeRetrieved() {
        Contact contact = new Contact();
        contact.setPreferredLanguage(Language.HEBREW);
        assertEquals(Language.HEBREW, contact.getPreferredLanguage());
    }
}
