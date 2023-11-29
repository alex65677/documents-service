package ru.volovnik.documents.service;

public interface InboxService {

    /**
     * Добавить сообщение на обработку
     *
     * @param topic   топик
     * @param key     ключ сообщения
     * @param payload данные
     */
    void addMessage(String topic, String key, String payload);

    /**
     * Обработать сообщение
     */
    void completeMessage();
}
