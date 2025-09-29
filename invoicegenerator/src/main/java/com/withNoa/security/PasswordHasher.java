package com.withNoa.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("password for goGoldin"));
        System.out.println(encoder.encode("password for noaGoldin"));
    }
}

