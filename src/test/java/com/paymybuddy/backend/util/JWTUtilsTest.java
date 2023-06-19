package com.paymybuddy.backend.util;

import com.paymybuddy.backend.exception.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTUtilsTest {

    private final JWTUtils jwt = JWTUtils.getInstance();
    private final String email = "test@test.com";

    @Test
    public void getTokenTest() {
       String token = jwt.get(email);

       assertNotNull(token);
    }

    @Test
    public void verifyTokenTest() {
        String token = jwt.get(email);

        String email = jwt.verify(token);

        assertNotNull(email);
        assertEquals(this.email, email);

        String anotherToken = "eofkzeofk";

        assertThrows(UserException.BadCredentialsException.class , () -> jwt.verify(anotherToken));
    }
}
