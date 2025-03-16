package org.lokeshdarla.notifly;

import jakarta.mail.internet.MimeMessage;
import org.lokeshdarla.notifly.dto.BulkEmailRequest;
import org.lokeshdarla.notifly.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private JavaMailSenderImpl configureMailSender(String host, int port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    private boolean sendSingleEmail(JavaMailSenderImpl mailSender, String senderEmail, String recipient, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending email to " + recipient + ": " + e.getMessage());
            return false;
        }
    }

    public String sendEmail(EmailRequest emailRequest) {
        JavaMailSenderImpl mailSender = configureMailSender(
                emailRequest.getSmtpHost(),
                emailRequest.getSmtpPort(),
                emailRequest.getSenderEmail(),
                emailRequest.getAppPassword()
        );

        boolean success = sendSingleEmail(
                mailSender,
                emailRequest.getSenderEmail(),
                emailRequest.getRecipientEmail(),
                emailRequest.getSubject(),
                emailRequest.getBody()
        );

        return success ? "Email sent successfully!" : "Error sending email.";
    }

    public String sendEmail(BulkEmailRequest emailRequest) {
        JavaMailSenderImpl mailSender = configureMailSender(
                emailRequest.getSmtpHost(),
                emailRequest.getSmtpPort(),
                emailRequest.getSenderEmail(),
                emailRequest.getAppPassword()
        );

        int successCount = 0;
        List<String> recipients = emailRequest.getRecipientEmails();

        for (String recipient : recipients) {
            if (sendSingleEmail(mailSender, emailRequest.getSenderEmail(), recipient, emailRequest.getSubject(), emailRequest.getBody())) {
                successCount++;
            }
        }

        return "Bulk emails sent: " + successCount + "/" + recipients.size();
    }
}
