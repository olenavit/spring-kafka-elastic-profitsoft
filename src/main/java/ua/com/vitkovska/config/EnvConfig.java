package ua.com.vitkovska.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Component for loading environment variables from a .env file into system properties.
 * This class uses the Dotenv library to load environment variables and set them as
 * system properties at application startup.
 */
@Component
public class EnvConfig {
    /**
     * Loads environment variables from a .env file and sets them as system properties.
     * This method is annotated with @PostConstruct to ensure it is executed after
     * the bean is initialized by the Spring container.
     */
    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}
