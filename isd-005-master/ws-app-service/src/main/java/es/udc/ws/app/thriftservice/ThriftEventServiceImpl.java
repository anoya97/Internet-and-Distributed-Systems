package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventservice.EventServiceFactory;
import es.udc.ws.app.thrift.ThriftEventDto;
import es.udc.ws.app.thrift.ThriftEventService;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.util.exceptions.InputValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ThriftEventServiceImpl implements ThriftEventService.Iface {


    @Override
    public ThriftEventDto addEvent(ThriftEventDto eventDto) throws ThriftInputValidationException {

        Event event = EventToThriftEventDtoConversor.toEvent(eventDto);

        try {

            if (event.getCelebrationDate().isBefore(LocalDateTime.now())){
                throw new InputValidationException("Cannot enter a date in the past: " + event.getCelebrationDate());
            }

            Event addedEvent = EventServiceFactory.getService().addEvent(event);
            return EventToThriftEventDtoConversor.toThriftEventDto(addedEvent);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

    @Override
    public List<ThriftEventDto> findEvents(String endDate, String keyword) throws ThriftInputValidationException {

        try {

            if (LocalDate.parse(endDate).isBefore(LocalDate.now())){
                throw new InputValidationException("Cannot enter a date in the past: " + endDate);
            }

            List<Event> events = EventServiceFactory.getService().findEvents(LocalDate.now(), LocalDate.parse(endDate), keyword);

            return EventToThriftEventDtoConversor.toThriftEventDtos(events);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }

    }

}
