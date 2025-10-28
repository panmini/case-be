package com.thy.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private RandomUtils() {
    }

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!-_";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String uniqueNumericCode() {
        long timestamp = System.currentTimeMillis(); // e.g. 1713209014235
        int random = ThreadLocalRandom.current().nextInt(1000, 9999); // 4-digit random
        return String.valueOf(timestamp) + random; // e.g. "17132090142359834"
    }

    public static long safeUniqueNumericCode() {
        long timestampPart = System.currentTimeMillis() % 1_000_000_000_000L; // last 12 digits
        int randomPart = ThreadLocalRandom.current().nextInt(100, 999);       // 3-digit random
        return timestampPart * 1000 + randomPart; // total max ~15 digits
    }

    public static String generateRandomPassword(int length) {
        if (length < 12) {
            length = 12;
        }

        List<Character> password = new ArrayList<>();

        password.add(UPPER.charAt(random.nextInt(UPPER.length())));
        password.add(LOWER.charAt(random.nextInt(LOWER.length())));
        password.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            password.add(ALL.charAt(random.nextInt(ALL.length())));
        }

        Collections.shuffle(password);

        StringBuilder sb = new StringBuilder();
        for (char c : password) {
            sb.append(c);
        }

        return sb.toString();
    }
}
