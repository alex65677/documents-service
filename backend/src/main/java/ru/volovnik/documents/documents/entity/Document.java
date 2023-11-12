package ru.volovnik.documents.documents.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.volovnik.documents.documents.controller.dto.StatusCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "documents")
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    /**
     * Номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Вид документа
     */
    @Column
    private String type;

    /**
     * Организация
     */
    @Column
    private String organization;

    /**
     * Описание
     */
    @Column
    private String description;

    /**
     * Пациент
     */
    @Column
    private String patient;

    /**
     * Дата документа
     */
    @Column
    private Date date;

    /**
     * Статус
     */
    @Column
    @Enumerated(EnumType.STRING)
    private StatusCode status;
}
