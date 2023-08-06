package com.paymybuddy.backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
public class CredentialUtilsTest {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testCheck() {
        String password = "test";
        String hash = bCryptPasswordEncoder.encode(password);

        assertTrue(bCryptPasswordEncoder.matches(password, hash));
        assertFalse(bCryptPasswordEncoder.matches(password + "1", hash));
        assertFalse(bCryptPasswordEncoder.matches(password, hash + "1"));
    }
}
