package fr.ul.miage.m1.projet_genie_logiciel.orm;

import fr.ul.miage.m1.projet_genie_logiciel.Main;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * ORM, pour la sélection, et la manipulation
 * de données de base de données PostgreSQL.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class ORM {
    //Configuration de la connexion à la base de données.
    private Properties configuration;
    //Connexion à la base de données.
    private Connection connexion;
    //Singleton.
    private static ORM ORMSingleton;
    //Nom du fichier de configuration.
    public static String CONFIGURATION_FILENAME = "./configuration/configuration_bdd.properties";

    private ORM() {
        chargerConfiguration();
        connecter();
    }

    /**
     * Obtenir le singleton ORM.
     *
     * @return
     */
    public static ORM getInstance() {
        if(ORMSingleton == null) {
            ORMSingleton = new ORM();
        }
        return ORMSingleton;
    }

    /**
     * Charger la configuration de la connexion à la base de données.
     */
    private void chargerConfiguration() {
        configuration = new Properties();
        try {
            configuration.load(Main.class.getResourceAsStream(CONFIGURATION_FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur ! Problème au cours du chargement de la configuration de la base de données !");
            System.exit(1);
        }
    }

    /**
     * Connecter l'application à la base de données.
     */
    private void connecter() {
        try {
            Class.forName("org.postgresql.Driver");
            connexion = DriverManager.getConnection("jdbc:postgresql://" +
                                                    configuration.get("hote") + ":" + configuration.get("port") + "/" +
                                                    configuration.get("baseDeDonnees"),
                                                    (String) configuration.get("utilisateur"), (String) configuration.get("motDePasse"));
            connexion.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Connexion impossible à la base de données !");
            System.exit(1);
        }
    }

    /**
     * Chercher des n-uplets d'une table
     * avec un prédicat.
     *
     * @param predicat
     * @param entiteClasse
     * @return
     */
    public List<Entite> chercherNUpletsAvecPredicat(@NotNull String predicat, @NotNull Class entiteClasse) {
        //Récupération des métadonnées de la table.
        String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);
        Map<String, Class> structure = new TreeMap<>(EntiteMetadonnee.getEntiteStructure(entiteClasse));

        //Construction de la requête.
        Statement requete = null;
        String requeteString = "SELECT " +
                                structure.keySet()
                                        .stream()
                                        .collect(Collectors.joining(", FROM_TABLE.", "FROM_TABLE.", "")) +
                                " FROM " + nomTable + " AS FROM_TABLE " + predicat + "" +
                                " ORDER BY FROM_TABLE.ID;";

        List<Entite> listeNUplets = new ArrayList<Entite>();
        try {
            //Execution de la requête.
            requete = connexion.createStatement();
            ResultSet resultatLignes = requete.executeQuery(requeteString);

            //Lecture du résultat de la requête.
            while(resultatLignes.next()) {
                Map<String, Object> nUpletAttributs = new HashMap<String, Object>();
                for(String attribut : structure.keySet()) {
                    Object valeur = null;
                    Class type = structure.get(attribut);
                    if (type.equals(Integer.class)) {
                        valeur = resultatLignes.getInt(attribut);
                        if(resultatLignes.wasNull()) {
                            valeur = null;
                        }
                    } else if (type.equals(Double.class)) {
                        valeur = resultatLignes.getDouble(attribut);
                        if(resultatLignes.wasNull()) {
                            valeur = null;
                        }
                    } else if (type.equals(String.class)) {
                        valeur = resultatLignes.getString(attribut);
                    } else if (type.equals(Date.class)) {
                        valeur = resultatLignes.getTimestamp(attribut);
                    }
                    nUpletAttributs.put(attribut, valeur);
                }
                Entite nUplet = EntiteMetadonnee.instancierNUplet(entiteClasse, nUpletAttributs);
                listeNUplets.add(nUplet);
            }

            //Fin de la lecture du résultat de la requête.
            resultatLignes.close();
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête se sélection a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }

        return listeNUplets;
    }

    /**
     * Chercher un n-uplet d'une table
     * avec un prédicat.
     *
     * @param predicat
     * @param entiteClasse
     * @return
     */
    public Entite chercherNUpletAvecPredicat(@NotNull String predicat, @NotNull Class entiteClasse) {
        List<Entite> nUplets = chercherNUpletsAvecPredicat(predicat, entiteClasse);
        return nUplets.size() == 0 ? null : nUplets.get(0);
    }

    /**
     * Chercher tous les n-uplets d'une table.
     */
    public List<Entite> chercherTousLesNUplets(@NotNull Class entiteClasse) {
        return chercherNUpletsAvecPredicat("", entiteClasse);
    }

    /**
     * Compter le nombre de n-uplets
     * d'une table avec un prédicat.
     *
     * @param predicat
     * @param entiteClasse
     * @return
     */
    public Integer compterNUpletsAvecPredicat(@NotNull String predicat, @NotNull Class entiteClasse) {
        //Récupération des métadonnées de la table.
        String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);

        //Construction de la requête.
        Statement requete = null;
        String requeteString = "SELECT COUNT(FROM_TABLE.*) AS NOMBRE_NUPLETS FROM " + nomTable + " AS FROM_TABLE " + predicat + ";";

        Integer nombreNUplets = 0;
        try {
            //Execution de la requête.
            requete = connexion.createStatement();
            ResultSet resultatLignes = requete.executeQuery(requeteString);

            //Lecture du résultat de la requête.
            while(resultatLignes.next()) {
                nombreNUplets = resultatLignes.getInt("NOMBRE_NUPLETS");
            }

            //Fin de la lecture du résultat de la requête.
            resultatLignes.close();
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête se sélection a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }

        return nombreNUplets;
    }

    /**
     * Compter le nombre total de
     * n-uplets d'une table.
     *
     * @param entiteClasse
     * @return
     */
    public Integer compterTousLesNUplets(@NotNull Class entiteClasse) {
        return compterNUpletsAvecPredicat("", entiteClasse);
    }

    /**
     * Faire persister une modification.
     */
    public void persisterNUplet(@NotNull Entite nUplet) {
        String requeteString = null;
        try {
            //Récupération des métadonnées de la table.
            Class entiteClasse = nUplet.getClass();
            String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);
            Map<String, Class> structure = EntiteMetadonnee.getEntiteStructure(entiteClasse);

            //Mode : insertion ou mise à jour ?
            boolean modeInsertion = nUplet.getId() == null;

            //Construction de la requête.
            //Cas insertion.
            if(modeInsertion) {
                requeteString = "INSERT INTO " + nomTable + "(";
                //Colonnes.
                for(String attribut : structure.keySet()) {
                    if(!attribut.equals("ID")) {
                        requeteString += attribut + ",";
                    }
                }
                requeteString = requeteString.substring(0, requeteString.length() - 1)
                                + ") VALUES (" ;
                //Valeurs.
                for(String attribut : structure.keySet()) {
                    if(!attribut.equals("ID")) {
                        Object valeur = nUplet.get(attribut);
                        if(valeur == null) {
                            requeteString += "NULL,";
                        } else {
                            requeteString += "'" + valeur + "',";
                        }
                    }
                }
                requeteString = requeteString.substring(0, requeteString.length() - 1)
                                + ");";
            //Cas mise à jour.
            } else {
                requeteString = "UPDATE " + nomTable + " SET ";
                //Nouvelles valeurs.
                for(String attribut : structure.keySet()) {
                    if(!attribut.equals("ID")) {
                        Object valeur = nUplet.get(attribut);
                        if(valeur == null) {
                            requeteString += attribut + " = NULL,";
                        } else {
                            requeteString += attribut + " = '" + valeur + "',";
                        }
                    }
                }
                requeteString = requeteString.substring(0, requeteString.length() - 1) +
                                " WHERE ID = " + nUplet.getId() + ";";
            }

            //Execution de la requête.
            PreparedStatement requete = connexion.prepareStatement(requeteString, Statement.RETURN_GENERATED_KEYS);
            requete.executeUpdate();
            //Validation de la transaction.
            connexion.commit();
            //Si insertion, on fournit l'id généré à l'objet.
            if(modeInsertion) {
                ResultSet clesGenerees = requete.getGeneratedKeys();
                clesGenerees.next();
                nUplet.set("ID", clesGenerees.getInt("ID"));
                //Fin de la lecture des clés générées.
                clesGenerees.close();
            }
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête de persistence a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }
    }

    /**
     * Supprimer des n-uplets d'une table
     * avec un pédicat.
     *
     * @param predicat
     * @param entiteClasse
     */
    public void supprimerNUpletsAvecPredicat(@NotNull String predicat, @NotNull Class entiteClasse) {
        String requeteString = null;
        try {
            //Récupération des métadonnées de la table.
            String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);

            //Requête.
            //Construction de la requête.
            requeteString = "DELETE FROM " + nomTable + " " + predicat + " ;";
            Statement requete = connexion.createStatement();
            //Execution de la requête.
            requete.executeUpdate(requeteString);
            //Validation de la transaction.
            connexion.commit();
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête de suppression a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }
    }

    /**
     * Supprimer un n-uplet.
     *
     * @param nUplet
     */
    public void supprimerNUplet(@NotNull Entite nUplet) {
        supprimerNUpletsAvecPredicat("WHERE ID = " + nUplet.getId(), nUplet.getClass());
    }

    /**
     * Supprimer tous les n-uplets d'une
     * table.
     *
     * @param entiteClasse
     */
    public void supprimerTousLesNUplets(@NotNull Class entiteClasse) {
        supprimerNUpletsAvecPredicat("", entiteClasse);
    }

    /**
     * Réinitialiser une séuence d'id
     * à une valur de départ.
     *
     * @param entiteClasse
     */
    public void reinitialiserSequenceId(int idDebut, @NotNull Class entiteClasse) {
        String requeteString = null;
        try {
            //Récupération des métadonnées de la table.
            String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);

            requeteString = "ALTER SEQUENCE " + nomTable.toLowerCase() + "_id_seq RESTART WITH " + idDebut + ";";
            Statement requete = connexion.createStatement();
            //Execution de la requête.
            requete.executeUpdate(requeteString);
            //Validation de la transaction.
            connexion.commit();
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête de réinitialisation de séquence d'id a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }
    }

    /**
     * Réinitialiser une séuence d'id
     * à une valur de départ.
     *
     * @param entiteClasse
     */
    public void reinitialiserSequenceIdA1(@NotNull Class entiteClasse) {
        reinitialiserSequenceId(1, entiteClasse);
    }

    /**
     * Supprimer tous les n-uplets d'une table,
     * et réinitialiser la séquence de ses ids à 1.
     *
     * @param entiteClasse
     */
    public void reinitialiserTable(@NotNull Class entiteClasse) {
        supprimerTousLesNUplets(entiteClasse);
        reinitialiserSequenceIdA1(entiteClasse);
    }
}