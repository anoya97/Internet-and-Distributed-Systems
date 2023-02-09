package es.udc.ws.app.model.response;

import java.sql.*;

public class JdbcCreatesSqlResponseDao extends AbstractSqlResponseDao{

    public Response create(Connection connection, Response response){

        String queryString = "INSERT INTO Response"
                + " (employeeEmail, eventId, confirmation, responseDate)"
                + " VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, response.getEmployeeEmail());
            preparedStatement.setLong(i++, response.getEventId());
            preparedStatement.setBoolean(i++, response.isConfirmation());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(response.getResponseDate()));

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key.");
            }

            Long responseId = resultSet.getLong(1);

            /* Return event. */

            return new Response(responseId, response.getEmployeeEmail(), response.getEventId(), response.isConfirmation(), response.getResponseDate());

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}