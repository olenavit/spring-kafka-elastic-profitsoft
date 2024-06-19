package ua.com.vitkovska;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ua.com.vitkovska.config.TestElasticsearchConfiguration;
import ua.com.vitkovska.data.EmailData;
import ua.com.vitkovska.data.EmailStatus;
import ua.com.vitkovska.message.EmailDetailsMessage;
import ua.com.vitkovska.service.EmailService;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Integration tests for email sending functionality.
 * Verifies sending and handling of team emails using mocked JavaMailSender.
 */
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {SpringKafkaProfItSoftApplication.class, TestElasticsearchConfiguration.class})
public class EmailSendingTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @MockBean
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

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

    private final EmailDetailsMessage emailDetailsMessage = EmailDetailsMessage.builder()
            .subject("Test subject")
            .content("Test content")
            .build();

    /**
     * Test case to verify successful sending of team email when listener receives message.
     * Mocks JavaMailSender to simulate successful email sending.
     * Verifies that the email is marked as SENT in Elasticsearch.
     */
    @Test
    public void shouldSendTeamEmailWhenListenerReceivedMessage() {

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        EmailData emailData = emailService.createEmail(emailDetailsMessage);
        emailService.sendEmail(emailData);

        verify(javaMailSender, after(5000)).send(any(SimpleMailMessage.class));

        Query searchQuery = new CriteriaQuery(new Criteria("subject").is(emailDetailsMessage.getSubject()));
        EmailData emailDataCreated = elasticsearchOperations.searchOne(searchQuery, EmailData.class).getContent();
        assertEquals(EmailStatus.SENT, emailDataCreated.getStatus());
        assertEquals(1, emailDataCreated.getAttemptCount());

    }

    /**
     * Test case to verify handling of failed email when mail service throws an error.
     * Mocks JavaMailSender to simulate email sending failure.
     * Verifies that the email is marked as FAILED in Elasticsearch.
     */
    @Test
    public void shouldSaveFailedEmailWhenMailServiceThrowError() {
        doThrow(new RuntimeException("Mail server not available")).when(javaMailSender).send(any(SimpleMailMessage.class));

        EmailData emailData = emailService.createEmail(emailDetailsMessage);
        emailService.sendEmail(emailData);

        verify(javaMailSender, after(5000)).send(any(SimpleMailMessage.class));

        Query searchQuery = new CriteriaQuery(new Criteria("subject").is(emailDetailsMessage.getSubject()));
        EmailData emailDataCreated = elasticsearchOperations.searchOne(searchQuery, EmailData.class).getContent();
        assertEquals(EmailStatus.FAILED, emailDataCreated.getStatus());
        assertEquals(1, emailDataCreated.getAttemptCount());
    }

}
