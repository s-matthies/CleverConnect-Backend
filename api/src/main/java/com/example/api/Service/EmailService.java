package com.example.api.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service für das Versenden von E-Mails
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    /**
     * Methode für das Versenden einer HTML-E-Mail
     * @param to Die E-Mail-Adresse des Empfängers
     * @param subject Der Betreff der E-Mail
     * @param body Der Inhalt der E-Mail
     */
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

    /**
     * Methode für das Versenden einer Willkommens-E-Mail an eine externe Person (Zweitbetreuer*in)
     * @param to Die E-Mail-Adresse der Empfängerin
     * @param firstName Der Vorname der Empfängerin
     * @param lastName Der Nachname der Empfängerin
     */
    public void sendWelcomeEmailExternal(String to, String firstName, String lastName) {
        String subject = "Willkommen bei CleverConnect";
        String body = String.format("<p>Hallo %s %s,</p><p> wir freuen uns sehr, dass Sie jetzt auch clever connecten! </br> " +
                "Ein aussagekräftiges Profil erhöht die Chancen, von den Richtigen gefunden zu werden., also legen Sie am besten gleich los. </br> " +
                "Viel Freude dabei!</p>" +
                "<p>Ihr CleverConnect Team </p>", firstName, lastName);

        sendHtmlEmail(to, subject, body);
    }

    /**
     * Methode für das Versenden einer Willkommens-E-Mail an eine Studierende
     * @param to Die E-Mail-Adresse der Empfängerin
     * @param firstName Der Vorname der Empfängerin
     * @param lastName Der Nachname der Empfängerin
     */
    public void sendWelcomeEmailUser(String to, String firstName, String lastName) {
        String subject = "Willkommen bei CleverConnect";
        String body = String.format("<p>Hallo %s %s,</p><p> Du hast sich erfolgreich registriert.</br> " +
                "                Leg direkt los und finde die passende Zweitbetreuung für deine Bachelorarbeit.</br> " +
                "                Viel Freude dabei! </p>" +
                "<p>Dein CleverConnect Team </p>", firstName, lastName);

        sendHtmlEmail(to, subject, body);
    }

    public void sendWelcomeEmailExternalByAdmin(String to, String firstName, String lastName) {
        String subject = "Willkommen bei CleverConnect";
        String body = String.format("<p>Hallo %s %s,</p><p> Sie wurden erfolgreich bei unserer Platform Clever Connect registriert. </br>" +
                "Wir freuen uns sehr, dass Sie jetzt auch clever connecten! </br> " +
                "Ein aussagekräftiges Profil erhöht die Chancen, von den Richtigen gefunden zu werden., also legen Sie am besten gleich los. </br> " +
                "Viel Freude dabei!</br></br>" +
                "Infos zu Ihren Zugangsdaten zum Portal holen Sie bitte bei Herrn Prof. Dr.-Ing. Jörn Freiheit ein. </br>" +
                "Email: Joern.Freiheit@HTW-Berlin.de </p>" +
                "<p>Ihr CleverConnect Team </p>", firstName, lastName);

        sendHtmlEmail(to, subject, body);
    }

}
