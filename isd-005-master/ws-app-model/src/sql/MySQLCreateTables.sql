-- ----------------------------------------------------------------------------
-- Event Model
-------------------------------------------------------------------------------

DROP TABLE Response;
DROP TABLE Event;

-- --------------------------------- Event ------------------------------------
CREATE TABLE Event(
    eventId BIGINT NOT NULL AUTO_INCREMENT,
    ename VARCHAR(255) COLLATE latin1_bin NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    celebrationDate DATETIME NOT NULL,
    duration FLOAT NOT NULL,
    registerDate DATETIME NOT NULL,
    activeEvent BIT NOT NULL,
    afirmativeResponses INT NOT NULL,
    negativeResponses INT NOT NULL,
    CONSTRAINT EventPK PRIMARY KEY(eventId),
    CONSTRAINT validDuration CHECK ( duration >= 0 AND duration <= 1000),
    CONSTRAINT validAfirmativeResponse CHECK ( afirmativeResponses >= 0 ),
    CONSTRAINT validNegativeResponses CHECK ( negativeResponses >= 0 )
) ENGINE = InnoDB;

-- ------------------------------- Responses ----------------------------------
CREATE TABLE Response(
    responseId BIGINT NOT NULL AUTO_INCREMENT,
    employeeEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    eventId BIGINT NOT NULL,
    confirmation BIT NOT NULL,
    responseDate DATETIME NOT NULL,
    CONSTRAINT ResponsePK PRIMARY KEY(responseId),
    CONSTRAINT ResponseEventIdFK FOREIGN KEY(eventId)
        REFERENCES Event(eventId) ON DELETE CASCADE
) ENGINE = InnoDB;


