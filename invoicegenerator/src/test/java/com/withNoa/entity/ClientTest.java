package com.withNoa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void whenClientIsCreated_thenFieldsCanBeSetAndRetrieved() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Noa Goldin");
        client.setEmail("noa@withnoa.com");

        assertEquals(1L, client.getId());
        assertEquals("Noa Goldin", client.getName());
        assertEquals("noa@withnoa.com", client.getEmail());
    }

    @Test
    void whenClientIsCreatedWithConstructor_thenFieldsAreInitialized() {
        Client client = new Client(2L, "Legacy Partner", "partner@withnoa.com");

        assertEquals(2L, client.getId());
        assertEquals("Legacy Partner", client.getName());
        assertEquals("partner@withnoa.com", client.getEmail());
    }

    @Test
    void whenClientIsCreated_thenEmailCanBeNull() {
        Client client = new Client();
        client.setName("Noa");
        assertNull(client.getEmail());
    }
}
