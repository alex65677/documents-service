package ru.volovnik.documents.documents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.volovnik.documents.documents.controller.dto.DocumentDto;
import ru.volovnik.documents.documents.controller.dto.StatusCode;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DocumentService documentService;

    @KafkaListener(topics = "documents", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload DocumentDto documentDto) {
        documentDto.setStatus(StatusCode.ACCEPTED);
        documentService.update(documentDto);
    }
}
