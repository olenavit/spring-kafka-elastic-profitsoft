package ua.com.vitkovska.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import java.util.Map;


/**
 * Configuration class for setting up Kafka topics.
 * This class reads Kafka properties from the application properties file
 * and configures the KafkaAdmin and topics accordingly.
 */
@Configuration
public class KafkaTopicConfig {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Value("${kafka.topic.teamEmail}")
  private String teamEmailReceived;

  /**
   * Creates and configures a KafkaAdmin bean.
   *
   * @return a configured KafkaAdmin instance
   */
  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = Map.of(
        AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  /**
   * Creates and configures a NewTopic bean for the team email topic.
   *
   * @return a configured NewTopic instance
   */
  @Bean
  public NewTopic teamEmailReceivedTopic() {
    return new NewTopic(teamEmailReceived, 2, (short) 1);
  }
}
