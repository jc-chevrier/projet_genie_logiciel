--script de creation des tables

--création de la table role
CREATE TABLE ROLE (
	ID SERIAL8 NOT NULL,
	LIBELLE VARCHAR(50) NOT NULL,
	CONSTRAINT CST_PK_ROLE PRIMARY KEY (ID)
);

--création de la table compte
CREATE TABLE COMPTE (
	ID SERIAL8 NOT NULL,
	NOM VARCHAR(50) NOT NULL,
	PRENOM VARCHAR(50) NOT NULL,
	ACTIF NUMERIC(1,0) NOT NULL,
	ID_ROLE INT8 NOT NULL,
	CONSTRAINT CST_PK_COMPTE PRIMARY KEY (ID),
	CONSTRAINT CST_FK_COMPTE_ID_ROLE FOREIGN KEY (ID_ROLE) REFERENCES ROLE(ID)
);

--création de la table place
CREATE TABLE PLACE (
	ID SERIAL8 NOT NULL,
	ETAT VARCHAR(50) NOT NULL,
	DATETIME_RESERVATION TIMESTAMP,
	NOM_RESERVATION VARCHAR(50),
	PRENOM_RESERVATION VARCHAR(50),
	ID_COMPTE_SERVEUR INT8 NOT NULL,
	CONSTRAINT CST_PK_PLACE PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_COMMANDE_DATETIME_RESERVATION_SUP_ZERO CHECK (DATETIME_RESERVATION >= NOW()),
	CONSTRAINT CST_FK_PLACE_ID_COMPTE_SERVEUR FOREIGN KEY (ID_COMPTE_SERVEUR) REFERENCES COMPTE(ID)
);

--création de la table commande
CREATE TABLE COMMANDE (
	ID SERIAL8 NOT NULL,
	DATETIME_CREATION TIMESTAMP  NOT NULL,
	COUT_TOTAL DECIMAL(8,3),
	ETAT VARCHAR(50) NOT NULL,
	ID_PLACE INT8 NOT NULL,
	CONSTRAINT CST_PK_COMMANDE PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_COMMANDE_COUT_TOTAL_SUP_ZERO CHECK (COUT_TOTAL > 0),
	CONSTRAINT CST_DOM_COMMANDE_DATETIME_CREATION_SUP_ZERO CHECK (DATETIME_CREATION >= NOW()),
	CONSTRAINT CST_FK_COMMANDE_ID_PLACE FOREIGN KEY (ID_PLACE) REFERENCES PLACE(ID)
);

--création de la table categorie
CREATE TABLE CATEGORIE (
	ID SERIAL8 NOT NULL,
	LIBELLE VARCHAR(50) NOT NULL,
	CONSTRAINT CST_PK_CATEGORIE PRIMARY KEY (ID)
);

--création de la table unité
CREATE TABLE UNITE (
	ID SERIAL8 NOT NULL,
	LIBELLE VARCHAR(50) NOT NULL,
	CONSTRAINT CST_PK_UNITE PRIMARY KEY (ID)
);

--création de la table ingredient
CREATE TABLE INGREDIENT (
	ID SERIAL8 NOT NULL,
	LIBELLE VARCHAR(50) NOT NULL,
	STOCK DECIMAL (16,3) NOT NULL,
	ID_UNITE INT8 NOT NULL,
	CONSTRAINT CST_PK_INGREDIENT  PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_INGREDIENT_STOCK_SUP_ZERO CHECK (STOCK >= 0),
	CONSTRAINT CST_FK_INGREDIENT_ID_UNITE FOREIGN KEY (ID_UNITE) REFERENCES UNITE(ID)
);

--création de la table plat
CREATE TABLE PLAT (
	ID SERIAL8 NOT NULL,
	LIBELLE VARCHAR(50) NOT NULL,
	PRIX DECIMAL(8,3) NOT NULL,
	CARTE NUMERIC(1,0) NOT NULL,
	ID_CATEGORIE INT8 NOT NULL,
	CONSTRAINT CST_PK_PLAT PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_PLAT_PRIX_SUP_ZERO CHECK (PRIX > 0),
	CONSTRAINT CST_FK_PLAT_ID_CATEGORIE FOREIGN KEY (ID_CATEGORIE) REFERENCES CATEGORIE(ID)
);

<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> 2496e6ed7b551e1964eeed3d8e697b1c40905eb5
=======

>>>>>>> 3b29530ceedfde8db6fcf2c07969249d093c4cb0
--création de la table plat_ingredients
CREATE TABLE PLAT_INGREDIENTS (
	ID SERIAL8 NOT NULL,
	QUANTITE DECIMAL (16,3) NOT NULL,
	ID_PLAT INT8 NOT NULL,
	ID_INGREDIENT INT8 NOT NULL,
	CONSTRAINT CST_PK_PLAT_INGREDIENTS PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_PLAT_INGREDIENTS_QUANTITE_SUP_ZERO CHECK (QUANTITE > 0),
	CONSTRAINT CST_FK_PLAT_INGREDIENTS_ID_PLAT FOREIGN KEY (ID_PLAT) REFERENCES PLAT(ID),
	CONSTRAINT CST_FK_PLAT_INGREDIENTS_ID_INGREDIENT FOREIGN KEY (ID_INGREDIENT) REFERENCES INGREDIENT(ID)
);

--création de la table ligne_commande
CREATE TABLE LIGNE_COMMANDE (
	ID SERIAL8 NOT NULL,
	NB_OCCURENCES INT8 NOT NULL,
	ETAT NUMERIC(1,0) NOT NULL,
	ID_PLAT INT8 NOT NULL,
	ID_COMMANDE INT8 NOT NULL,
	CONSTRAINT CST_PK_LIGNE_COMMANDE PRIMARY KEY (ID),
	CONSTRAINT CST_DOM_LIGNE_COMMANDE_NB_OCCURENCES_SUP_ZERO CHECK (NB_OCCURENCES > 0),
	CONSTRAINT CST_FK_LIGNE_COMMANDE_ID_PLAT FOREIGN KEY (ID_PLAT) REFERENCES PLAT(ID),
	CONSTRAINT CST_FK_LIGNE_COMMANDE_ID_COMMANDE FOREIGN KEY (ID_COMMANDE) REFERENCES COMMANDE(ID)
);