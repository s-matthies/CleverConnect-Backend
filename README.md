# Projekt CleverConnect

## Voraussetzungen

## Projekt starten

### Bines Überschrift (Instalation)

### Datenbank

## Details zum Projekt

### UseCase Diagramm

### Klassendiagramm
![](images/Klassendiagramm.jpg)
### Datenbankentwurf

### PostgresSQL-Datenbank anlegen

Erstellung der Tabellen

>>> Muss geprüft werden!
>>> Create User table fehlt
>>> liber Pdf/Bild einfügen, für bessere Übersicht

CREATE TABLE Externals
    (id                 SERIAL PRIMARY KEY,
     first_name         VARCHAR(30),
     last_name          VARCHAR(30),
     email              VARCHAR(100) UNIQUE NOT NULL,
     password           VARCHAR(255) NOT NULL,
     role               VARCHAR(10) CHECK (role IN ('ADMIN', 'STUDENT', 'EXTERN')),
     locked             BOOLEAN,
     enabled            BOOLEAN,
     registration_date  DATE,
     company            VARCHAR(50),
     availability_start DATE,
     availability_end   DATE,
     description        VARCHAR(255));

CREATE TABLE Bachelor_Subject
    (id             SERIAL PRIMARY KEY,
     title          VARCHAR(255),
     b_description  VARCHAR(255),
     date           DATE,
     external_id    SERIAL,
     FOREIGN KEY (external_id) REFERENCES Externals(id) ON DELETE CASCADE);


CREATE TABLE Special_Field
	(id				SERIAL PRIMARY KEY,
	name			VARCHAR(255) NOT NULL);

CREATE TABLE choosen_fields
	(external_id SERIAL,
	special_field_id SERIAL,
	PRIMARY KEY (external_id, special_field_id),
	FOREIGN KEY (external_id) REFERENCES Externals(id) ON DELETE CASCADE,
	FOREIGN KEY (special_field_id) REFERENCES Special_Field(id) ON DELETE CASCADE);

### Endpunkte


