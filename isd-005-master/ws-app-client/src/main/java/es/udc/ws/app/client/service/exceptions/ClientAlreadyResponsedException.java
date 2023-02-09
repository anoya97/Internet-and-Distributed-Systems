package es.udc.ws.app.client.service.exceptions;

public class ClientAlreadyResponsedException extends Exception{

    private Long eventId;

    public ClientAlreadyResponsedException(Long eventId){
        super("Event with id=\"" + eventId + "\" has already been answered");
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

}
