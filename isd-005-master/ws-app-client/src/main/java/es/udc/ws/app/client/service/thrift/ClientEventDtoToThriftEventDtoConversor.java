package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.thrift.ThriftEventDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientEventDtoToThriftEventDtoConversor {

    public static ThriftEventDto toThriftEventDto(ClientEventDto clientEventDto) {

        Long eventId = clientEventDto.getEventId();
        LocalDateTime celebrationDate = clientEventDto.getCelebrationDate();
        LocalDateTime endDate = clientEventDto.getEndDate();
        
        return new ThriftEventDto(
                eventId == null ? -1 : eventId.longValue(),
                clientEventDto.getEname(),
                clientEventDto.getDescription(),
                Duration.between(celebrationDate, endDate).toHours(),
                celebrationDate.toString(),
                clientEventDto.isActiveEvent(),
                clientEventDto.getAfirmativeResponses(),
                clientEventDto.getTotalResponses()
        );
    }

    public static List<ClientEventDto> toClientEventDtos(List<ThriftEventDto> events){

        List<ClientEventDto> clientEventDtos = new ArrayList<>(events.size());

        for (ThriftEventDto event : events) {
            clientEventDtos.add(toClientEventDto(event));
        }

        return clientEventDtos;
    }


    private static ClientEventDto toClientEventDto(ThriftEventDto event) {

        return new ClientEventDto(
                event.getEventId(),
                event.getEname(),
                event.getDescription(),
                LocalDateTime.parse(event.getCelebrationDate()).withNano(0),
                LocalDateTime.parse(event.getCelebrationDate()).plusHours((long) event.getDuration()),
                event.isActiveEvent(),
                event.getAfirmativeResponses(),
                event.getTotalResponses()
        );


    }

}
