#!/bin/bash

configurationFichier="./configuration/configuration_bdd.properties"
while IFS='=' read -r cle valeur
do
  eval ${cle}=\${valeur}
done < "$configurationFichier"

export PGPASSWORD=$motDePasse
psql -h $hote -p $port -U $utilisateur -d postgres -q -v baseDeDonnees=$baseDeDonnees < ./initialisation/script_creation_bdd.sql
psql -h $hote -p $port -U $utilisateur -d $baseDeDonnees -q < ./initialisation/script_creation_tables.sql
psql -h $hote -p $port -U $utilisateur -d $baseDeDonnees -q < ./initialisation/script_insertion_roles.sql
psql -h $hote -p $port -U $utilisateur -d $baseDeDonnees -q < ./jeu_de_donnees/script_insertion_comptes.sql