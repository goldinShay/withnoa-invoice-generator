package com.withNoa.entity;

import com.withNoa.entity.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void whenAppUserIsCreated_thenFieldsCanBeSetAndRetrieved() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("noaGoldin");
        user.setPassword("iloveyou!");
        user.setRole(Role.USER);

        assertEquals(1L, user.getId());
        assertEquals("noaGoldin", user.getUsername());
        assertEquals("iloveyou!", user.getPassword());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void whenAppUserIsCreatedWithConstructor_thenFieldsAreInitialized() {
        AppUser user = new AppUser(2L, "goGoldin", "AmonaMakeit4show!", Role.ADMIN);

        assertEquals(2L, user.getId());
        assertEquals("goGoldin", user.getUsername());
        assertEquals("AmonaMakeit4show!", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void whenAppUserIsCreated_thenRoleCanBeNull() {
        AppUser user = new AppUser();
        user.setUsername("legacyUser");
        assertNull(user.getRole());
    }
}
