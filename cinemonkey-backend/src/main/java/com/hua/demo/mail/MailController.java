package com.hua.demo.mail;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Mail Management", description = "API to manage emails")
public class MailController {
    @Autowired
    private JavaMailSender mailSender;
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@example.com");
        message.setSubject("Test Email");
        message.setText("This is a test from Spring Boot + MailDev");
        mailSender.send(message);
    }
    @PostMapping("/send-ticket")
    public ResponseEntity<String> sendTicketEmail(@RequestParam String email, @RequestBody byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Your Ticket from Cine-Monkey 🎬");
            helper.setText("Please find your ticket attached. Enjoy the movie!");

            // Attach the PDF
            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment("ticket.pdf", dataSource);

            mailSender.send(message);
            return ResponseEntity.ok("Email sent");
        } catch (Exception e) {
            log.error("Failed to send email", e);
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }

}
