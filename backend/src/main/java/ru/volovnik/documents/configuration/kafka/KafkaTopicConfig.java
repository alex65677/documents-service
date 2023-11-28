package ru.volovnik.documents.configuration.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${documents.topic.documents-in}")
    private String docsIn;

    @Value("${documents.topic.documents-out}")
    private String docsOut;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(config);
    }

    @Bean
    public NewTopic topicDocumentsOut() {
        return new NewTopic(docsOut, 1, (short) 1);
    }

    @Bean
    public NewTopic topicDocumentsIn() {
        return new NewTopic(docsIn, 1, (short) 1);
    }
}
