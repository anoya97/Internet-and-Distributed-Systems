package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyResponsedException;
import es.udc.ws.app.client.service.exceptions.ClientEventCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientLimitDateException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClientEventService {

    public Long addEvent(ClientEventDto event) throws InputValidationException;
    public ClientEventDto findEvent(Long eventId) throws InstanceNotFoundException;
    public List<ClientEventDto> findEvents(LocalDate end, String keyword) throws InputValidationException;
    public Long respondToEvent(String employeeEmail, Long eventId, boolean confirmation)
            throws InstanceNotFoundException, InputValidationException, ClientEventCanceledException, ClientLimitDateException, ClientAlreadyResponsedException;
    public  void cancelEvent(Long eventId)
            throws InstanceNotFoundException, ClientEventCanceledException, ClientLimitDateException, InputValidationException;
    public List<ClientResponseDto> getEmployeeResponses(String employeeEmail, boolean allResponses) throws InputValidationException;

}
