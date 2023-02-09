package es.udc.ws.app.model.event;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractSqlEventDao implements SqlEventDao {

    protected AbstractSqlEventDao() {
    }

    @Override
    public Event find(Connection connection, Long eventId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT ename, description, "
                + " celebrationDate, duration, registerDate, activeEvent, afirmativeResponses, negativeResponses FROM Event WHERE eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, eventId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(eventId,
                        Event.class.getName());
            }

            /* Get results. */
            i = 1;
            String ename = resultSet.getString(i++);
            String description = resultSet.getString(i++);
            Timestamp celebrationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime celebrationDate = celebrationDateAsTimestamp.toLocalDateTime();
            float duration = resultSet.getFloat(i++);
            Timestamp registerDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime registerDate = registerDateAsTimestamp.toLocalDateTime();
            boolean activeEvent = resultSet.getBoolean(i++);
            int afirmativeResponses = resultSet.getInt(i++);
            int negativeResponses = resultSet.getInt(i++);

            /* Return Event. */
            return new Event(eventId, ename, description, celebrationDate, duration,
                    registerDate, activeEvent, afirmativeResponses, negativeResponses);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Event> findByDate(Connection connection, LocalDate start, LocalDate end, String keyword){
        /* Create "queryString". */

        String queryString = "SELECT eventId, ename, description, celebrationDate, duration, registerDate, activeEvent, afirmativeResponses, negativeResponses "
                + "FROM Event "
                + "WHERE (celebrationDate >= ?) AND (celebrationDate BETWEEN ? AND ?)";
        if(keyword != null){
            queryString += " AND LOWER(description) LIKE LOWER(?)";
        }
        queryString += " ORDER BY ename";


        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int j = 1;
            preparedStatement.setTimestamp(j++, Timestamp.valueOf(LocalDateTime.now().withNano(0)));
            preparedStatement.setTimestamp(j++, Timestamp.valueOf(start.atStartOfDay().withNano(0)));
            preparedStatement.setTimestamp(j++, Timestamp.valueOf(end.atStartOfDay().withNano(0)));
            if (keyword != null){
                preparedStatement.setString(j++, "%" + keyword + "%");
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read movies. */
            List<Event> events = new ArrayList<>();

            while (resultSet.next()) {

                int i = 1;
                Long eventId = Long.valueOf(resultSet.getLong(i++));
                String ename = resultSet.getString(i++);
                String description = resultSet.getString(i++);
                Timestamp celebrationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime celebrationDate = celebrationDateAsTimestamp.toLocalDateTime();
                float duration = resultSet.getFloat(i++);
                Timestamp registerDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime registerDate = registerDateAsTimestamp.toLocalDateTime();
                boolean activeEvent = resultSet.getBoolean(i++);
                int afirmativeResponses = resultSet.getInt(i++);
                int negativeResponses = resultSet.getInt(i++);

                events.add(new Event(eventId, ename, description, celebrationDate,
                        duration, registerDate, activeEvent, afirmativeResponses, negativeResponses));

            }

            /* Return movies. */
            return events;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEvent(Connection connection, Event event)
            throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "UPDATE Event"
                + " SET ename = ?, description = ?, celebrationDate = ?, duration = ?, registerDate = ?, "
                + "activeEvent = ?, afirmativeResponses = ?, negativeResponses = ? WHERE eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

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
            preparedStatement.setLong(i++, event.getEventId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(event.getEventId(),
                        Event.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long eventId)
            throws InstanceNotFoundException{
        /* Create "queryString". */
        String queryString = "DELETE FROM Event WHERE" + " eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, eventId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(eventId,
                        Event.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}
