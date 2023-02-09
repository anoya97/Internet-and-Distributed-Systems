package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.eventservice.exceptions.AlreadyResponsedException;
import es.udc.ws.app.model.eventservice.exceptions.EventCanceledException;
import es.udc.ws.app.model.eventservice.exceptions.LimitDateException;

public class AppExceptionToJsonConversor {

    public static ObjectNode toAlreadyResponsedException(AlreadyResponsedException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyResponsed");
        exceptionObject.put("eventId", (ex.getEventId() != null) ? ex.getEventId() : null);

        return exceptionObject;
    }

    public static ObjectNode toEventCanceledException(EventCanceledException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "EventCanceled");
        exceptionObject.put("eventId", (ex.getEventId() != null) ? ex.getEventId() : null);

        return exceptionObject;
    }

    public static ObjectNode toLimitDateException(LimitDateException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "LimitDate");
        exceptionObject.put("eventId", (ex.getEventId() != null) ? ex.getEventId() : null);

        return exceptionObject;
    }

}
