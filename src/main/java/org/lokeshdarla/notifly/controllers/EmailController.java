package org.lokeshdarla.notifly.controllers;

import org.lokeshdarla.notifly.EmailSenderService;
import org.lokeshdarla.notifly.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailSenderService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest);
    }
}
