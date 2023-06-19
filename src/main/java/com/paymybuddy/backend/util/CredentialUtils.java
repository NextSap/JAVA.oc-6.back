package com.paymybuddy.backend.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class CredentialUtils {

    private static final CredentialUtils INSTANCE = new CredentialUtils();

    private CredentialUtils() {
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean check(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public static CredentialUtils getInstance() {
        return INSTANCE;
    }
}
