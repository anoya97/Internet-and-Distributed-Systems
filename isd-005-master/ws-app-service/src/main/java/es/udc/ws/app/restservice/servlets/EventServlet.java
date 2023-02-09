package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.eventservice.EventServiceFactory;
import es.udc.ws.app.model.eventservice.exceptions.EventCanceledException;
import es.udc.ws.app.model.eventservice.exceptions.LimitDateException;
import es.udc.ws.app.restservice.dto.EventToRestEventDtoConversor;
import es.udc.ws.app.restservice.dto.RestEventDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestEventDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class EventServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if (!req.toString().contains("events/")) {
            ServletUtils.checkEmptyPath(req);

            RestEventDto eventDto = JsonToRestEventDtoConversor.toRestEventDto(req.getInputStream());
            Event event = EventToRestEventDtoConversor.toEvent(eventDto);

            event = EventServiceFactory.getService().addEvent(event);

            eventDto = EventToRestEventDtoConversor.toRestEventDto(event);
            String eventURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + event.getEventId();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", eventURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestEventDtoConversor.toObjectNode(eventDto), headers);
        } else {
            Long eventId = ServletUtils.getIdFromPath(req, "event");

            try {
                EventServiceFactory.getService().cancelEvent(eventId);
            } catch (LimitDateException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toLimitDateException(ex),
                        null);
                return;

            } catch (EventCanceledException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toEventCanceledException(ex),
                        null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
        }

    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {

        if(!req.toString().contains("endDate")){
            Long eventId = ServletUtils.getIdFromPath(req, "event");

            Event event = EventServiceFactory.getService().findEvent(eventId);
            RestEventDto eventDto = EventToRestEventDtoConversor.toRestEventDto(event);
            ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_OK,
                    JsonToRestEventDtoConversor.toObjectNode(eventDto),null);

        }else {
            ServletUtils.checkEmptyPath(req);
            LocalDate actual = LocalDate.now();
            LocalDate endDate = LocalDate.parse(req.getParameter("endDate"));
            String keyword = req.getParameter("keyword");
            List<Event> events = EventServiceFactory.getService().findEvents(actual, endDate, keyword);
            List<RestEventDto> eventDtos = EventToRestEventDtoConversor.toRestEventDtos(events);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestEventDtoConversor.toArrayNode(eventDtos), null);
        }
    }



}
