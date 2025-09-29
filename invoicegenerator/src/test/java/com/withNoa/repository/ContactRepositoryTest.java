package com.withNoa.repository;

import com.withNoa.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository repository;

    @Test
    void whenContactIsSaved_thenCanBeRetrievedById() {
        repository.deleteAll(); // 🧹 Clean slate

        Contact contact = new Contact(null, "Noa", "Goldin", "noa@withnoa.com", "Leiden, NL");
        Contact saved = repository.save(contact);

        Optional<Contact> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Noa", found.get().getFirstName());
        assertEquals("Goldin", found.get().getLastName());
        assertEquals("noa@withnoa.com", found.get().getEmail());
        assertEquals("Leiden, NL", found.get().getAddress());
    }

    @Test
    void whenContactIsDeleted_thenCannotBeRetrieved() {
        Contact contact = new Contact(null, "Ghost", "Client", "ghost@withnoa.com", "Nowhere");
        Contact saved = repository.save(contact);

        repository.deleteById(saved.getId());

        Optional<Contact> found = repository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    void whenMultipleContactsAreSaved_thenFindAllReturnsThem() {
        repository.deleteAll();

        Contact c1 = new Contact(null, "Alice", "Wonder", "alice@withnoa.com", "Amsterdam");
        Contact c2 = new Contact(null, "Bob", "Builder", "bob@withnoa.com", "Rotterdam");

        repository.save(c1);
        repository.save(c2);

        var allContacts = repository.findAll();
        assertEquals(2, allContacts.size());
    }
}
