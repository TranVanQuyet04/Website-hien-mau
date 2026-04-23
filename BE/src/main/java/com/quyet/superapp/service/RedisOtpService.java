package com.quyet.superapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnBean(RedisTemplate.class)
public class RedisOtpService implements OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int OTP_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration REGISTER_OTP_FLAG_TTL = Duration.ofMinutes(10);

    /**
     * 🔐 Sinh OTP và lưu vào Redis (kèm reset số lần thử về 0)
     */
    @Override
    public String generateOtp(String email) {
        String otp = generateRandomOtp();
        String otpKey = buildOtpKey(email);
        String attemptKey = buildAttemptKey(email);

        redisTemplate.opsForValue().set(otpKey, otp, OTP_TTL);
        redisTemplate.opsForValue().set(attemptKey, "0", OTP_TTL);

        log.info("✅ OTP [{}] được sinh cho email [{}]", otp, email);
        return otp;
    }

    /**
     * ✅ Kiểm tra OTP người dùng nhập và xử lý đếm số lần sai
     */
    @Override
    public boolean validateOtp(String email, String inputOtp) {
        String otpKey = buildOtpKey(email);
        String attemptKey = buildAttemptKey(email);

        String savedOtp = redisTemplate.opsForValue().get(otpKey);
        if (savedOtp == null) {
            log.warn("⚠️ Không tìm thấy OTP trong Redis cho [{}]", email);
            return false;
        }

        int attempts = Integer.parseInt(
                Optional.ofNullable(redisTemplate.opsForValue().get(attemptKey)).orElse("0")
        );

        if (attempts >= MAX_ATTEMPTS) {
            log.warn("🚫 Quá số lần thử OTP cho [{}]: {} lần", email, attempts);
            return false;
        }

        if (savedOtp.equals(inputOtp)) {
            redisTemplate.delete(otpKey);
            redisTemplate.delete(attemptKey);
            log.info("✅ OTP hợp lệ cho [{}]", email);
            return true;
        }

        // ❌ Nhập sai → tăng số lần thử + reset TTL
        redisTemplate.opsForValue().increment(attemptKey);
        redisTemplate.expire(attemptKey, OTP_TTL.getSeconds(), TimeUnit.SECONDS);
        log.warn("❌ OTP không đúng cho [{}] – Lần thử thứ [{}]", email, attempts + 1);
        return false;
    }

    /**
     * 🔢 Sinh ngẫu nhiên chuỗi 6 số
     */
    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String buildOtpKey(String email) {
        return "otp:" + email;
    }
    private String buildAttemptKey(String email) {
        return "otp-attempts:" + email;
    }



}
