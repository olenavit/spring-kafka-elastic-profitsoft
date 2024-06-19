package ua.com.vitkovska;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ua.com.vitkovska.config.TestElasticsearchConfiguration;
import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.data.EmailStatus;
import ua.com.vitkovska.repository.EmailMessageRepository;
import ua.com.vitkovska.service.EmailScheduler;

import java.time.Instant;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Integration test for email scheduling functionality.
 * Verifies retry mechanism for failed emails using mocked JavaMailSender.
 */
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {SpringKafkaProfItSoftApplication.class, TestElasticsearchConfiguration.class})
public class EmailSchedulingTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailScheduler emailScheduler;

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    /**
     * Setup method executed before each test method.
     * Creates mapping for EmailData in Elasticsearch.
     */
    @BeforeEach
    public void beforeEach() {
        elasticsearchOperations.indexOps(EmailData.class).createMapping();
    }

    /**
     * Teardown method executed after each test method.
     * Deletes all documents of EmailData from Elasticsearch.
     */
    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(EmailData.class).delete();
    }

    /**
     * Test case to verify retry mechanism for failed emails.
     * Creates failed emails, saves them, triggers retry mechanism,
     * and verifies that emails are retried and status is updated correctly.
     */
    @Test
    void testRetryFailedEmails() {
        EmailData failedEmail1 = new EmailData();
        failedEmail1.setRecipient("user1@example.com");
        failedEmail1.setSubject("Subject 1");
        failedEmail1.setContent("Content 1");
        failedEmail1.setStatus(EmailStatus.FAILED);
        failedEmail1.setAttemptCount(1);
        failedEmail1.setLastAttemptTime(Instant.now());

        EmailData failedEmail2 = new EmailData();
        failedEmail2.setRecipient("user2@example.com");
        failedEmail2.setSubject("Subject 2");
        failedEmail2.setContent("Content 2");
        failedEmail2.setStatus(EmailStatus.FAILED);
        failedEmail2.setAttemptCount(1);
        failedEmail2.setLastAttemptTime(Instant.now());

        emailMessageRepository.save(failedEmail1);
        emailMessageRepository.save(failedEmail2);

        emailScheduler.retryFailedEmails();

        List<EmailData> emails = StreamSupport
                .stream(emailMessageRepository.findAll().spliterator(), false)
                .toList();

        verify(javaMailSender, times(2)).send(any(SimpleMailMessage.class));

        assertEquals(2, emails.size());
        assertEquals(EmailStatus.SENT, emails.get(0).getStatus());
        assertEquals(EmailStatus.SENT, emails.get(1).getStatus());
        assertEquals(2, emails.get(0).getAttemptCount());
        assertEquals(2, emails.get(1).getAttemptCount());
    }
}
