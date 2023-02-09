package es.udc.ws.app.model.response;

import java.time.LocalDateTime;

public class Response {

    private Long responseId;
    private String employeeEmail;
    private Long eventId;
    private boolean confirmation;
    private LocalDateTime responseDate;

    public Response (String employeeEmail, Long eventId,
                     boolean confirmation, LocalDateTime responseDate){
        this.eventId = eventId;
        this.confirmation = confirmation;
        this.responseDate = (responseDate != null) ? responseDate.withNano(0) : null;
        this.employeeEmail = employeeEmail;
    }

    public Response (Long responseId, String employeeEmail, Long eventId,
                     boolean confirmation, LocalDateTime responseDate){
        this(employeeEmail, eventId, confirmation, responseDate);
        this.responseId=responseId;
    }

    public Response (Long responseId, String employeeEmail, Long eventId,
                     boolean confirmation){
        this.responseId=responseId;
        this.eventId = eventId;
        this.confirmation = confirmation;
        this.employeeEmail = employeeEmail;
    }


    public Long getResponseId(){return responseId;}

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public Long getEventId() {
        return eventId;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

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

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((responseId == null) ? 0 : responseId.hashCode());
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        result = prime * result + ((employeeEmail == null) ? 0 : employeeEmail.hashCode());
        result = prime * result + ((responseDate == null) ? 0 : responseDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Response other = (Response) obj;

        if (responseId == null) {
            if (other.responseId != null)
                return false;
        } else if (!responseId.equals(other.responseId))
            return false;

        if (employeeEmail == null) {
            if (other.employeeEmail != null)
                return false;
        } else if (!employeeEmail.equals(other.employeeEmail))
            return false;

        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;

        if (confirmation != other.confirmation) {
            return false;
        }

        if (responseDate == null) {
            if (other.responseDate!= null)
                return false;
        } else if (!responseDate.equals(other.responseDate))
            return false;


        return true;
    }

}
