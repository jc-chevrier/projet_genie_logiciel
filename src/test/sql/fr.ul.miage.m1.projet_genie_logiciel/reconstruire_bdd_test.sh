#!/bin/bash

configurationFichier="../../resources/fr/ul/miage/m1/projet_genie_logiciel/configuration/configuration_bdd_test.properties"
while IFS='=' read -r cle valeur
do
  eval ${cle}=\${valeur}
done < "$configurationFichier"
eval ${cle}=\${valeur}

export PGPASSWORD=$motDePasse
scriptsChemin="../../../main/sql/fr.ul.miage.m1.projet_genie_logiciel/"
psql -h "$hote" -p "$port" -U "$utilisateur" -d "postgres" -q -v "baseDeDonnees=$baseDeDonnees" < "${scriptsChemin}initialisation/script_creation_bdd.sql"
psql -h "$hote" -p "$port" -U "$utilisateur" -d "$baseDeDonnees" -q < "${scriptsChemin}initialisation/script_creation_tables.sql"
psql -h "$hote" -p "$port" -U "$utilisateur" -d "$baseDeDonnees" -q < "${scriptsChemin}initialisation/script_insertion_roles.sql"
psql -h "$hote" -p "$port" -U "$utilisateur" -d "$baseDeDonnees" -q < "${scriptsChemin}jeu_de_donnees/script_insertion_comptes.sql"