CREATE TABLE teams
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50)  NOT NULL UNIQUE,
    description   VARCHAR(100) NOT NULL,
    creation_date DATE         NOT NULL
);

CREATE TABLE users
(
    id                SERIAL PRIMARY KEY,
    email             VARCHAR(50)  NOT NULL UNIQUE,
    password          VARCHAR(100) NOT NULL,
    first_name        VARCHAR(25)  NOT NULL,
    last_name         VARCHAR(25)  NOT NULL,
    registration_date DATE         NOT NULL
);

CREATE TYPE user_role AS ENUM ('OWNER', 'MANAGER', 'MEMBER');

CREATE TABLE memberships
(
    team_id              INT       NOT NULL,
    user_id              INT       NOT NULL,
    role                 user_role NOT NULL,
    team_joining_date    DATE      NOT NULL,
    role_assignment_date DATE      NOT NULL,
    PRIMARY KEY (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES teams (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TYPE meeting_status AS ENUM ('SCHEDULED', 'POSTPONED', 'CANCELED');

CREATE TABLE meetings
(
    id            SERIAL PRIMARY KEY,
    organizer_id  INT            NOT NULL,
    team_id       INT            NOT NULL,
    subject       VARCHAR(100)   NOT NULL,
    start_time    TIMESTAMP      NOT NULL,
    end_time      TIMESTAMP      NOT NULL,
    link          VARCHAR(100)   NOT NULL,
    status        meeting_status NOT NULL,
    creation_time TIMESTAMP      NOT NULL,
    update_time   TIMESTAMP,
    FOREIGN KEY (organizer_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TYPE invitation_status AS ENUM ('ACCEPTED', 'REJECTED', 'ACTIVE');

CREATE TABLE invitations
(
    meeting_id     INT               NOT NULL,
    user_id        INT               NOT NULL,
    status         invitation_status NOT NULL,
    suggested_time TIMESTAMP,
    update_time    TIMESTAMP,
    PRIMARY KEY (meeting_id, user_id),
    FOREIGN KEY (meeting_id) REFERENCES meetings (id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
