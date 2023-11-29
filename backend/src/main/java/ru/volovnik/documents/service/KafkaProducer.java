package ru.volovnik.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public RecordMetadata sendMessage(String topic, String key, String value) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, value);
        try {
            SendResult<String, String> sendResult = future.get();
            log.info(sendResult.getProducerRecord().toString());
            return sendResult.getRecordMetadata();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
