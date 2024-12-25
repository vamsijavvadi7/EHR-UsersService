package com.ehr.users.controller;

import com.ehr.users.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return ResponseEntity.status(HttpStatus.OK).body("OTP sent to email");
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP is valid");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
    }
}
