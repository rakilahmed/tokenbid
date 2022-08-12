package com.tokenbid.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class to send an email from "exceptions.tokenbid@gmail.com"
 */
@Service
public class EmailService {

    @Autowired
    JavaMailSender emailSender;

    public void sendMessage(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@tokenbid.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        } catch (MailException e) {
            System.out.println("Failed to send email");
            e.printStackTrace();
        }
    }
}
