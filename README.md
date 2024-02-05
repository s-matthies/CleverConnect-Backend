# Projekt CleverConnect

Die Anwendung ermöglicht Studierenden eine Zweitbetreuung für ihre Bachelorarbeit zu finden.
Es gibt drei Rollen von Nutzer*innen, die die Anwendung jeweils verschieden nutzen können und unterschiedliche Bereiche einsehen können.

Eine umfassende Erklärung zu der Anwendung und ihrer Funktionialitäten ist hier zu finden:  
https://gitlab.rz.htw-berlin.de/Fuerueze.Saritoprak/team-c-frontend-neu/-/tree/main2?ref_type=heads


## Technologien
Im Rahmen des Projekts verwendete Technologien

* **Java**: Version 21
* **Spring Boot**: Ein Framework zur Vereinfachung der Erstellung von Spring-Anwendungen. Version 3.1.5 
* **Maven**: Ein Werkzeug zur Verwaltung und zum Bau von Java-Projekten. Die Version wird durch Version von Spring Boot bestimmt.
* **PostgresSQL**: Die Datenbank, die für die Speicherung der Daten verwendet wird.
* **JPA/Hibernate**: Ein Framework zur Abbildung von Java-Objekten auf relationale Datenbanken. Die Version wird durch Version von Spring Boot bestimmt.
* **OpenAPI/Swagger**: Ein Werkzeug zur Erstellung von API-Dokumentationen. Version 2.3.0




## Projekt lokal ausführen

Um dieses Projekt lokal auszuführen, führen Sie die folgenden Schritte aus:

1. Projekt klonen: 
```
$ git clone https://gitlab.htw-berlin.de/Sabine.Matthies/team-c-backend
```
2. In das Projektverzeichnis wechseln: 
```
$ cd ../path/to/the/file
```
3. Abhängigkeiten installieren: 
```
$ mvn install
```
4. Anwendung starten:
```
$ mvn spring-boot:run
```
Das Projekt wird nun auf http://localhost:3000 gestartet.



### Datenbank

Wir nutzen für das Projekt eine PostgresSQL-Datenbank.
Sie ist auf dem Server cleverconnect (IP-Adresse: 141.45.146.134) installiert.

- Name der Datenbank: clever
- UserName: clever_user
- Passwort: cleverconnect

## Details zum Projekt

### UseCase Diagramm
<img src="/images/uc_diagramm.drawio.png" width="60%" height="60%">


### Klassendiagramm
<img src="images/Klassendiagramm.drawio.jpg" width="60%" height="60%">


### Datenbankentwurf
<img src="images/Datenbankentwurf2_Team_C.drawio.png" width="50%" height="50%">

### PostgresSQL-Datenbank anlegen

Erstellen der Tabellen


```
CREATE TABLE Users
    (id	 		        SERIAL PRIMARY KEY,
    first_name		        VARCHAR(30),
    last_name                   VARCHAR(30),
    email                       VARCHAR(100) UNIQUE NOT NULL,
    password		        VARCHAR(255) NOT NULL,
    role 			VARCHAR(10) CHECK (role IN ('ADMIN', 'STUDENT', 'EXTERN')),
    locked 		        BOOLEAN,
    enabled 		        BOOLEAN,
    registration_date	        DATE);

CREATE TABLE Externals  
    (id                         SERIAL PRIMARY KEY,  
     first_name                 VARCHAR(30),
     last_name                  VARCHAR(30),
     email                      VARCHAR(100) UNIQUE NOT NULL,
     password                   VARCHAR(255) NOT NULL,
     role                       VARCHAR(10) CHECK (role IN ('ADMIN', 'STUDENT', 'EXTERN')),
     locked                     BOOLEAN,
     enabled                    BOOLEAN,
     registration_date          DATE,
     company                    VARCHAR(50),
     availability_start         DATE,
     availability_end           DATE,
     description                VARCHAR(255));

CREATE TABLE Bachelor_Subject
    (id		                INTEGER SET DEFAULT NULL,
    title			VARCHAR(100),
    b_description	        VARCHAR(255),
    date			DATE,
    external_id	                SERIAL,
    FOREIGN KEY (external_id)   REFERENCES Externals(id) ON DELETE CASCADE);

CREATE TABLE Special_Field
    (id                         SERIAL PRIMARY KEY,
    name                        VARCHAR(255) NOT NULL);

CREATE TABLE choosen_fields
    (external_id                SERIAL,
    special_field_id            SERIAL,
    PRIMARY KEY (external_id, special_field_id),
    FOREIGN KEY (external_id) REFERENCES Externals(id) ON DELETE CASCADE,
    FOREIGN KEY (special_field_id) REFERENCES Special_Field(id) ON DELETE CASCADE);
```

## Dokumentation

### Dokumentation der Endpunkte

Alle Endpunkte wurden mit **OpenAPI** dokumentiert.  
Wenn die Anwendung ausgeführt wird, kann die Dokumentation hier aufgerufen werde:   

[Swagger UI](http://localhost:3000/swagger-ui/index.html#/)

Im Projekt wurde ein Endpunkt zum Aufrufen der Dokumentation implementiert.   
Folgender Endpunkt leitet zur Dokumentation der Endpunkte weiter:  

**GET `/user/swagger`** (localhost:3000/user/swagger)

### Javadoc-Dokumentation

Die Javadoc-Dokumentation für dieses Projekt wurde generiert und kann im Verzeichnis `target/site/apidocs` gefunden werden. Sie können die Dokumentation anzeigen, indem Sie die Datei `index.html` in diesem Verzeichnis in einem Webbrowser öffnen.



