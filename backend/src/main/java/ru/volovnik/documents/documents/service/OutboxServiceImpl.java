package ru.volovnik.documents.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.volovnik.documents.documents.entity.Outbox;
import ru.volovnik.documents.documents.exception.DocumentSendingException;
import ru.volovnik.documents.documents.repository.OutboxRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {

    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 500L)
    @Transactional
    @Override
    public void processMessage() {
        Optional<Outbox> outboxMessage = outboxRepository.findFirstByIsSentFalse();
        if (outboxMessage.isPresent()) {
            Outbox outbox = outboxMessage.get();
            RecordMetadata metadata =
                    kafkaProducer.sendMessage(outbox.getTopic(), outbox.getId().toString(), outbox.getPayload());
            if (metadata.hasOffset()) {
                setMessageIsSent(outbox.getId());
            }
        }
    }

    @Transactional
    @Override
    public void setMessageIsSent(UUID id) {
        Optional<Outbox> optionalOutbox = outboxRepository.findById(id);
        Outbox outbox = optionalOutbox.orElseThrow(
                () -> new IllegalStateException("Can't find message with id = " + id));
        outbox.setIsSent(true);
        outboxRepository.save(outbox);
    }

    @Transactional
    @Override
    public void addMessage(String topic, String data) {
        UUID uuid = UUID.nameUUIDFromBytes(data.getBytes());
        Outbox outbox = new Outbox(uuid, topic, data, false);
        boolean exists = outboxRepository.existsById(uuid);
        if (exists) {
            throw new DocumentSendingException("Error saving existing outbox message = " + outbox);
        } else {
            outboxRepository.save(outbox);
        }
    }
}
