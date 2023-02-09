package es.udc.ws.app.client.service.dto;

import java.time.LocalDate;

public class ClientResponseDto {

    private Long responseId;
    private String employeeEmail;
    private Long eventId;
    private boolean confirmation;

    // =========================================CONSTRUCTOR=========================================

    public ClientResponseDto() {
    }

    public ClientResponseDto(Long responseId, String employeeEmail, Long eventId, boolean confirmation) {
        this.responseId = responseId;
        this.employeeEmail = employeeEmail;
        this.eventId = eventId;
        this.confirmation = confirmation;
    }

// ===========================================GETTERS===========================================

    public Long getResponseId() {
        return responseId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public Long getEventId() {
        return eventId;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    // ===========================================SETTERS===========================================

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    // ==========================================TO STRING==========================================

    @Override
    public String toString() {
        return "RestResponseDto [" +
                "responseId=" + responseId +
                ", employeeEmail='" + employeeEmail +
                ", eventId=" + eventId +
                ", confirmation=" + confirmation +
                ']';
    }
}