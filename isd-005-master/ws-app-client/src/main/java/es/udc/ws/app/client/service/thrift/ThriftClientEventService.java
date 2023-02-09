package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientEventService;
import es.udc.ws.app.client.service.dto.ClientEventDto;
import es.udc.ws.app.client.service.dto.ClientResponseDto;
import es.udc.ws.app.client.service.exceptions.ClientAlreadyResponsedException;
import es.udc.ws.app.client.service.exceptions.ClientEventCanceledException;
import es.udc.ws.app.client.service.exceptions.ClientLimitDateException;
import es.udc.ws.app.thrift.ThriftEventService;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientEventService implements ClientEventService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientEventService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    @Override
    public Long addEvent(ClientEventDto event) throws InputValidationException {

        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{

            transport.open();

            return client.addEvent(ClientEventDtoToThriftEventDtoConversor.toThriftEventDto(event)).getEventId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    @Override
    public ClientEventDto findEvent(Long eventId) throws InstanceNotFoundException {
        return null;
    }

    @Override
    public List<ClientEventDto> findEvents(LocalDate end, String keyword) throws InputValidationException {

        ThriftEventService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try{

            transport.open();

            return ClientEventDtoToThriftEventDtoConversor.toClientEventDtos(client.findEvents(end.toString(), keyword));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    @Override
    public Long respondToEvent(String employeeEmail, Long eventId, boolean confirmation) throws InstanceNotFoundException, InputValidationException, ClientEventCanceledException, ClientLimitDateException, ClientAlreadyResponsedException {
        return null;
    }

    @Override
    public void cancelEvent(Long eventId) throws InstanceNotFoundException, ClientEventCanceledException, ClientLimitDateException, InputValidationException {

    }

    @Override
    public List<ClientResponseDto> getEmployeeResponses(String employeeEmail, boolean allResponses) throws InputValidationException {
        return null;
    }

    private ThriftEventService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftEventService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }

}
