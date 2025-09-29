package com.withNoa.security;

import com.withNoa.entity.AppUser;
import com.withNoa.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void whenCustomUserDetailsCreated_thenUsernameAndPasswordAreMappedCorrectly() {
        AppUser user = new AppUser(1L, "noaGoldin", "iloveyou!", Role.USER);
        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("noaGoldin", details.getUsername());
        assertEquals("iloveyou!", details.getPassword());
    }

    @Test
    void whenCustomUserDetailsCreated_thenAuthoritiesContainUserRole() {
        AppUser user = new AppUser(2L, "goGoldin", "AmonaMakeit4show!", Role.ADMIN);
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void whenCustomUserDetailsCreated_thenAccountFlagsAreTrue() {
        AppUser user = new AppUser(3L, "legacyUser", "secure!", Role.USER);
        CustomUserDetails details = new CustomUserDetails(user);

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
