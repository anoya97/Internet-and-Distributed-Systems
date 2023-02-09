namespace java es.udc.ws.app.thrift

struct ThriftEventDto {
    1: i64 eventId
    2: string ename
    3: string description
    4: double duration
    5: string celebrationDate
    6: bool activeEvent
    7: i32 afirmativeResponses
    8: i32 totalResponses
}

struct ThriftResponseDto {
    1: i64 responseId
    2: string employeeEmail
    3: i64 eventId
    4: bool confirmation
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftAlreadyResponsedException {
    1: i64 eventId
}

exception ThriftEventCanceledException {
    1: i64 eventId
}

exception ThriftLimitDateException {
    1: i64 eventId
}

service ThriftEventService {
    ThriftEventDto addEvent(1: ThriftEventDto eventDto) throws (1: ThriftInputValidationException e)
    list<ThriftEventDto> findEvents(1: string endDate, 2: string keyword) throws (1: ThriftInputValidationException e)

}

