package com.quyet.superapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnMissingBean(OtpService.class)
@Slf4j
public class InMemoryOtpService implements OtpService {

    private static final int OTP_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);

    private static final class Entry {
        final String otp;
        final long expiresAtMs;
        int attempts;

        Entry(String otp, long expiresAtMs) {
            this.otp = otp;
            this.expiresAtMs = expiresAtMs;
            this.attempts = 0;
        }
    }

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public String generateOtp(String email) {
        String otp = randomOtp();
        store.put(key(email), new Entry(otp, System.currentTimeMillis() + OTP_TTL.toMillis()));
        log.info("✅ OTP (in-memory) generated for [{}]", email);
        return otp;
    }

    @Override
    public boolean validateOtp(String email, String inputOtp) {
        Entry e = store.get(key(email));
        if (e == null) return false;
        if (System.currentTimeMillis() > e.expiresAtMs) {
            store.remove(key(email));
            return false;
        }
        if (e.attempts >= MAX_ATTEMPTS) return false;
        if (e.otp.equals(inputOtp)) {
            store.remove(key(email));
            return true;
        }
        e.attempts++;
        return false;
    }

    private String randomOtp() {
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }

    private String key(String email) {
        return "otp:" + email;
    }
}

