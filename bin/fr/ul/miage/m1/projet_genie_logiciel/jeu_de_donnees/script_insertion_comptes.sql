INSERT INTO COMPTE(ID, NOM, PRENOM, ACTIF, ID_ROLE) VALUES (1, 'LAURENT', 'Victoria', 1, 1);
INSERT INTO COMPTE(ID, NOM, PRENOM, ACTIF, ID_ROLE) VALUES (2, 'DURAND', 'Olivier', 1, 2);
INSERT INTO COMPTE(ID, NOM, PRENOM, ACTIF, ID_ROLE) VALUES (3, 'CARON', 'Jules', 1, 3);
INSERT INTO COMPTE(ID, NOM, PRENOM, ACTIF, ID_ROLE) VALUES (4, 'DUPONT', 'Pierre', 1, 4);
INSERT INTO COMPTE(ID, NOM, PRENOM, ACTIF, ID_ROLE) VALUES (5, 'ANDERSON', 'Christa', 1, 5);

ALTER SEQUENCE compte_id_seq RESTART WITH 6;