package ua.com.vitkovska.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.List;

/**
 * Test configuration for Elasticsearch using Testcontainers.
 * Extends {@link ElasticsearchConfiguration} to provide Elasticsearch client configuration.
 */
@Slf4j
@TestConfiguration
public class TestElasticsearchConfiguration extends ElasticsearchConfiguration {

    public static final String ELASTICSEARCH_DOCKER_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.6.1";

    /**
     * Creates and starts an Elasticsearch Docker container using Testcontainers.
     *
     * @return the ElasticsearchContainer instance
     */
    @Bean(destroyMethod = "stop")
    public ElasticsearchContainer elasticsearchContainer() {
        ElasticsearchContainer container = new ElasticsearchContainer(
                ELASTICSEARCH_DOCKER_IMAGE);
        container.setEnv(List.of(
                "discovery.type=single-node",
                "ES_JAVA_OPTS=-Xms1g -Xmx1g",
                "xpack.security.enabled=false")
        );
        container.start();
        log.info("Started ES container on address {}", container.getHttpHostAddress());
        return container;
    }

    /**
     * Provides a ClientConfiguration connected to the Elasticsearch Docker container.
     *
     * @return the ClientConfiguration instance
     */
    @NotNull
    @Bean
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchContainer().getHttpHostAddress())
                .build();
    }

}