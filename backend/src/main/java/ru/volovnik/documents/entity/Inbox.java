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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inbox")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inbox {

    /**
     * Идентификатор
     */
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Топик
     */
    @Column
    private String topic;

    /**
     * Данные
     */
    @Column
    private String payload;

    /**
     * Флаг выполнения
     */
    @Column(name = "completed")
    private Boolean isCompleted;
}
