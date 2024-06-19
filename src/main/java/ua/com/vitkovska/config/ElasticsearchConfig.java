package ua.com.vitkovska.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * Configuration class for setting up Elasticsearch client configuration.
 * This class extends AbstractElasticsearchConfiguration and overrides
 * the clientConfiguration method to provide custom Elasticsearch settings.
 */
@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.address}")
    private String esAddress;

    /**
     * Provides the client configuration for connecting to Elasticsearch.
     *
     * @return a configured ClientConfiguration instance
     */
    @NotNull
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(esAddress)
                .build();
    }

}
