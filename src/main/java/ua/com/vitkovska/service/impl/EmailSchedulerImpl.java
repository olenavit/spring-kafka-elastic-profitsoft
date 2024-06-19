package ua.com.vitkovska.service.impl;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.data.EmailStatus;
import ua.com.vitkovska.repository.EmailMessageRepository;
import ua.com.vitkovska.service.EmailScheduler;
import ua.com.vitkovska.service.EmailService;

import java.util.List;

/**
 * Implementation of {@link EmailScheduler} that retries sending failed emails on a schedule.
 * Uses {@link EmailService} to resend failed emails and {@link EmailMessageRepository} for accessing email data.
 */
@Service
@RequiredArgsConstructor
public class EmailSchedulerImpl implements EmailScheduler {

    private final EmailService emailService;

    private final EmailMessageRepository emailMessageRepository;


    /**
     * Scheduled method to retry sending failed emails.
     * Retrieves failed emails from the repository and attempts to resend them using {@link EmailService}.
     * Uses ShedLock to ensure that this method is executed exclusively.
     */
    @Override
    @Scheduled(cron = "${retry.failed.emails.cron:0 0/5 * * * ?}")
    @SchedulerLock(name = "ProcessFailedEmails", lockAtLeastFor = "PT10S", lockAtMostFor = "PT10M")
    public void retryFailedEmails() {
        List<EmailData> failedMessages = emailMessageRepository.findByStatus(EmailStatus.FAILED);
        for (EmailData message : failedMessages) {
           emailService.sendEmail(message);
        }
    }
}
