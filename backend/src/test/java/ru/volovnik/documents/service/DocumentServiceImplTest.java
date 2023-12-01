package ru.volovnik.documents.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import ru.volovnik.documents.controller.dto.DocumentDto;
import ru.volovnik.documents.controller.dto.ProcessResultDto;
import ru.volovnik.documents.controller.dto.StatusCode;
import ru.volovnik.documents.entity.Outbox;
import ru.volovnik.documents.exception.DocumentReceivingException;
import ru.volovnik.documents.repository.DocumentRepository;
import ru.volovnik.documents.repository.OutboxRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
public class DocumentServiceImplTest {

    @Autowired
    private DocumentServiceImpl documentService;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private OutboxRepository outboxRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        documentRepository.deleteAll();
        outboxRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        documentRepository.deleteAll();
        outboxRepository.deleteAll();
    }

    @Test
    public void testSuccessSave() {
        DocumentDto savedDocument = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});

        DocumentDto documentDto = documentService.get(savedDocument.getId());
        Assertions.assertEquals(savedDocument.getId(), documentDto.getId());
    }

    @Test
    void testSuccessUpdate() {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});

        documentDto.setDescription("test");
        DocumentDto updated = documentService.update(documentDto);
        Assertions.assertEquals("test", updated.getDescription());
    }

    @Test
    void testSuccessDelete() {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});
        documentService.delete(documentDto.getId());
        Assertions.assertEquals(0, documentService.findAll().size());
    }

    @Test
    void testSuccessDeleteAll() {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});
        DocumentDto documentDto2 = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});

        documentService.deleteAll(Set.of(documentDto.getId(), documentDto2.getId()));
        Assertions.assertEquals(0, documentService.findAll().size());
    }

    @Test
    void testSuccessFindAll() {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});
        DocumentDto documentDto2 = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});

        List<DocumentDto> allDocument = documentService.findAll();
        Assertions.assertEquals(2, allDocument.size());
    }

    @Test
    void testSuccessThrowsWhenDocumentNotFound() {
        Assertions.assertThrows(DocumentReceivingException.class, () -> documentService.get(1L));
    }

    @Test
    void testSuccessSendDocument() throws JsonProcessingException {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});
        DocumentDto send = documentService.send(documentDto.getId());
        Optional<Outbox> optionalOutbox = outboxRepository
                .findById(UUID.nameUUIDFromBytes(objectMapper.writeValueAsString(send).getBytes()));
        Outbox outbox = optionalOutbox.get();
        DocumentDto dto = objectMapper.readValue(outbox.getPayload(), DocumentDto.class);
        Assertions.assertEquals(dto.getStatus(), StatusCode.IN_PROCESS);
    }

    @Test
    void testSuccessProcessResult() throws JsonProcessingException {
        DocumentDto documentDto = documentService.save(new DocumentDto() {{
            setType("type");
            setPatient("patient");
            setDescription("desc");
            setOrganization("org");
        }});
        DocumentDto send = documentService.send(documentDto.getId());
        ProcessResultDto processResultDto = new ProcessResultDto(send.getId(), StatusCode.ACCEPTED);
        documentService.processResult(objectMapper().writeValueAsString(processResultDto));
        DocumentDto dto = documentService.get(send.getId());
        Assertions.assertEquals(processResultDto.getStatus(), dto.getStatus());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat());

        return mapper;
    }
}
