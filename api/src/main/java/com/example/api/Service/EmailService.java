package com.example.api.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;




    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("Mail wurde erfolgreich versendet.");
    }

    private void sendHtmlEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom("noreply@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set the second parameter to true for HTML content
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);

        System.out.println("Mail wurde erfolgreich versendet.");
    }

    public void sendWelcomeEmailExternal(String to, String firstName, String lastName) {
        String subject = "Willkommen bei CleverConnect";
        String body = String.format("<p>Hallo %s %s,</p><p> wir freuen uns sehr, dass Sie jetzt auch clever connecten! </br> " +
                "Ein aussagekräftiges Profil erhöht die Chancen, von den Richtigen gefunden zu werden., also legen Sie am besten gleich los. </br> " +
                "Viel Freude dabei!</p>" +
                "<p>Ihr CleverConnect Team </p>", firstName, lastName);

        sendHtmlEmail(to, subject, body);
    }

    public void sendWelcomeEmailUser(String to, String firstName, String lastName) {
        String subject = "Willkommen bei CleverConnect";
        String body = String.format("<p>Hallo %s %s,</p><p> Du hast sich erfolgreich registriert.</br> " +
                "                Leg direkt los und finde die passende Zweitbetreuung für deine Bachelorarbeit.</br> " +
                "                Viel Freude dabei! </p>" +
                "<p>Dein CleverConnect Team </p>", firstName, lastName);

        sendHtmlEmail(to, subject, body);
    }
}
