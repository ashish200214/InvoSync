package com.asent.invoSync.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email containing quotation links (PDF + images).
     * No drawing file URL will be included.
     */
    public void sendQuotationLinks(String toEmail, String subject, String body, List<String> fileUrls) {
        if (toEmail == null || toEmail.isEmpty()) {
            System.err.println("⚠️ Email address is missing. Skipping email send.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(body).append("\n\n");

        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (String url : fileUrls) {
                sb.append("🔗 ").append(url).append("\n");
            }
        } else {
            sb.append("No files were attached.\n");
        }

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject(subject);
        msg.setText(sb.toString());

        try {
            mailSender.send(msg);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}
