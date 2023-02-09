package es.udc.ws.app.model.response;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSqlResponseDao implements SqlResponseDao {

    protected AbstractSqlResponseDao() {
    }

    @Override
    public boolean existsByEmailAndEventId(Connection connection, String employeeEmail, Long eventId) {

        /* Create "queryString". */
        String queryString = "SELECT COUNT(*) FROM Response WHERE employeeEmail = ? AND eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, employeeEmail);
            preparedStatement.setLong(i++, eventId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new SQLException("Error retrieving the number of responses for the event with id " + eventId + " and employee with email: " + employeeEmail);
            }

            /* Get results. */
            i = 1;
            Long numberOfResponses = resultSet.getLong(i++);

            /* Return result. */
            return numberOfResponses > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Response> getEmployeeResponses(Connection connection, String employeeEmail,boolean allResponses) {

        List<Response> responses = new LinkedList<>();
        String queryString = "SELECT responseId, eventId,confirmation,responseDate FROM Response WHERE employeeEmail = ?";

        if(!allResponses) {
            queryString += " AND confirmation = 1";
        }
        queryString += " ORDER BY eventId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int i = 1;
            preparedStatement.setString(i++, employeeEmail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                i = 1;
                Long responseId = Long.valueOf(resultSet.getLong(i++));
                Long eventId = Long.valueOf(resultSet.getLong(i++));
                boolean confirmation = resultSet.getBoolean(i++);
                Timestamp expirationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime responseDate = expirationDateAsTimestamp.toLocalDateTime();

                /* Return sale. */
                responses.add(new Response(responseId, employeeEmail, eventId, confirmation, responseDate));

            }
            return responses;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long responseId)
            throws InstanceNotFoundException{

        /* Create "queryString". */
        String queryString = "DELETE FROM Response WHERE" + " responseId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, responseId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(responseId,
                        Response.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}