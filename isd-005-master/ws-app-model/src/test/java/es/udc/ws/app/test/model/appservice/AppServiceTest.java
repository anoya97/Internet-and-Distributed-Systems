package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.event.Event;
import es.udc.ws.app.model.event.SqlEventDao;
import es.udc.ws.app.model.event.SqlEventDaoFactory;
import es.udc.ws.app.model.eventservice.EventService;
import es.udc.ws.app.model.eventservice.EventServiceFactory;
import es.udc.ws.app.model.eventservice.exceptions.*;
import es.udc.ws.app.model.response.SqlResponseDao;
import es.udc.ws.app.model.response.SqlResponseDaoFactory;
import es.udc.ws.app.model.response.Response;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_DURATION;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private static EventService eventService = null;
    private final long NON_EXISTENT_EVENT_ID = -1;
    private final long NON_EXISTENT_EVENT = 1233242321;
    private static SqlResponseDao responseDao = null;
    private final int DURATION = 10;


    @BeforeAll
    public static void init() {

        /*
         * Create a simple data source and add it to "DataSourceLocator" (this
         * is needed to test "es.udc.ws.event.model.eventservice.EventService"
         */
        DataSource dataSource = new SimpleDataSource();

        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);

        eventService = EventServiceFactory.getService();

        responseDao = SqlResponseDaoFactory.getDao();
    }

    private Event getValidEvent(String ename) {
        LocalDateTime date = LocalDateTime.parse("2023-12-12T11:25");
        return new Event(ename, "Event description",date ,DURATION);
    }

    private Event getValidEvent() {
        return getValidEvent("Event title");
    }

    private void removeEvent(Long eventId) {

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        SqlEventDao eventDao = SqlEventDaoFactory.getDao();

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                eventDao.remove(connection, eventId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeResponse(Long responseId) throws InstanceNotFoundException{

        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection.*/
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work.*/
                responseDao.remove(connection, responseId);

                /* Commit.*/
                connection.commit();

            } catch (InstanceNotFoundException e) {
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


    private Event createEvent(Event event) {

        Event addedEvent = null;
        try {
            addedEvent = eventService.addEvent(event);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedEvent;

    }

    @Test
    public void testAddEventAndFindEvent() throws InputValidationException, InstanceNotFoundException {

        Event event = getValidEvent();
        Event addedEvent = null;

        try {

            // Create Event
            LocalDateTime beforeRegisterDate = LocalDateTime.now().withNano(0);

            addedEvent = eventService.addEvent(event);

            LocalDateTime afterRegisternDate = LocalDateTime.now().withNano(0);

            // Find Event
            Event foundEvent = eventService.findEvent(addedEvent.getEventId());

            assertEquals(addedEvent, foundEvent);
            assertEquals(foundEvent.getEname(),event.getEname());
            assertEquals(foundEvent.getDescription(),event.getDescription());
            assertEquals(foundEvent.getCelebrationDate(), event.getCelebrationDate());
            assertTrue((foundEvent.getRegisterDate().compareTo(beforeRegisterDate) >= 0)
                    && (foundEvent.getRegisterDate().compareTo(afterRegisternDate) <= 0));

        } finally {
            // Clear Database
            if (addedEvent!=null) {
                removeEvent(addedEvent.getEventId());
            }
        }
    }

    @Test
    public void testAddInvalidEvent(){
        // Check event name not null
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setEname(null);
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event name not empty
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setEname("");
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event description not null
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDescription(null);
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        // Check event description not empty
        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDescription("");
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDuration((short) -1);
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

        assertThrows(InputValidationException.class, () -> {
            Event event = getValidEvent();
            event.setDuration((short) (MAX_DURATION + 1));
            Event addedEvent = eventService.addEvent(event);
            removeEvent(addedEvent.getEventId());
        });

    }

    @Test
    public void testFindNonExistentEvent() {
        assertThrows(InstanceNotFoundException.class, () -> eventService.findEvent(NON_EXISTENT_EVENT_ID));
    }

    @Test
    public void testFindEvents() throws InputValidationException {

        // Add events
        List<Event> events = new LinkedList<Event>();
        Event event1 = createEvent(getValidEvent("event title 1"));
        events.add(event1);
        LocalDateTime date = LocalDateTime.parse("2023-12-12T11:25");
        LocalDateTime date2 = LocalDateTime.now().minusDays(1);
        Event event2 = createEvent(new Event("event title 2", "Este evento es una prueba",date ,DURATION));
        events.add(event2);
        Event event3 = createEvent(new Event("event title 3", "Este evento no es normal",date ,DURATION));
        events.add(event3);


        LocalDate start1 = LocalDate.parse("2023-09-09");
        LocalDate end1 = LocalDate.parse("2024-09-09");
        LocalDate start2 = LocalDate.parse("2021-10-10");
        LocalDate end2 = LocalDate.parse("2021-12-10");
        LocalDate start3 = LocalDate.parse("2022-09-09");

        try {
            List<Event> foundEvents = eventService.findEvents(start1, end1, null);
            assertEquals(events, foundEvents);

            foundEvents = eventService.findEvents(start2, end2, null);
            assertEquals(0, foundEvents.size());

            Event event4 = createEvent(new Event("event title 4",  "Este evento no es normal", date2, DURATION));
            events.add(event4);
            foundEvents = eventService.findEvents(start3, end1, null);
            assertEquals(3, foundEvents.size());
            assertEquals(events.get(0), foundEvents.get(0));
            assertEquals(events.get(1), foundEvents.get(1));
            assertEquals(events.get(2), foundEvents.get(2));

            foundEvents = eventService.findEvents(start1, end1, "prueba");
            assertEquals(1, foundEvents.size());
            assertEquals(events.get(1), foundEvents.get(0));

            foundEvents = eventService.findEvents(start1, end1, "evento");
            assertEquals(2, foundEvents.size());
            assertEquals(events.get(1), foundEvents.get(0));
            assertEquals(events.get(2), foundEvents.get(1));

        } finally {
            // Clear Database
            for (Event event : events) {
                removeEvent(event.getEventId());
            }
        }

    }

    @Test
    public void testInvalidKeywordAndDates(){

        assertThrows(InputValidationException.class, () -> {

            List<Event> events = new LinkedList<Event>();
            Event event1 = createEvent(getValidEvent("event title 1"));
            events.add(event1);
            LocalDateTime date = LocalDateTime.parse("2020-10-10T11:25");
            Event event2 = createEvent(new Event("event title 2", "pruebas y pruebas en este fichero", date, DURATION));
            events.add(event2);
            Event event3 = createEvent(getValidEvent("event title 3"));
            events.add(event3);

            LocalDate start1 = LocalDate.parse("2020-09-09");
            LocalDate end1 = LocalDate.parse("2020-12-12");

            try {
                eventService.findEvents(start1, end1, "pruebas y pruebas");
            }finally{
                for (Event event : events) {
                    removeEvent(event.getEventId());
                }
            }

        });

        assertThrows(InputValidationException.class, () -> {
            LocalDate date = LocalDate.parse("2020-09-09");
            eventService.findEvents(null, date, "prueba");
        });

        assertThrows(InputValidationException.class, () -> {
            LocalDate date = LocalDate.parse("2020-09-09");
            eventService.findEvents(date, null, "prueba");
        });

    }

    @Test
    public void testRespondEvent()
            throws InstanceNotFoundException, InputValidationException, AlreadyResponsedException, LimitDateException, EventCanceledException {
        LocalDateTime date = LocalDateTime.parse("2023-12-10T11:25");
        Event event = createEvent(new Event("event title 1", "Este evento es una prueba", date,DURATION));
        Response response1 = null;
        Response response2 = null;
        Response response3 = null;

        try {

            // Respond event
            LocalDateTime beforeResponseDate = LocalDateTime.now().withNano(0);

            response1 = eventService.respondToEvent("raul@udc.es", event.getEventId(), true);
            response2 = eventService.respondToEvent("armando@udc.es", event.getEventId(), true);
            response3 = eventService.respondToEvent("brais@udc.es", event.getEventId(), false);

            LocalDateTime afterResponseDate = LocalDateTime.now().withNano(0);

            // Find response
            Response foundResponse = eventService.getEmployeeResponses(response1.getEmployeeEmail(), true).get(0);
            event = eventService.findEvent(event.getEventId());

            // Check response
            assertEquals(response1, foundResponse);
            assertEquals(response1.getEmployeeEmail(), "raul@udc.es");
            assertEquals(response1.getEventId(), event.getEventId());
            assertTrue(response1.isConfirmation());
            assertTrue((foundResponse.getResponseDate().compareTo(beforeResponseDate) >= 0) && (foundResponse.getResponseDate().compareTo(afterResponseDate) <= 0));
            assertEquals(2, event.getAfirmativeResponses());
            assertEquals(1, event.getNegativeResponses());

        } finally {
            // Clear database: remove response (if created) and event
            if (response1 != null) {
                removeResponse(response1.getResponseId());
            }
            if (response2 != null) {
                removeResponse(response2.getResponseId());
            }
            if (response3 != null) {
                removeResponse(response3.getResponseId());
            }

            removeEvent(event.getEventId());
        }
    }

    @Test
    public void testInvalidResponses(){

        LocalDateTime date3 = LocalDateTime.parse("2021-12-10T11:25");
        Event event1 = createEvent(new Event("event title 2", "Este evento es una prueba", date3,DURATION));
        LocalDateTime date = LocalDateTime.parse("2023-12-10T11:25");
        Event event2 = createEvent(new Event("event title 2", "Este evento es una prueba", date,DURATION));
        LocalDateTime date2 = LocalDateTime.now().plusHours(10);
        Event event3 = createEvent(new Event("event title 3", "Este evento es una prueba", date2,DURATION));

        String employeeEmail = "raul@udc.es";
        try {
            assertThrows(LimitDateException.class, () -> {  //Respuesta después del evento
                Response response = eventService.respondToEvent(employeeEmail, event1.getEventId(), true);
                removeResponse(response.getResponseId());
            });

            assertThrows(LimitDateException.class, () -> {  //Respuesta 10 horas antes de que se celebre
                Response response = eventService.respondToEvent(employeeEmail, event3.getEventId(), true);
                removeResponse(response.getResponseId());
            });

            assertThrows(EventCanceledException.class, () -> { //Respuesta a un evento cancelado
                eventService.cancelEvent(event2.getEventId());
                Response response = eventService.respondToEvent(employeeEmail, event2.getEventId(), true);
                removeResponse(response.getResponseId());
            });

            assertThrows(InstanceNotFoundException.class, () -> { //Respuesta a un evento imposible
                Response response = eventService.respondToEvent(employeeEmail, NON_EXISTENT_EVENT, true);
                removeResponse(response.getResponseId());
            });

            assertThrows(InputValidationException.class, () -> { //Respuesta a un evento inexistente
                Response response = eventService.respondToEvent(employeeEmail, NON_EXISTENT_EVENT_ID, true);
                removeResponse(response.getResponseId());
            });

        } finally {
            // Clear database
            removeEvent(event1.getEventId());
            removeEvent(event2.getEventId());
            removeEvent(event3.getEventId());
        }

    }

    @Test
    public void testMoreThanOneResponse()
            throws InstanceNotFoundException, InputValidationException, AlreadyResponsedException,EventCanceledException,LimitDateException{

        LocalDateTime date2 = LocalDateTime.parse("2023-12-10T11:25");
        Event event = createEvent(new Event("event title 2", "Este evento es otra prueba", date2,DURATION));
        String employeeEmail = "raul@udc.es";

        Response response = eventService.respondToEvent(employeeEmail, event.getEventId(), true);

        try {
            assertThrows(AlreadyResponsedException.class, () -> {
                Response response2 = eventService.respondToEvent(employeeEmail, event.getEventId(), true);
                removeResponse(response2.getResponseId());
            });
        }finally {
            removeResponse(response.getResponseId());
            removeEvent(event.getEventId());
        }

    }


    @Test
    public void testCancelEvent() throws InstanceNotFoundException,EventCanceledException , LimitDateException, InputValidationException {
        LocalDateTime date2 = LocalDateTime.parse("2023-12-10T11:25");
        Event event = createEvent(new Event("event title 1","Este evento es otra prueba", date2,DURATION));//NO CANCELABLE POR ANTIGUA2

        eventService.cancelEvent(event.getEventId());
        Event event2 = eventService.findEvent(event.getEventId());
        assertFalse(event2.isActiveEvent());
        removeEvent(event2.getEventId());

    }


    @Test
    public void testInvalidCancelEvent() {

        LocalDateTime date = LocalDateTime.parse("2020-12-10T11:25");
        LocalDateTime date2 = LocalDateTime.parse("2023-12-10T11:25");
        Event event1 = createEvent(new Event("event title 1","Este evento es otra prueba", date,DURATION));//NO CANCELABLE POR ANTIGUA
        Event event2 = createEvent(new Event("event title 2","Este evento es otra prueba", date2,DURATION));//NO CANCELABLE POR YA ESTAR CANCELADO


        try{
            assertThrows(LimitDateException.class, () -> {
                eventService.cancelEvent(event1.getEventId());
            });

            assertThrows(EventCanceledException.class, () -> {
                eventService.cancelEvent(event2.getEventId());
                Event event3 = eventService.findEvent(event2.getEventId());
                eventService.cancelEvent(event3.getEventId());
            });

            assertThrows(InstanceNotFoundException.class, () -> {
                eventService.cancelEvent(NON_EXISTENT_EVENT);
            });

            assertThrows(InputValidationException.class, () -> {
                eventService.cancelEvent(NON_EXISTENT_EVENT_ID);
            });

        }finally {
            removeEvent(event1.getEventId());
            removeEvent(event2.getEventId());
        }
    }



    @Test
    public void testEmployeeResponses()
            throws InstanceNotFoundException, InputValidationException,AlreadyResponsedException,EventCanceledException,LimitDateException{
        LocalDateTime date = LocalDateTime.parse("2025-12-10T11:25");
        LocalDateTime date2 = LocalDateTime.parse("2023-12-10T11:25");
        LocalDateTime date3 = LocalDateTime.parse("2024-12-10T11:25");
        String employeeEmail = "raul@udc.es";
        List<Event> events = new LinkedList<>();
        List<Response> allResponses = new LinkedList<>();
        List<Response> afirmativeResponses = new LinkedList<>();

        Event event1 = createEvent(new Event("event title 1","Este evento es otra prueba", date,DURATION));
        Event event2 = createEvent(new Event("event title 2","Este evento es otra prueba", date2,DURATION));
        Event event3 = createEvent(new Event("event title 3","Este evento es otra prueba", date3,DURATION));

        events.add(event1);
        events.add(event2);
        events.add(event3);

        Response response1 = eventService.respondToEvent(employeeEmail,event1.getEventId(),true);
        Response response2 = eventService.respondToEvent(employeeEmail,event2.getEventId(),true);
        Response response3 = eventService.respondToEvent(employeeEmail,event3.getEventId(),false);

        allResponses.add(response1);
        allResponses.add(response2);
        allResponses.add(response3);
        afirmativeResponses.add(response1);
        afirmativeResponses.add(response2);

        try {

            List<Response> foundResponses = eventService.getEmployeeResponses(employeeEmail,true);
            assertEquals(allResponses, foundResponses);

            foundResponses = eventService.getEmployeeResponses(employeeEmail, false);
            assertEquals(afirmativeResponses,foundResponses);

        } finally {
            for (Response response : allResponses){
                removeResponse(response.getResponseId());
            }

            for (Event event : events){
                removeEvent(event.getEventId());
            }
        }
    }

    @Test
    public void testInvalidEmail(){

        assertThrows(InputValidationException.class, () -> {
            List<Response> foundResponses = eventService.getEmployeeResponses(null, false);
        });

    }
}