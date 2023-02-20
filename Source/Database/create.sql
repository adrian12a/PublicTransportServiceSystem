--DROP TABLE połączenia;
--DROP TABLE przystanki;
--DROP TABLE accounts;

CREATE TABLE połączenia (
    id_trasy int(11),
    id_przystanku int(11) NOT NULL,
    numer_przystanku_na_trasie int(11) NOT NULL,
    godzina_odjazdu time NOT NULL
);

CREATE TABLE przystanki (
    id_przystanku int(11),
    ulica varchar(100) NOT NULL
);

CREATE TABLE accounts (
    login varchar(50),
    password varchar(50) NOT NULL,
    email varchar(100),
    type varchar(20) NOT NULL
);

ALTER TABLE przystanki ADD CONSTRAINT przystanki_pk PRIMARY KEY(id_przystanku);
ALTER TABLE accounts ADD CONSTRAINT accounts_pk PRIMARY KEY(login);
ALTER TABLE połączenia ADD CONSTRAINT połączenia_fk FOREIGN KEY (id_przystanku) REFERENCES przystanki(id_przystanku);