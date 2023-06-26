package com.paymybuddy.backend.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
public class CredentialUtilsTest {

    private final CredentialUtils credentialUtils = CredentialUtils.getInstance();

    @Test
    public void testCheck() {
        String password = "test";
        String hash = credentialUtils.hash(password);

        assertTrue(credentialUtils.check(password, hash));
        assertFalse(credentialUtils.check(password + "1", hash));
        assertFalse(credentialUtils.check(password, hash + "1"));
    }
}
