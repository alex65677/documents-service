package ru.volovnik.documents.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.volovnik.documents.repository.InboxRepository;

import java.util.UUID;

@SpringBootTest
public class InboxServiceImplTest {

    @Autowired
    private InboxServiceImpl inboxService;
    @Autowired
    private InboxRepository inboxRepository;

    @BeforeEach
    public void before() {
        inboxRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        inboxRepository.deleteAll();
    }

    @Test
    public void testSuccessAddMessage() {
        UUID uuid = UUID.nameUUIDFromBytes("key".getBytes());
        inboxService.addMessage("test", uuid.toString(), "payload");
        boolean exists = inboxRepository.existsById(uuid);
        Assertions.assertTrue(exists);
    }
}
