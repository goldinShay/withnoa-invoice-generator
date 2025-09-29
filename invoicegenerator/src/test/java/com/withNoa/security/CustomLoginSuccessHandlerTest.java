package com.withNoa.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class CustomLoginSuccessHandlerTest {

    private final CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();

    @Test
    void whenRoleIsAdmin_thenRedirectToAdmin() throws IOException, ServletException {        Authentication auth = mock(Authentication.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"));
        doReturn(authorities).when(auth).getAuthorities();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();
        handler.onAuthenticationSuccess(request, response, auth);

        verify(response).sendRedirect("/admin");
    }



    @Test
    void whenRoleIsUser_thenRedirectToNoa() throws IOException, ServletException {
        Authentication auth = mock(Authentication.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
        doReturn(authorities).when(auth).getAuthorities();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();
        handler.onAuthenticationSuccess(request, response, auth);

        verify(response).sendRedirect("/noa");
    }

    @Test
    void whenRoleIsUnknown_thenRedirectToRoot() throws IOException, ServletException {
        Authentication auth = mock(Authentication.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("GUEST"));
        doReturn(authorities).when(auth).getAuthorities();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        CustomLoginSuccessHandler handler = new CustomLoginSuccessHandler();
        handler.onAuthenticationSuccess(request, response, auth);

        verify(response).sendRedirect("/");
    }

}
