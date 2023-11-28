package ru.volovnik.documents.documents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.volovnik.documents.documents.entity.Inbox;
import ru.volovnik.documents.documents.exception.DocumentReceivingException;
import ru.volovnik.documents.documents.repository.InboxRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;
    private final DocumentService documentService;

    @Value("${documents.topic.documents-in}")
    private String docsIn;

    @Value("${documents.topic.documents-out}")
    private String docsOut;

    @Transactional
    @Override
    public void addMessage(String topic, String key, String payload) {
        UUID uuid = UUID.fromString(key);
        Inbox inbox = new Inbox(uuid, topic, payload, false);
        boolean exists = inboxRepository.existsById(uuid);
        if (exists) {
            throw new DocumentReceivingException("Error saving existing inbox message = " + inbox);
        } else {
            inboxRepository.save(inbox);
        }
    }

    @Scheduled(fixedDelay = 500L)
    @Transactional
    @Override
    public void completeMessage() {
        Optional<Inbox> inboxOptional = inboxRepository.findFirstByIsCompletedFalse();
        if (inboxOptional.isPresent()) {
            Inbox inbox = inboxOptional.get();
            String topic = inbox.getTopic();
            if (topic.equals(docsIn)) {
                documentService.processResult(inbox.getPayload());
            } else if (topic.equals(docsOut)) {
                documentService.processDocument(inbox.getPayload());
            }
            inbox.setIsCompleted(true);
            inboxRepository.save(inbox);
        }
    }
}
