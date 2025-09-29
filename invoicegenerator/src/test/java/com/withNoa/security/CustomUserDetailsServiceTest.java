package com.withNoa.security;

import com.withNoa.entity.AppUser;
import com.withNoa.entity.enums.Role;
import com.withNoa.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Test
    void whenUsernameExists_thenUserDetailsIsReturned() {
        AppUserRepository mockRepo = mock(AppUserRepository.class);
        AppUser user = new AppUser(1L, "noaGoldin", "iloveyou!", Role.USER);
        when(mockRepo.findByUsername("noaGoldin")).thenReturn(Optional.of(user));

        CustomUserDetailsService service = new CustomUserDetailsService();
        service.userRepository = mockRepo;

        UserDetails details = service.loadUserByUsername("noaGoldin");

        assertEquals("noaGoldin", details.getUsername());
        assertEquals("iloveyou!", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("USER")));
    }

    @Test
    void whenUsernameDoesNotExist_thenExceptionIsThrown() {
        AppUserRepository mockRepo = mock(AppUserRepository.class);
        when(mockRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        CustomUserDetailsService service = new CustomUserDetailsService();
        service.userRepository = mockRepo;

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("ghost"));
    }
}
