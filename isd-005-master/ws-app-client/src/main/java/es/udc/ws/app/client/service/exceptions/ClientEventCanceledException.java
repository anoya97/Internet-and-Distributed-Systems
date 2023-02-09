package es.udc.ws.app.client.service.exceptions;

public class ClientEventCanceledException extends Exception {

    private Long eventId;

    public ClientEventCanceledException(Long eventId){
        super("Event with id=\"" + eventId + "\" is canceled");
        this.eventId = eventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long responseId) {
        this.eventId = responseId;
    }

}
