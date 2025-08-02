package com.quyet.superapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminCreateUserRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String fullName;
    private LocalDate dob;
    private String gender;

    @NotBlank
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;


    private ContactInfoDTO contactInfo;
    private AddressDTO address;
    private String location;

    private Double weight;
    private Double height;

    @NotBlank
    private String staffPosition; // "Doctor" hoặc "Staff"
}
