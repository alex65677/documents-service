package ru.volovnik.documents.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    /**
     * Номер
     */
    private Long id;

    /**
     * Вид документа
     */
    @NotBlank
    @Size(max = 255)
    private String type;

    /**
     * Организация
     */
    @NotBlank
    @Size(max = 255)
    private String organization;

    /**
     * Описание
     */
    @NotBlank
    @Size(max = 255)
    private String description;

    /**
     * Пациент
     */
    @NotBlank
    @Size(max = 255)
    private String patient;

    /**
     * Дата документа
     */
    private Date date;

    /**
     * Статус
     */
    private StatusCode status;
}
