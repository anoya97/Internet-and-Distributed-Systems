package es.udc.ws.app.restservice.dto;

public class RestEventDto {

    private Long eventId;
    private String ename;
    private String description;
    private float duration;
    private String celebrationDate;
    private boolean activeEvent;
    private int afirmativeResponses;
    private int totalResponses;

    // =========================================CONSTRUCTOR=========================================

    public RestEventDto(){
    }

    public RestEventDto(Long eventId, String ename, String description, float duration,
                        String celebrationDate, boolean activeEvent, int afirmativeResponses, int totalResponses){

        this.eventId = eventId;
        this.ename = ename;
        this.description = description;
        this.duration = duration;
        this.celebrationDate = celebrationDate;
        this.activeEvent = activeEvent;
        this.afirmativeResponses = afirmativeResponses;
        this.totalResponses = totalResponses;
    }

    // ===========================================GETTERS===========================================

    public Long getEventId() {
        return eventId;
    }

    public String getEname() {
        return ename;
    }

    public String getDescription() {
        return description;
    }

    public float getDuration() {
        return duration;
    }

    public String getCelebrationDate() {
        return celebrationDate;
    }

    public boolean isActiveEvent() {
        return activeEvent;
    }

    public int getAfirmativeResponses() {
        return afirmativeResponses;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    // ===========================================SETTERS===========================================

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setCelebrationDate(String celebrationDate) {
        this.celebrationDate = celebrationDate;
    }

    public void setActiveEvent(boolean activeEvent) {
        this.activeEvent = activeEvent;
    }

    public void setAfirmativeResponses(int afirmativeResponses) {
        this.afirmativeResponses = afirmativeResponses;
    }

    public void setTotalResponses(int totalResponses) {
        this.totalResponses = totalResponses;
    }

    // ==========================================TO STRING==========================================

    @Override
    public String toString() {
        return "RestEventDto [" +
                "eventId=" + eventId +
                ", ename=" + ename +
                ", description=" + description +
                ", duration=" + duration +
                ", celebrationDate=" + celebrationDate +
                ", activeEvent=" + activeEvent +
                ", afirmativeResponses=" + afirmativeResponses +
                ", totalResponses=" + totalResponses +
                ']';
    }

}
