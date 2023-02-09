package es.udc.ws.app.model.eventservice.exceptions;

public class AlreadyResponsedException extends Exception{
    private Long eventId;

    public AlreadyResponsedException(Long eventId){
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
