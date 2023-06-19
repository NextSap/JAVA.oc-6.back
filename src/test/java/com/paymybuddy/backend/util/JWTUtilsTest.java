package com.paymybuddy.backend.util;

import com.paymybuddy.backend.exception.JWTException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTUtilsTest {

    private final JWTUtils jwt = JWTUtils.getInstance();

    @Test
    public void getTokenTest() {
       String token = jwt.get("test@test.com");

       assertNotNull(token);
    }

    @Test
    public void verifyTokenTest() {
        String token = jwt.get("test@test.com");

        String email = jwt.verify(token);

        assertNotNull(email);
        assertEquals("test@test.com", email);

        String anotherToken = "eofkzeofk";

        assertThrows(JWTException.VerifyingTokenException.class , () -> jwt.verify(anotherToken));
    }

}
