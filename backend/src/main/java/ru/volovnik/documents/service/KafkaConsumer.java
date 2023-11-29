package ru.volovnik.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final InboxService inboxService;

    @KafkaListener(topics = "${documents.topic.documents-in}",
            groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumeIn(ConsumerRecord<String, String> record) {
        inboxService.addMessage(record.topic(), record.key(), record.value());
    }

    @KafkaListener(topics = "${documents.topic.documents-out}",
            groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOut(ConsumerRecord<String, String> record) {
        inboxService.addMessage(record.topic(), record.key(), record.value());
    }
}
