package es.udc.ws.app.model.response;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlResponseDao {
    public Response create(Connection connection, Response response);
    public boolean existsByEmailAndEventId(Connection connection, String employeeEmail, Long eventId);
    public List<Response> getEmployeeResponses(Connection connection, String employeeEmail, boolean allResponses);
    public void remove(Connection connection, Long responseId)
            throws InstanceNotFoundException;

}

