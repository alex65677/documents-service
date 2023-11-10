package ru.volovnik.documents.documents.controller.dto;


public enum StatusCode {
    NEW("NEW"),
    IN_PROCESS("IN_PROCESS"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");

    private final String status;

    StatusCode(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
