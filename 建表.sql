Drop TABLE if EXISTS DATA,daily,license,refer,sources,DATAToDaily,DATAToSOURCES,DATAToLICENSE;

CREATE TABLE DATA(
    Times INTEGER not null,
		Name VARCHAR(50) not null,
    code VARCHAR(50),
    updateTime VARCHAR(50),
    pfxLink VARCHAR(50),
    CONSTRAINT DATAID PRIMARY KEY (TIMES)
);

CREATE TABLE DATAToDaily(
    Times INTEGER not null REFERENCES DATA (Times) ON DELETE CASCADE ON UPDATE CASCADE,
    did INTEGER not null REFERENCES Daily (did) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT DATAToDailyID PRIMARY KEY (Times,did)
);

CREATE TABLE Daily (
    did INTEGER not null,
    fxDate VARCHAR(50) not NULL,
    sunrise VARCHAR(50),
    sunset VARCHAR(50),
    moonrise VARCHAR(50),
    moonset VARCHAR(50),
    moonPhase VARCHAR(50),
    moonPhaseIcon VARCHAR(50),
    tempMax VARCHAR(50),
    tempMin VARCHAR(50),
    iconDay VARCHAR(50),
    textDay VARCHAR(50),
    iconNight VARCHAR(50),
    textNight VARCHAR(50),
    wind360Day VARCHAR(50),
    windDirDay VARCHAR(50),
    windScaleDay VARCHAR(50),
    windSpeedDay VARCHAR(50),
    wind360Night VARCHAR(50),
    windDirNight VARCHAR(50),
    windScaleNight VARCHAR(50),
    windSpeedNight VARCHAR(50),
    humidity VARCHAR(50),
    precip VARCHAR(50),
    pressure VARCHAR(50),
    vis VARCHAR(50),
    cloud VARCHAR(50),
    uvIndex VARCHAR(50),
    CONSTRAINT DailyPK PRIMARY KEY (did)
);

CREATE TABLE DATAToSOURCES(
    Times INTEGER not null REFERENCES DATA (Times) ON DELETE CASCADE ON UPDATE CASCADE,
    sid INTEGER REFERENCES SOURCES (sid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT DATAToREFER PRIMARY KEY (Times,sid)
);

CREATE TABLE DATAToLICENSE(
    Times INTEGER not null REFERENCES DATA (Times) ON DELETE CASCADE ON UPDATE CASCADE,
    LID INTEGER REFERENCES LICENSE (lid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT DATAToREFER PRIMARY KEY (Times,lid)
);

CREATE TABLE SOURCES (
    sid INTEGER,
    sources VARCHAR(50),
		CONSTRAINT SourcesPK PRIMARY KEY (sid)
);

CREATE TABLE LICENSE (
    lid INTEGER,
    license VARCHAR(50),
		CONSTRAINT LicensePK PRIMARY KEY (lid)
);