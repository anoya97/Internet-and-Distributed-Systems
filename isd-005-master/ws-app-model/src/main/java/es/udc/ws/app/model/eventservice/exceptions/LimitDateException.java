package es.udc.ws.app.model.eventservice.exceptions;

public class LimitDateException extends Exception{
    private Long eventId;

    public LimitDateException(Long eventId){
        super("The limit time of the event with id=\"" + eventId +  "\" has expired\n ");
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }



}

