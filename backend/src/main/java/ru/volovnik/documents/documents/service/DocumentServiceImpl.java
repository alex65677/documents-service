package ru.volovnik.documents.documents.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.volovnik.documents.documents.controller.dto.DocumentDto;
import ru.volovnik.documents.documents.controller.dto.ProcessResultDto;
import ru.volovnik.documents.documents.controller.dto.StatusCode;
import ru.volovnik.documents.documents.entity.Document;
import ru.volovnik.documents.documents.exception.DocumentReceivingException;
import ru.volovnik.documents.documents.exception.DocumentSendingException;
import ru.volovnik.documents.documents.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper mapper;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @Value("${documents.topic.documents-in}")
    private String docsIn;

    @Value("${documents.topic.documents-out}")
    private String docsOut;

    @Transactional
    @Override
    public DocumentDto save(DocumentDto documentDto) {
        if (documentDto.getDate() == null) {
            documentDto.setDate(new Date());
        }
        if (documentDto.getStatus() == null) {
            documentDto.setStatus(StatusCode.NEW);
        }
        Document documentEntity = mapper.map(documentDto, Document.class);
        Document saved = documentRepository.save(documentEntity);
        if (documentDto.getId() == null) {
            documentDto.setId(saved.getId());
        }
        return documentDto;
    }

    @Transactional
    @Override
    public DocumentDto update(DocumentDto documentDto) {
        Optional<Document> document = documentRepository.findById(documentDto.getId());
        if (document.isPresent()) {
            save(documentDto);
        }
        return documentDto;
    }

    @Transactional
    @Override
    public DocumentDto send(Long id) {
        DocumentDto documentDto = get(id);
        documentDto.setStatus(StatusCode.IN_PROCESS);
        try {
            String json = objectMapper.writeValueAsString(documentDto);
            outboxService.addMessage(docsOut, json);
        } catch (JsonProcessingException e) {
            throw new DocumentSendingException(e.getMessage());
        }
        return update(documentDto);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            documentRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteAll(Set<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    public List<DocumentDto> findAll() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (Document document : documents) {
            DocumentDto documentDto = mapper.map(document, DocumentDto.class);
            documentDtos.add(documentDto);
        }
        return documentDtos;
    }

    @Override
    public DocumentDto get(Long id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        Document document = optionalDocument.orElseThrow(
                () -> new DocumentReceivingException("Can't find document with id = " + id));
        return mapper.map(document, DocumentDto.class);
    }

    @Transactional
    @Override
    public void processDocument(String data) {
        try {
            DocumentDto documentDto = objectMapper.readValue(data, DocumentDto.class);
            Random random = new Random();
            int randomInt = random.nextInt(2);
            ProcessResultDto processResultDto;
            if (randomInt == 0) {
                processResultDto = new ProcessResultDto(documentDto.getId(), StatusCode.DECLINED);
            } else {
                processResultDto = new ProcessResultDto(documentDto.getId(), StatusCode.ACCEPTED);
            }
            String json = objectMapper.writeValueAsString(processResultDto);
            outboxService.addMessage(docsIn, json);
        } catch (JsonProcessingException e) {
            throw new DocumentSendingException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void processResult(String data) {
        try {
            ProcessResultDto resultDto = objectMapper.readValue(data, ProcessResultDto.class);
            DocumentDto documentDto = get(resultDto.getId());
            documentDto.setStatus(resultDto.getStatus());
            save(documentDto);
        } catch (JsonProcessingException e) {
            throw new DocumentReceivingException(e.getMessage());
        }
    }
}
