@ECHO OFF

SET configurationFichier=.\configuration\configuration_bdd.properties
FOR /F "tokens=1,2 delims==" %%G IN (%configurationFichier%) DO (set %%G=%%H)

SET PGPASSWORD=%motDePasse%
psql -h %hote% -p %port% -U %utilisateur% -d postgres -q -v baseDeDonnees=%baseDeDonnees% < .\initialisation\script_creation_bdd.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < .\initialisation\script_creation_tables.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < .\initialisation\script_insertion_roles.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < .\jeu_de_donnees\script_insertion_comptes.sql