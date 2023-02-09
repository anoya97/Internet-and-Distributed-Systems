package es.udc.ws.app.model.eventservice.exceptions;


public class EventCanceledException extends Exception{
    private Long eventId;

    public EventCanceledException(Long eventId){
        super("Event with id=\"" + eventId + "\" is canceled");
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }



}

