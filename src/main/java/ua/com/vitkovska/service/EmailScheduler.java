package ua.com.vitkovska.service;

/**
 * Service interface for scheduling email-related tasks.
 */
public interface EmailScheduler {
    void retryFailedEmails();
}
