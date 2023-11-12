package ru.volovnik.documents.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.volovnik.documents.documents.controller.dto.DocumentDto;
import ru.volovnik.documents.documents.controller.dto.StatusCode;
import ru.volovnik.documents.documents.entity.Document;
import ru.volovnik.documents.documents.repository.DocumentRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ModelMapper mapper;

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


    public DocumentDto update(DocumentDto documentDto) {
        Optional<Document> document = documentRepository.findById(documentDto.getId());
        if (document.isPresent()) {
            save(documentDto);
        }
        return documentDto;
    }

    public void delete(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent()) {
            documentRepository.deleteById(id);
        }
    }

    public void deleteAll(Set<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<DocumentDto> findAll() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDto> documentDtos = new ArrayList<>();
        for (Document document : documents) {
            DocumentDto documentDto = mapper.map(document, DocumentDto.class);
            documentDtos.add(documentDto);
        }
        return documentDtos;
    }

    public DocumentDto get(Long id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        Document document = optionalDocument.orElseThrow(() -> new IllegalStateException("Can't find document with id = " + id));
        return mapper.map(document, DocumentDto.class);
    }
}
