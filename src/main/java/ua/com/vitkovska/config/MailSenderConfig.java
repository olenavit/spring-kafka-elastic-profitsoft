package ua.com.vitkovska.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * Configuration class for setting up JavaMailSender bean.
 * This class reads email properties from .env file
 * and configures the JavaMailSender instance accordingly.
 */
@Configuration
public class MailSenderConfig {
    @Value("${EMAIL_HOST}")
    private String host;

    @Value("${EMAIL_PORT}")
    private int port;

    @Value("${EMAIL_USERNAME}")
    private String username;

    @Value("${EMAIL_PASSWORD}")
    private String password;

    @Value("${EMAIL_PROTOCOL}")
    private String protocol;

    @Value("${EMAIL_SMTP_AUTH}")
    private String smtpAuth;

    @Value("${EMAIL_SMTP_STARTTLS_ENABLE}")
    private String starttlsEnable;

    @Value("${EMAIL_DEBUG}")
    private String debug;

    /**
     * Creates and configures a JavaMailSender bean.
     *
     * @return a configured JavaMailSender instance
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.debug", debug);

        return mailSender;
    }

}
