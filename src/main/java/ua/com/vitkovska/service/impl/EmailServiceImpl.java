package ua.com.vitkovska.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.data.EmailStatus;
import ua.com.vitkovska.message.EmailDetailsMessage;
import ua.com.vitkovska.repository.EmailMessageRepository;
import ua.com.vitkovska.service.EmailService;

import java.time.Instant;

/**
 * Implementation of {@link EmailService} that sends emails and manages email creation.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    private final EmailMessageRepository emailMessageRepository;

    /**
     * Sends an email using {@link JavaMailSender}.
     * Updates the status of the email based on success or failure.
     *
     * @param message the EmailData object containing email details
     */
    @Override
    public void sendEmail(EmailData message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(message.getRecipient());
            mailMessage.setSubject(message.getSubject());
            mailMessage.setText(message.getContent());
            emailSender.send(mailMessage);
            message.setStatus(EmailStatus.SENT);
        } catch (Exception e) {
            log.error("Failed to send email", e);
            message.setStatus(EmailStatus.FAILED);
            message.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            message.setLastAttemptTime(Instant.now());
            message.setAttemptCount(message.getAttemptCount() + 1);
            emailMessageRepository.save(message);
        }
    }


    /**
     * Creates a new {@link EmailData} based on the provided {@link EmailDetailsMessage}.
     * Sets initial values and saves the email to the repository.
     *
     * @param message the EmailDetailsMessage object containing email details
     * @return the created EmailData object
     */
    @Override
    public EmailData createEmail(EmailDetailsMessage message) {
        EmailData emailData = new EmailData();
        emailData.setSubject(message.getSubject());
        emailData.setContent(message.getContent());
        emailData.setRecipient(adminEmail);
        emailData.setStatus(EmailStatus.NEW);
        emailData.setAttemptCount(0);
        emailMessageRepository.save(emailData);
        return emailMessageRepository.save(emailData);
    }

}
