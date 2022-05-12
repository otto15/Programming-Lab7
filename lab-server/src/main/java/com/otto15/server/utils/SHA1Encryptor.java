package com.otto15.server.utils;

import com.otto15.server.logging.LogConfig;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHA1Encryptor implements Encryptor {

    private static final short RADIX = 16;
    private static final short HASH_TEXT_LENGTH = 32;

    @Override
    public String encrypt(String string) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(string.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(RADIX));

            while (hashText.length() < HASH_TEXT_LENGTH) {
                hashText.insert(0, "0");
            }

            result = hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            LogConfig.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
        return result;
    }
}
