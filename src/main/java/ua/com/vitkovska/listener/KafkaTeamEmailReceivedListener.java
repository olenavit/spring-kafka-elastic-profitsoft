package ua.com.vitkovska.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.message.EmailDetailsMessage;
import ua.com.vitkovska.service.EmailService;

/**
 * Listener class for handling Kafka messages related to team emails.
 * This class listens to the specified Kafka topic and processes received email messages.
 */
@Component
@RequiredArgsConstructor
public class KafkaTeamEmailReceivedListener {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.teamEmail}")
    public void teamEmailReceived(EmailDetailsMessage emailDetailsMessage) {
        EmailData emailData = emailService.createEmail(emailDetailsMessage);
        emailService.sendEmail(emailData);
    }
}