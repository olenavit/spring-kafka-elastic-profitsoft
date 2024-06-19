package ua.com.vitkovska.service;

import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.message.EmailDetailsMessage;

/**
 * Service interface for managing emails.
 */
public interface EmailService {
    EmailData createEmail(EmailDetailsMessage message);
    void sendEmail(EmailData email);
}
