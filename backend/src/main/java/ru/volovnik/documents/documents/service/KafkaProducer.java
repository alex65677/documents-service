package ru.volovnik.documents.documents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.volovnik.documents.documents.controller.dto.DocumentDto;

import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<Long, DocumentDto> kafkaTemplate;

    public void sendMessage(DocumentDto documentDto) {
        ListenableFuture<SendResult<Long, DocumentDto>> future =
                kafkaTemplate.send("documents", documentDto.getId(), documentDto);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ex.getMessage());
            }
            @Override
            public void onSuccess(SendResult<Long, DocumentDto> result) {
                log.info("Message = '" + documentDto + "' sent");
            }
        });
        try {
            SendResult<Long, DocumentDto> sendResult = future.get();
            log.info(sendResult.getProducerRecord().toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
