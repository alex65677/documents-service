package ru.volovnik.documents.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.volovnik.documents.entity.Outbox;
import ru.volovnik.documents.repository.OutboxRepository;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class OutboxServiceImplTest {

    @Autowired
    private OutboxServiceImpl outboxService;
    @Autowired
    private OutboxRepository outboxRepository;

    @BeforeEach
    public void before() {
        outboxRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        outboxRepository.deleteAll();
    }

    @Test
    public void testSuccessAddMessage() {
        outboxService.addMessage("test", "test");
        boolean exists = outboxRepository.existsById(UUID.nameUUIDFromBytes("test".getBytes()));
        Assertions.assertTrue(exists);
    }

    @Test
    public void testSuccessMessageIsSent() {
        outboxService.addMessage("test", "test");
        UUID uuid = UUID.nameUUIDFromBytes("test".getBytes());
        outboxService.setMessageIsSent(uuid);
        Optional<Outbox> outbox = outboxRepository.findById(uuid);
        Boolean isSent = outbox.get().getIsSent();
        Assertions.assertTrue(isSent);
    }
}
