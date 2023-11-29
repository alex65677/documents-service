package ru.volovnik.documents.service;

import java.util.UUID;

public interface OutboxService {

    /**
     * Метод обработки сообщения
     */
    void processMessage();

    /**
     * Установить, что сообщение отправлено
     *
     * @param id uuid сообщения
     */
    void setMessageIsSent(UUID id);

    /**
     * Добавить сообщение
     *
     * @param topic название топика
     * @param data  данные
     */
    void addMessage(String topic, String data);
}
