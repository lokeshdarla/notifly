package org.lokeshdarla.notifly;

import jakarta.mail.internet.MimeMessage;
import org.lokeshdarla.notifly.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("lokeshdarla@gmail.com");
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        System.out.println("Email sent");
    }

    public String sendEmail(EmailRequest emailRequest) {
        try {
            // Configure Mail Sender
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailRequest.getSmtpHost());
            mailSender.setPort(emailRequest.getSmtpPort());
            mailSender.setUsername(emailRequest.getSenderEmail());
            mailSender.setPassword(emailRequest.getAppPassword());

            // Set Properties
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            // Create Email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(emailRequest.getSenderEmail());
            helper.setTo(emailRequest.getRecipientEmail());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getBody(), true);

            // Send Email
            mailSender.send(message);
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }
}
