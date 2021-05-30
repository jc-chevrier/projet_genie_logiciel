@ECHO OFF

SET configurationFichier=..\..\resources\fr\ul\miage\m1\projet_genie_logiciel\configuration\configuration_bdd_test.properties
FOR /F "tokens=1,2 delims==" %%G IN (%configurationFichier%) DO (set %%G=%%H)

SET PGPASSWORD=%motDePasse%
SET scriptsChemin=..\..\..\main\sql\fr.ul.miage.m1.projet_genie_logiciel\
psql -h %hote% -p %port% -U %utilisateur% -d postgres -q -v baseDeDonnees=%baseDeDonnees% < %scriptsChemin%initialisation\script_creation_bdd.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < %scriptsChemin%initialisation\script_creation_tables.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < %scriptsChemin%initialisation\script_insertion_roles.sql
psql -h %hote% -p %port% -U %utilisateur% -d %baseDeDonnees% -q < %scriptsChemin%jeu_de_donnees\script_insertion_comptes.sql