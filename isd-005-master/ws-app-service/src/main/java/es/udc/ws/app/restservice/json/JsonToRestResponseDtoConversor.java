package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestResponseDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestResponseDtoConversor {

    public static ObjectNode toObjectNode(RestResponseDto response){

        ObjectNode responseObject = JsonNodeFactory.instance.objectNode();

        if (response.getResponseId() != null) {
            responseObject.put("responseId", response.getResponseId());
        }

        responseObject.put("employeeEmail", response.getEmployeeEmail()).
                put("eventId", response.getEventId()).
                put("confirmation", response.isConfirmation());

        return responseObject;
    }

    public static ArrayNode toArrayNode(List<RestResponseDto> reponses){

        ArrayNode responseNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < reponses.size(); i++){
            RestResponseDto responseDto = reponses.get(i);
            ObjectNode responseObject = toObjectNode(responseDto);
            responseNode.add(responseObject);
        }

        return responseNode;
    }

    public static RestResponseDto toRestResponseDto(InputStream jsonResponse) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode responseObject = (ObjectNode) rootNode;

                JsonNode responseIdNode = responseObject.get("responseId");
                Long responseId = (responseIdNode != null) ? responseIdNode.longValue() : null;

                String employeeEmail = responseObject.get("employeeEmail").textValue().trim();
                Long eventId = responseObject.get("eventId").longValue();
                boolean confirmation = responseObject.get("confirmation").asBoolean();

                return new RestResponseDto(responseId, employeeEmail, eventId, confirmation);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}

