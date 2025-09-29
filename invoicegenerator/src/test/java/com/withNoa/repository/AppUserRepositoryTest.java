package com.withNoa.repository;

import com.withNoa.entity.AppUser;
import com.withNoa.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository repository;

    @Test
    void whenUserIsSaved_thenCanBeFoundByUsername() {
        repository.deleteAll(); // 🧹 Clean up before test

        AppUser user = new AppUser(null, "noaGoldin", "iloveyou!", Role.USER);
        repository.save(user);

        Optional<AppUser> found = repository.findByUsername("noaGoldin");
        assertTrue(found.isPresent());
        assertEquals("noaGoldin", found.get().getUsername());
    }

    @Test
    void whenUsernameDoesNotExist_thenFindByUsernameReturnsEmpty() {
        Optional<AppUser> result = repository.findByUsername("ghost");
        assertTrue(result.isEmpty());
    }
}
