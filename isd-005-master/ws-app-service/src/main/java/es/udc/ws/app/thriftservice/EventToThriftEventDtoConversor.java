package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftEventDto;
import es.udc.ws.app.model.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventToThriftEventDtoConversor {

    public static Event toEvent(ThriftEventDto eventDto){
        return new Event(eventDto.getEventId(), eventDto.getEname(), eventDto.getDescription(),
                Double.valueOf(eventDto.getDuration()).floatValue(), LocalDateTime.parse(eventDto.getCelebrationDate()), eventDto.isActiveEvent(),
                eventDto.getAfirmativeResponses(), eventDto.getTotalResponses() - eventDto.getAfirmativeResponses());
    }

    public static List<ThriftEventDto> toThriftEventDtos(List<Event> events) {

        List<ThriftEventDto> dtos = new ArrayList<>(events.size());

        for (Event event : events) {
            dtos.add(toThriftEventDto(event));
        }
        return dtos;

    }

    public static ThriftEventDto toThriftEventDto(Event event) {
        return new ThriftEventDto(event.getEventId(), event.getEname(), event.getDescription(), event.getDuration(),
                event.getCelebrationDate().toString(), event.isActiveEvent(), event.getAfirmativeResponses(),
                event.getAfirmativeResponses() + event.getNegativeResponses());
    }

}
