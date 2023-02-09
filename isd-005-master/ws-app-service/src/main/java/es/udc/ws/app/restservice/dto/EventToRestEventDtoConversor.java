package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.event.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventToRestEventDtoConversor {

    public static List<RestEventDto> toRestEventDtos(List<Event> events){
        List<RestEventDto> eventDtos = new ArrayList<>(events.size());
        for (Event event : events) {
            eventDtos.add(toRestEventDto(event));
        }
        return eventDtos;
    }

    public static RestEventDto toRestEventDto(Event event) {
        return new RestEventDto(event.getEventId(), event.getEname(), event.getDescription(),
                event.getDuration(), event.getCelebrationDate().withNano(0).toString(), event.isActiveEvent(),
                event.getAfirmativeResponses(), event.getAfirmativeResponses() + event.getNegativeResponses());
    }

    public static Event toEvent(RestEventDto eventDto) {

        return new Event(eventDto.getEventId(), eventDto.getEname(), eventDto.getDescription(),
                eventDto.getDuration(), LocalDateTime.parse(eventDto.getCelebrationDate()), eventDto.isActiveEvent(),
                eventDto.getAfirmativeResponses(), eventDto.getTotalResponses() - eventDto.getAfirmativeResponses());
    }

}
