package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientEventService;
import es.udc.ws.app.client.service.ClientEventServiceFactory;
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

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientEventService clientEventService = ClientEventServiceFactory.getService();

        if("-addEvent".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {});

            // [addEvent] EventServiceClient -addEvent <ename> <description> <celebrationDate> <endDate>
            try {
                Long eventId = clientEventService.addEvent(new ClientEventDto(null, args[1], args[2],
                        LocalDateTime.parse(args[3]), LocalDateTime.parse(args[4])));
                System.out.println("Event " + eventId + " created successfully");
            }catch(NumberFormatException | InputValidationException ex) {
                ex.printStackTrace();
            }

        } else if("-findEvents".equalsIgnoreCase(args[0])) {

            String keyword;
            boolean noKeyword=false;
            if (args.length == 2) {
                validateArgs(args, 2, new int[] {});
                keyword=null;
                noKeyword=true;
            } else {
                validateArgs(args, 3, new int[] {});
                keyword=args[2];
            }

            // [findEvents] EventServiceClient -findEvents <endDate> [keyword]

            try {
                List<ClientEventDto> event = clientEventService.findEvents(LocalDate.parse(args[1]), keyword);
                if (noKeyword){
                    System.out.println("Found " + event.size() +
                            " event(s) with endDate before '" + args[1] + "'");
                } else {
                    System.out.println("Found " + event.size() +
                            " event(s) with endDate before '" + args[1] + "' and with keyword = " + keyword);
                }

                for (int i = 0; i < event.size(); i++) {
                    ClientEventDto eventDto = event.get(i);
                    System.out.println("Id: " + eventDto.getEventId() +
                            ", Ename: " + eventDto.getEname() +
                            ", Description: " + eventDto.getDescription() +
                            ", CelebrationDate: " + eventDto.getCelebrationDate() +
                            ", EndDate: " + eventDto.getEndDate() +
                            ", ActiveEvent: " + eventDto.isActiveEvent() +
                            ", AffirmativeResponses: " + eventDto.getAfirmativeResponses() +
                            ", TotalResponses: " + eventDto.getTotalResponses());
                }
            } catch (InputValidationException ex){
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findEvent".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [findEvent] EventServiceClient -findEvent <eventId>

            try {
                ClientEventDto event = clientEventService.findEvent(Long.valueOf(args[1]));
                System.out.println("Found event with Id = " + event.getEventId());
                System.out.println("Id: " + event.getEventId() +
                        ", Ename: " + event.getEname() +
                        ", Description: " + event.getDescription() +
                        ", CelebrationDate: " + event.getCelebrationDate() +
                        ", EndDate: " + event.getEndDate() +
                        ", ActiveEvent: " + event.isActiveEvent() +
                        ", AffirmativeResponses: " + event.getAfirmativeResponses() +
                        ", TotalResponses: " + event.getTotalResponses());
            } catch (InstanceNotFoundException ex){
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-respond".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2});

            // [respond] EventServiceClient -respond <emailEmployee> <eventId> <confirmation>

            try {
                Long responseId = clientEventService.respondToEvent(args[1], Long.valueOf(args[2]), Boolean.parseBoolean(args[3]));

                System.out.println(args[1] + " successfully responded (with id = " + responseId +
                        ") to event with id = " + args[2]);

            } catch (NumberFormatException | InstanceNotFoundException | InputValidationException |
                     ClientEventCanceledException | ClientLimitDateException | ClientAlreadyResponsedException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [cancel] EventServiceClient -cancel <eventId>

            try {
                clientEventService.cancelEvent(Long.valueOf(args[1]));

                System.out.println("Event " + args[1] + " cancelled successfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException | ClientEventCanceledException | ClientLimitDateException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-findResponses".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            // [findResponses] EventServiceClient -findResponses <emailEmployee> <allResponses>

            try {
                List<ClientResponseDto> response = clientEventService.getEmployeeResponses(args[1], Boolean.parseBoolean(args[2]));
                System.out.println("Found " + response.size() +
                        " response(s) for email = " + args[1]);
                for (int i = 0; i < response.size(); i++) {
                    ClientResponseDto responseDto = response.get(i);

                    System.out.println("Id: " + responseDto.getResponseId()+
                            ", employeeEmail: " + responseDto.getEmployeeEmail() +
                            ", eventId: " + responseDto.getEventId() +
                            ", confirmation: " + ((responseDto.isConfirmation()) ? "Si" : "No"));
                }
            } catch (InputValidationException ex){
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }

    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addEvent]          EventServiceClient -addEvent <ename> <description> <celebrationDate> <duration>\n" +
                "    [findEvents]        EventServiceClient -findEvents <endDate> [keyword]\n" +
                "    [findEvent]         EventServiceClient -findEvent <eventId>\n" +
                "    [respond]           EventServiceClient -respond <emailEmployee> <eventId> <confirmation>\n" +
                "    [cancel]            EventServiceClient -cancel <eventId>\n" +
                "    [findResponses]     EventServiceClient -findResponses <emailEmployee> <allResponses>\n");
    }



}