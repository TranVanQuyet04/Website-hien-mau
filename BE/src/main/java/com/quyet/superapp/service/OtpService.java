package com.quyet.superapp.service;

public interface OtpService {
    String generateOtp(String email);

    boolean validateOtp(String email, String inputOtp);
}

