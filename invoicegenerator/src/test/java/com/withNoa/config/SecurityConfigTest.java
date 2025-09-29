package com.withNoa.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void whenContextLoads_thenSecurityFilterChainIsAvailable() {
        SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
        assertNotNull(chain, "SecurityFilterChain bean should be present");
    }

    @Test
    void whenContextLoads_thenOauthSuccessHandlerIsAvailable() {
        AuthenticationSuccessHandler handler = context.getBean("oauthSuccessHandler", AuthenticationSuccessHandler.class);
        assertNotNull(handler, "OAuth2 success handler bean should be present");
    }

    @Test
    void whenContextLoads_thenPasswordEncoderIsAvailableAndNoOp() {
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        assertNotNull(encoder, "PasswordEncoder bean should be present");
        assertEquals("password", encoder.encode("password"), "NoOpPasswordEncoder should return input as-is");
    }
}
