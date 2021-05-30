INSERT INTO ROLE (ID, LIBELLE) values (1, 'Directeur');
INSERT INTO ROLE (ID, LIBELLE) values (2, 'Maitre d''hôtel');
INSERT INTO ROLE (ID, LIBELLE) values (3, 'Cuisinier');
INSERT INTO ROLE (ID, LIBELLE) values (4, 'Serveur');
INSERT INTO ROLE (ID, LIBELLE) values (5, 'Assistant de service');

ALTER SEQUENCE role_id_seq RESTART WITH 6;