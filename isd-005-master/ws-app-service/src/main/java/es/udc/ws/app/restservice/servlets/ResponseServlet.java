package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.eventservice.EventServiceFactory;
import es.udc.ws.app.model.eventservice.exceptions.AlreadyResponsedException;
import es.udc.ws.app.model.eventservice.exceptions.EventCanceledException;
import es.udc.ws.app.model.eventservice.exceptions.LimitDateException;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.restservice.dto.ResponseToRestResponseDtoConversor;
import es.udc.ws.app.restservice.dto.RestResponseDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestResponseDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ResponseServlet extends RestHttpServletTemplate {
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        String employeeEmail  = ServletUtils.getMandatoryParameter(req, "employeeEmail");
        boolean allResponses = Boolean.parseBoolean(ServletUtils.getMandatoryParameter(req, "allResponses"));

        List<Response> responses = EventServiceFactory.getService().getEmployeeResponses(employeeEmail,allResponses);

        List<RestResponseDto> responseDtos = ResponseToRestResponseDtoConversor.toRestResponseDtos(responses);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestResponseDtoConversor.toArrayNode(responseDtos), null);
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String employeeEmail  = ServletUtils.getMandatoryParameter(req, "employeeEmail");
        Long eventId  = ServletUtils.getMandatoryParameterAsLong(req, "eventId");
        boolean confirmation = Boolean.parseBoolean(ServletUtils.getMandatoryParameter(req, "confirmation"));

        Response response;

        try {
            response = EventServiceFactory.getService().respondToEvent(employeeEmail,eventId,confirmation);
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
        } catch (AlreadyResponsedException ex){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                    AppExceptionToJsonConversor.toAlreadyResponsedException(ex),
                    null);
            return;
        }

        RestResponseDto responseDto = ResponseToRestResponseDtoConversor.toRestResponseDto(response);

        String responseURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + response.getResponseId().toString();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", responseURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestResponseDtoConversor.toObjectNode(responseDto), headers);

    }



}
