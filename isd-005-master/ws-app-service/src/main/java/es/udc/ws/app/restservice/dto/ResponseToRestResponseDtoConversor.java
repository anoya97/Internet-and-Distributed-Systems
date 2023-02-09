package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.response.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResponseToRestResponseDtoConversor {

    public static List<RestResponseDto> toRestResponseDtos(List<Response> responses){
        List<RestResponseDto> responseDto = new ArrayList<>(responses.size());
        for (Response response : responses) {
            responseDto.add(toRestResponseDto(response));
        }
        return responseDto;
    }

    public static RestResponseDto toRestResponseDto(Response response) {
        return new RestResponseDto(response.getResponseId(), response.getEmployeeEmail(), response.getEventId(),
                response.isConfirmation());
    }

    public static Response toResponse(RestResponseDto responseDto) {
        return new Response(responseDto.getResponseId(), responseDto.getEmployeeEmail(), responseDto.getEventId(),
                responseDto.isConfirmation());
    }

}
