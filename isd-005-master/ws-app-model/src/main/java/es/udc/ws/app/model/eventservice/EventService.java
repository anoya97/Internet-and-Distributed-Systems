package es.udc.ws.app.model.eventservice;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventservice.exceptions.*;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    public Event addEvent(Event event) throws InputValidationException;
    public Event findEvent(Long eventId) throws InstanceNotFoundException;
    public List<Event> findEvents(LocalDate start, LocalDate end, String keywords) throws InputValidationException;
    public Response respondToEvent(String employeeEmail, Long eventId, boolean confirmation)
            throws InstanceNotFoundException, InputValidationException, EventCanceledException, LimitDateException, AlreadyResponsedException;
    public void cancelEvent(Long eventId)
            throws InstanceNotFoundException, EventCanceledException, LimitDateException, InputValidationException;
    public List<Response> getEmployeeResponses(String employeeEmail,boolean all) throws InputValidationException;
}