package com.example.shorten.utility;

import com.example.shorten.exception.HashGenerationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for Base62 encoding and URL shortening.
 * This implementation uses SHA-256 hashing and Base62 encoding to generate short URLs.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Base62 {
    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;
    private static final int SHORT_URL_LENGTH = 10;
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Encodes a BigInteger to Base62 string.
     *
     * @param number the number to encode
     * @return the Base62 encoded string
     * @throws IllegalArgumentException if number is null
     */
    public static String encode(BigInteger number) {
        Assert.notNull(number, "Number must not be null");

        if (number.equals(BigInteger.ZERO)) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        BigInteger value = number.abs();
        BigInteger base = BigInteger.valueOf(BASE);

        while (value.compareTo(BigInteger.ZERO) > 0) {
            sb.append(BASE62_ALPHABET.charAt(value.mod(base).intValue()));
            value = value.divide(base);
        }

        return sb.reverse().toString();
    }

    /**
     * Generates a short URL from the input string.
     *
     * @param input the input string to generate short URL from
     * @return the generated short URL
     * @throws IllegalArgumentException if input is null or empty
     * @throws HashGenerationException if there's an error generating the hash
     */
    public static String generateShortUrl(String input) {
        Assert.hasText(input, "Input string must not be null or empty");

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, hash);
            String encoded = encode(num);
            return encoded.substring(0, Math.min(encoded.length(), SHORT_URL_LENGTH));
        } catch (NoSuchAlgorithmException e) {
            throw new HashGenerationException("Error generating hash: " + e.getMessage(), e);
        }
    }
}
