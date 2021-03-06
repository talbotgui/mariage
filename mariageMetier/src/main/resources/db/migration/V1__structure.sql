-- Script initialement execute par Hibernate avec le HBM2DDL mais stocke ici pour sauvegarde
SET SCHEMA PUBLIC;
CREATE MEMORY TABLE PUBLIC.AUTORISATION(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,ID_MARIAGE BIGINT,ID_UTILISATEUR VARCHAR(255));
ALTER TABLE PUBLIC.AUTORISATION ALTER COLUMN ID RESTART WITH 1;
CREATE MEMORY TABLE PUBLIC.COURRIER(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,DATE_ENVOI_REALISE TIMESTAMP,DATE_PREVISION_ENVOI TIMESTAMP,NOM VARCHAR(255),MARIAGE_ID BIGINT);
ALTER TABLE PUBLIC.COURRIER ALTER COLUMN ID RESTART WITH 22;
CREATE MEMORY TABLE PUBLIC.COURRIER_ETAPES_INVITATION(COURRIER BIGINT NOT NULL,ETAPES_INVITATION BIGINT NOT NULL);
CREATE MEMORY TABLE PUBLIC.ETAPE(DTYPE VARCHAR(31) NOT NULL,ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,DATE_HEURE TIMESTAMP,LIEU VARCHAR(255),NOM VARCHAR(255),NUM_ORDRE INTEGER,CELEBRANT VARCHAR(255),MARIAGE_ID BIGINT);
ALTER TABLE PUBLIC.ETAPE ALTER COLUMN ID RESTART WITH 8;
CREATE MEMORY TABLE PUBLIC.INVITE(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,ADRESSE VARCHAR(255),AGE INTEGER,FOYER VARCHAR(255),GROUPE VARCHAR(255),NOM VARCHAR(255),PRENOM VARCHAR(255),TELEPHONE VARCHAR(255),MARIAGE_ID BIGINT,EMAIL VARCHAR(255));
ALTER TABLE PUBLIC.INVITE ALTER COLUMN ID RESTART WITH 257;
CREATE MEMORY TABLE PUBLIC.MARIAGE(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,DATE_CELEBRATION TIMESTAMP,MARIE1 VARCHAR(255),MARIE2 VARCHAR(255));
ALTER TABLE PUBLIC.MARIAGE ALTER COLUMN ID RESTART WITH 3;
CREATE MEMORY TABLE PUBLIC.PRESENCE_ETAPE(ID BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,PRESENT BOOLEAN,ETAPE_ID BIGINT,INVITE_ID BIGINT);
ALTER TABLE PUBLIC.PRESENCE_ETAPE ALTER COLUMN ID RESTART WITH 1785;
CREATE MEMORY TABLE PUBLIC.UTILISATEUR(LOGIN VARCHAR(255) NOT NULL PRIMARY KEY,MDP VARCHAR(255));
