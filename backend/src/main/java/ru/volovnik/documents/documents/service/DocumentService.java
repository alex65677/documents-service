package ru.volovnik.documents.documents.service;

import ru.volovnik.documents.documents.controller.dto.DocumentDto;

import java.util.List;
import java.util.Set;

/**
 * Сервис по работе с документами
 */
public interface DocumentService {
    /**
     * Сохранить документ
     *
     * @param documentDto документ
     * @return сохраненный документ
     */
    DocumentDto save(DocumentDto documentDto);

    /**
     * Удалить документ
     *
     * @param ids идентификаторы документов
     */
    void deleteAll(Set<Long> ids);

    /**
     * Удалить документ по ид
     *
     * @param id идентификатор документа
     */
    void delete(Long id);

    /**
     * Обновить документ
     *
     * @param documentDto документ
     * @return обновленный документ
     */
    DocumentDto update(DocumentDto documentDto);

    /**
     * Отправить документ на обработку
     *
     * @param id идентификатор документа
     * @return отправленный документ
     */
    DocumentDto send(Long id);

    /**
     * Получить все документы
     *
     * @return список документов
     */
    List<DocumentDto> findAll();

    /**
     * Получить документ по номеру
     *
     * @param id идентификатор
     * @return документ
     */
    DocumentDto get(Long id);
}
