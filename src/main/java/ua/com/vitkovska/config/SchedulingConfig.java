package ua.com.vitkovska.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.elasticsearch8.ElasticsearchLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class for setting up scheduling with ShedLock and Elasticsearch.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled",
        havingValue = "true", matchIfMissing = true)
@EnableSchedulerLock(defaultLockAtMostFor = "PT1M")
public class SchedulingConfig {


    /**
     * Creates and configures a LockProvider bean using Elasticsearch
     *
     * @param elasticsearchClient the Elasticsearch client to use for the lock provider
     * @return a configured ElasticsearchLockProvider instance
     */
    @Bean
    public LockProvider lockProvider(@Autowired ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchLockProvider(elasticsearchClient);
    }
}

