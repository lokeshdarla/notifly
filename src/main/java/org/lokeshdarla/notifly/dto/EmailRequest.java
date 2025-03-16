package org.lokeshdarla.notifly.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String smtpHost;
    private int smtpPort;
    private String senderEmail;
    private String appPassword;
    private String recipientEmail;
    private String subject;
    private String body;
}

