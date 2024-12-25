package com.ehr.users.service;

import com.ehr.users.dao.OtpRepository;
import com.ehr.users.model.Otp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    private final int OTP_EXPIRATION_MINUTES = 10;

    public void sendOtp(String email) {
        String otp = String.valueOf((int)(Math.random() * 9000) + 1000); // Generate 4-digit OTP
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpirationTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
        otpRepository.save(otpEntity);
        sendEmail(email, otp);
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It will expire in 10 minutes.");
        mailSender.send(message);
    }

    public boolean validateOtp(String email, String otp) {
        Optional<Otp> otpEntityOptional = otpRepository.findByEmail(email);
        if (otpEntityOptional.isPresent()) {
            Otp otpEntity = otpEntityOptional.get();
            return otpEntity.getOtp().equals(otp) && otpEntity.getExpirationTime().isAfter(LocalDateTime.now());
        }
        return false;
    }

}
