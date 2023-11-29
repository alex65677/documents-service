package ru.volovnik.documents.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessResultDto {

    /**
     * ID документа
     */
    private Long id;

    /**
     * Статус
     */
    private StatusCode status;
}
