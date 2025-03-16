package org.lokeshdarla.notifly.dto;

import lombok.Data;
import java.util.List;

@Data
public class BulkEmailRequest {
    private String smtpHost;
    private int smtpPort;
    private String senderEmail;
    private String appPassword;
    private List<String> recipientEmails;  // Now supports multiple recipients
    private String subject;
    private String body;
}

