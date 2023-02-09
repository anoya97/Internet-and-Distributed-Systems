package es.udc.ws.app.model.event;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface SqlEventDao {

    public Event create(Connection connection, Event event);
    public Event find(Connection connection, Long eventId)
            throws InstanceNotFoundException;
    public List<Event> findByDate(Connection connection, LocalDate start, LocalDate end, String keyword);
    public void remove(Connection connection, Long eventId)
            throws InstanceNotFoundException;
    public void updateEvent(Connection connection, Event event)
            throws InstanceNotFoundException;

}
