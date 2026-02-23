package com.example.CWMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;

    public void sendCredentials(String to, String username, String password) {
        org.springframework.mail.SimpleMailMessage message = new org.springframework.mail.SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Vos identifiants CWMS");
        message.setText("Bonjour,\n\nVotre compte a été créé.\n" +
                "Nom d'utilisateur : " + username + "\n" +
                "Mot de passe temporaire : " + password + "\n\n" +
                "Cordialement,\nL'administration.");
        mailSender.send(message);
    }
}