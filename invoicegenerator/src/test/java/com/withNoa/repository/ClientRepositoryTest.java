package com.withNoa.repository;

import com.withNoa.entity.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Test
    void whenClientIsSaved_thenCanBeRetrievedById() {
        repository.deleteAll(); // 🧹 Clean slate

        Client client = new Client(null, "Legacy Partner", "partner@withnoa.com");
        Client saved = repository.save(client);

        Optional<Client> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Legacy Partner", found.get().getName());
        assertEquals("partner@withnoa.com", found.get().getEmail());
    }

    @Test
    void whenClientIsDeleted_thenCannotBeRetrieved() {
        Client client = new Client(null, "Ghost Client", "ghost@withnoa.com");
        Client saved = repository.save(client);

        repository.deleteById(saved.getId());

        Optional<Client> found = repository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    void whenMultipleClientsAreSaved_thenFindAllReturnsThem() {
        repository.deleteAll();

        Client c1 = new Client(null, "Client One", "one@withnoa.com");
        Client c2 = new Client(null, "Client Two", "two@withnoa.com");

        repository.save(c1);
        repository.save(c2);

        var allClients = repository.findAll();
        assertEquals(2, allClients.size());
    }
}
