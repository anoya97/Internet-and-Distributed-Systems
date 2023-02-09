package es.udc.ws.app.model.eventservice;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.event.SqlEventDao;
import es.udc.ws.app.model.event.SqlEventDaoFactory;
import es.udc.ws.app.model.eventservice.exceptions.*;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.app.model.response.SqlResponseDao;
import es.udc.ws.app.model.response.SqlResponseDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.*;

public class EventServiceImpl implements EventService {

    private final DataSource dataSource;
    private SqlEventDao eventDao = null;
    private SqlResponseDao responseDao = null;

    public EventServiceImpl(){
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        eventDao = SqlEventDaoFactory.getDao();
        responseDao = SqlResponseDaoFactory.getDao();
    }

    private void validateEvent(Event event) throws InputValidationException {

        PropertyValidator.validateMandatoryString("ename", event.getEname());
        PropertyValidator.validateMandatoryString("description", event.getDescription());
        PropertyValidator.validateDouble("duration", event.getDuration(), 0, MAX_DURATION);

    }

    @Override
    public Event addEvent(Event event) throws InputValidationException {

        validateEvent(event);
        event.setRegisterDate(LocalDateTime.now());
        event.setActiveEvent(true);
        event.setAfirmativeResponses(0);
        event.setNegativeResponses(0);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Event createdEvent = eventDao.create(connection, event);

                /* Commit. */
                connection.commit();

                return createdEvent;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Event findEvent(Long eventId) throws InstanceNotFoundException{

        try (Connection connection = dataSource.getConnection()) {
            return eventDao.find(connection, eventId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> findEvents(LocalDate start, LocalDate end, String keywords) throws InputValidationException{

        try (Connection connection = dataSource.getConnection()) {

            /* Solo puede haber una palabra clave */
            String[] words = keywords != null ? keywords.split(" ") : null;
            if (words != null && words.length>1){
                throw new InputValidationException(keywords);
            }
            if (start == null){
                throw new InputValidationException("Not valid start date");
            }
            if (end == null){
                throw new InputValidationException("Not valid end date");
            }

            return eventDao.findByDate(connection, start, end, keywords);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Response respondToEvent(String employeeEmail, Long eventId, boolean confirmation)
            throws InstanceNotFoundException, InputValidationException, EventCanceledException, LimitDateException, AlreadyResponsedException {


        PropertyValidator.validateMandatoryString("employeeEmail", employeeEmail);
        PropertyValidator.validateNotNegativeLong("eventId", eventId);


        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Event event = eventDao.find(connection, eventId);
                LocalDateTime responseDate = LocalDateTime.now();

                if (responseDate.isAfter(event.getCelebrationDate().minusHours(RESPONSE_LAST_HOURS))){
                    throw new LimitDateException(eventId);
                }
                if(responseDao.existsByEmailAndEventId(connection, employeeEmail, eventId)){
                    throw new AlreadyResponsedException(eventId);
                }
                if(!event.isActiveEvent()){
                    throw new EventCanceledException(eventId);
                }

                Response response = responseDao.create(connection, new Response(employeeEmail, eventId, confirmation, responseDate));
                if (confirmation) {
                    event.setAfirmativeResponses(event.getAfirmativeResponses() + 1);
                } else {
                    event.setNegativeResponses(event.getNegativeResponses() + 1);
                }

                eventDao.updateEvent(connection, event);

                /* Commit. */
                connection.commit();

                return response;

            } catch (InstanceNotFoundException | EventCanceledException | LimitDateException | AlreadyResponsedException e ) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cancelEvent(Long eventId)
            throws InstanceNotFoundException, LimitDateException, EventCanceledException, InputValidationException {

        PropertyValidator.validateNotNegativeLong("eventId", eventId);

        try (Connection connection = dataSource.getConnection()) {
            Event event = findEvent(eventId);
            LocalDateTime actual = LocalDateTime.now();
            if (!event.isActiveEvent()) {
                throw new EventCanceledException(eventId);
            }else if(actual.isAfter(event.getCelebrationDate())){
                throw new LimitDateException(eventId);
            }else{
                event.setActiveEvent(false);
                eventDao.updateEvent(connection, event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Response> getEmployeeResponses(String employeeEmail,boolean all) throws InputValidationException {

        PropertyValidator.validateMandatoryString("employeeEmail", employeeEmail);

        try (Connection connection = dataSource.getConnection()) {
            return responseDao.getEmployeeResponses(connection, employeeEmail, all);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
