package es.udc.ws.app.model.event;

import java.time.LocalDateTime;

public class Event {

    private Long eventId;
    private String ename;
    private String description;
    private LocalDateTime celebrationDate;
    private float duration;
    private LocalDateTime registerDate;
    private boolean activeEvent;
    private int afirmativeResponses;
    private int negativeResponses;

    // =========================================CONSTRUCTOR=========================================

    public Event(String ename, String description, LocalDateTime celebrationDate, float duration){
        this.ename=ename;
        this.description=description;
        this.celebrationDate=(celebrationDate != null) ? celebrationDate.withNano(0) : null;
        this.duration=duration;
        this.activeEvent=true;
        this.registerDate=LocalDateTime.now().withNano(0);
        this.afirmativeResponses = 0;
        this.negativeResponses = 0;
    }

    public Event(Long eventId, String ename, String description, LocalDateTime celebrationDate, float duration){
        this(ename, description, celebrationDate, duration);
        this.eventId = eventId;
    }

    public Event(Long eventId, String ename, String description, LocalDateTime celebrationDate, float duration, LocalDateTime registerDate, int afirmativeResponses, int negativeResponses){
        this(eventId, ename, description, celebrationDate, duration);
        this.registerDate=registerDate;
        this.afirmativeResponses = afirmativeResponses;
        this.negativeResponses = negativeResponses;
    }

    public Event(Long eventId, String ename, String description, LocalDateTime celebrationDate, float duration, LocalDateTime registerDate, boolean activeEvent, int afirmativeResponses, int negativeResponses){
        this(eventId, ename, description, celebrationDate, duration, registerDate, afirmativeResponses, negativeResponses);
        this.activeEvent = activeEvent;
    }

    public Event(Long eventId, String ename, String description, float duration, LocalDateTime celebrationDate, boolean activeEvent, int afirmativeResponses, int negativeResponses){
        this.eventId = eventId;
        this.ename=ename;
        this.description=description;
        this.duration=duration;
        this.registerDate=LocalDateTime.now().withNano(0);
        this.celebrationDate=(celebrationDate != null) ? celebrationDate.withNano(0) : null;
        this.activeEvent = activeEvent;
        this.afirmativeResponses = afirmativeResponses;
        this.negativeResponses = negativeResponses;
    }

    // ===========================================GETTERS===========================================

    public Long getEventId(){
        return eventId;
    }

    public String getEname() {
        return ename;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCelebrationDate() {
        return celebrationDate;
    }

    public float getDuration() {
        return duration;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public boolean isActiveEvent(){
        return activeEvent;
    }

    public int getAfirmativeResponses() {
        return afirmativeResponses;
    }

    public int getNegativeResponses() {
        return negativeResponses;
    }

    // ===========================================SETTERS===========================================
    public void setEname(String ename){this.ename = ename;}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCelebrationDate(LocalDateTime celebrationDate) {
        this.celebrationDate = celebrationDate;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setRegisterDate(LocalDateTime registerDate){this.registerDate = (registerDate != null) ? registerDate.withNano(0) : null;}

    public void setActiveEvent(boolean activeEvent){
        this.activeEvent = activeEvent;
    }

    public void setAfirmativeResponses(int afirmativeResponses) {
        this.afirmativeResponses = afirmativeResponses;
    }

    public void setNegativeResponses(int negativeResponses) {
        this.negativeResponses = negativeResponses;
    }

    // ===========================================EQUALS============================================

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;

        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;

        if (ename == null) {
            if (other.ename != null)
                return false;
        } else if (!ename.equals(other.ename))
            return false;

        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;

        if (celebrationDate == null) {
            if (other.celebrationDate != null)
                return false;
        } else if (!celebrationDate.equals(other.celebrationDate))
            return false;

        if (Float.floatToIntBits(duration) != Float.floatToIntBits(other.duration))
            return false;

        if (registerDate == null) {
            if (other.registerDate != null)
                return false;
        } else if (!registerDate.equals(other.registerDate))
            return false;

        if (activeEvent != other.activeEvent) {
            return false;
        }

        if(afirmativeResponses != other.afirmativeResponses){
            return false;
        }

        if(negativeResponses != other.negativeResponses){
            return false;
        }

        return true;
    }

    // ===========================================HASHCODE==========================================

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        result = prime * result + ((ename == null) ? 0 : ename.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((celebrationDate == null) ? 0 : celebrationDate.hashCode());
        result = prime * result + Float.floatToIntBits(duration);
        result = prime * result + ((registerDate == null) ? 0 : registerDate.hashCode());
        result = prime * result + afirmativeResponses;
        result = prime * result + negativeResponses;

        return result;
    }


}
