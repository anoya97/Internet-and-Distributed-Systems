package es.udc.ws.app.client.service.exceptions;

public class ClientLimitDateException extends Exception {

    private Long eventId;

    public ClientLimitDateException(Long eventId){
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
