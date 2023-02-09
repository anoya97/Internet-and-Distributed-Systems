package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class  JsonToClientEventDtoConversor {

    public static ObjectNode toObjectNode(ClientEventDto event) throws IOException {

        ObjectNode eventObject = JsonNodeFactory.instance.objectNode();

        if (event.getEventId() != null) {
            eventObject.put("eventId", event.getEventId());
        }
        eventObject.put("ename", event.getEname()).
                put("description", event.getDescription()).
                put("celebrationDate", event.getCelebrationDate().toString()).
                put("duration", Duration.between(event.getCelebrationDate(), event.getEndDate()).toHours()).
                put("activeEvent", event.isActiveEvent()).
                put("afirmativeResponses", event.getAfirmativeResponses()).
                put("totalResponses", event.getTotalResponses());

        return eventObject;
    }

    public static ClientEventDto toClientEventDto(InputStream jsonEvent) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEvent);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientEventDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientEventDto> toClientEventDtos(InputStream jsonEvents) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEvents);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode eventsArray = (ArrayNode) rootNode;
                List<ClientEventDto> eventDtos = new ArrayList<>(eventsArray.size());
                for (JsonNode eventNode : eventsArray) {
                    eventDtos.add(toClientEventDto(eventNode));
                }

                return eventDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientEventDto toClientEventDto(JsonNode eventNode) throws ParsingException {
        if (eventNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode eventObject = (ObjectNode) eventNode;

            JsonNode eventIdNode = eventObject.get("eventId");
            Long eventId = (eventIdNode != null) ? eventIdNode.longValue() : null;

            String ename = eventObject.get("ename").textValue().trim();
            String description = eventObject.get("description").textValue().trim();
            String celebrationDate = eventObject.get("celebrationDate").textValue().trim();
            long duration = eventObject.get("duration").longValue();
            boolean activeEvent = eventObject.get("activeEvent").booleanValue();
            int afirmativeResponses = eventObject.get("afirmativeResponses").intValue();
            int totalResponses = eventObject.get("totalResponses").intValue();

            return new ClientEventDto(eventId, ename, description, LocalDateTime.parse(celebrationDate),
                    LocalDateTime.parse(celebrationDate).plusHours(duration), activeEvent,
                    afirmativeResponses, totalResponses);
        }
    }

}
