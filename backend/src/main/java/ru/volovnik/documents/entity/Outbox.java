package ru.volovnik.documents.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "outbox")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Outbox {

    /**
     * Идентификатор
     */
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Название топика
     */
    @Column
    private String topic;

    /**
     * Данные
     */
    @Column
    private String payload;

    /**
     * Флаг отправки
     */
    @Column(name = "sent")
    private Boolean isSent;
}
