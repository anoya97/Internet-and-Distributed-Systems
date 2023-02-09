package es.udc.ws.app.client.service.dto;


import java.time.LocalDateTime;

public class ClientEventDto {

    private Long eventId;
    private String ename;
    private String description;
    private LocalDateTime celebrationDate;
    private LocalDateTime endDate;
    private boolean activeEvent;
    private int afirmativeResponses;
    private int totalResponses;

    // =========================================CONSTRUCTOR=========================================

    public ClientEventDto(Long eventId, String ename, String description, LocalDateTime celebrationDate, LocalDateTime endDate) {
        this.eventId = eventId;
        this.ename = ename;
        this.description = description;
        this.celebrationDate = celebrationDate;
        this.endDate = endDate;
        this.activeEvent = true;
        this.afirmativeResponses = 0;
        this.totalResponses = 0;
    }

    public ClientEventDto(Long eventId, String ename, String description, LocalDateTime celebrationDate, LocalDateTime endDate,
                          boolean activeEvent, int afirmativeResponses, int totalResponses) {
        this(eventId, ename, description, celebrationDate, endDate);
        this.activeEvent = activeEvent;
        this.afirmativeResponses = afirmativeResponses;
        this.totalResponses = totalResponses;
    }

    // ===========================================GETTERS===========================================

    public Long getEventId() {return eventId;}

    public String getEname() {return ename;}

    public String getDescription() {return description;}

    public LocalDateTime getCelebrationDate() {return celebrationDate;}

    public LocalDateTime getEndDate() {return endDate;}

    public boolean isActiveEvent() {return activeEvent;}

    public int getAfirmativeResponses() {return afirmativeResponses;}

    public int getTotalResponses() {return totalResponses;}

    // ===========================================SETTERS===========================================

    public void setEventId(Long eventId) {this.eventId = eventId;}

    public void setEname(String ename) {this.ename = ename;}

    public void setDescription(String description) {this.description = description;}

    public void setCelebrationDate(LocalDateTime celebrationDate) {this.celebrationDate = celebrationDate;}

    public void setEndDate(LocalDateTime endTime) {this.endDate = endTime;}

    public void setActiveEvent(boolean activeEvent) {this.activeEvent = activeEvent;}

    public void setAfirmativeResponses(int afirmativeResponses) {this.afirmativeResponses = afirmativeResponses;}

    public void setTotalResponses(int totalResponses) {this.totalResponses = totalResponses;}


    // ==========================================TO STRING==========================================

    @Override
    public String toString() {
        return "ClientEventDto [" +
                "eventId=" + eventId +
                ", ename='" + ename +
                ", description='" + description +
                ", celebrationDate=" + celebrationDate +
                ", endDate=" + endDate +
                ", activeEvent=" + activeEvent +
                ", afirmativeResponses=" + afirmativeResponses +
                ", totalResponses=" + totalResponses +
                ']';
    }
}
