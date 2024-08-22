package com.tinqinacademy.auth.api.interfaces;

import org.springframework.scheduling.annotation.Async;


public interface EmailService {
    @Async
    void sendEmail(String to, String subject, String text);
}
