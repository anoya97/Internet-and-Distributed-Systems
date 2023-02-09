package es.udc.ws.app.model.event;

import java.sql.*;


public class JdbcCreateSqlEventDao extends AbstractSqlEventDao{

    @Override
    public Event create(Connection connection, Event event){

        String queryString = "INSERT INTO Event"
                + " (ename, description, celebrationDate, duration, registerDate, activeEvent, afirmativeResponses, negativeResponses)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS)){

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, event.getEname());
            preparedStatement.setString(i++, event.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getCelebrationDate()));
            preparedStatement.setFloat(i++, event.getDuration());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(event.getRegisterDate()));
            preparedStatement.setBoolean(i++, event.isActiveEvent());
            preparedStatement.setInt(i++, event.getAfirmativeResponses());
            preparedStatement.setInt(i++, event.getNegativeResponses());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()){
                throw new SQLException("JDBC driver did not return generated key.");
            }

            Long eventId = resultSet.getLong(1);

            /* Return event. */

            return new Event(eventId, event.getEname(), event.getDescription(), event.getCelebrationDate(), event.getDuration(),
                    event.getRegisterDate(), event.getAfirmativeResponses(), event.getNegativeResponses());

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

}