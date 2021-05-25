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
     * Chercher des n-uplets de manière "sauvage",
     * sans préciser de structure / d'entité correspondante.
     *
     * Cette méthode permet de faire une recherche libre
     * entièrement écrite puis passée en paramètre.
     *
     * @param requeteString
     * @return le résultat de la fonction de la fonction d'agrégation
     */
    public List<Map<String, Object>> chercherNUplets(@NotNull String requeteString) {
        //Construction de la requête.
        Statement requete = null;
        List<Map<String, Object>> resultatLignes = new ArrayList<Map<String, Object>>();

        try {
            //Exécution de la requête.
            requete = connexion.createStatement();
            ResultSet resultatLignes_ = requete.executeQuery(requeteString);

            //Récupération des libellés des attributs du résultat.
            ResultSetMetaData resultatMetadonnees = resultatLignes_.getMetaData();
            List<String> attributs = new ArrayList<String>();
            int nombreAttributs = resultatMetadonnees.getColumnCount();
            for(int indexAttribut = 1; indexAttribut <= nombreAttributs; indexAttribut++) {
                attributs.add(resultatMetadonnees.getColumnName(indexAttribut).toUpperCase());
            }

            //Lecture du résultat de la requête.
            while(resultatLignes_.next()) {
                Map<String, Object> resultatLigne = new HashMap<String, Object>();
                resultatLignes.add(resultatLigne);
                for(String attribut : attributs) {
                    Object valeur = resultatLignes_.getObject(attribut);
                    resultatLigne.put(attribut, valeur);
                }
            }

            //Fin de la lecture du résultat de la requête.
            resultatLignes_.close();
            //Fin de la requête.
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête se sélection a échouée : \"" + requeteString + "\" !");
            System.exit(1);
        }

        return resultatLignes;
    }

    /**
     * Chercher des n-uplets d'une table
     * avec un prédicat.
     *
     * La requête en interne fait un "SELECT * FROM"
     * sur la table désdignée par la classe précisée
     * ne paramètre.
     *
     * Cette méthode de recherche / de sélection permet
     * de préciser un prédicat / une condition de recherche :
     * condition WHERE, et INNER JOIN avant, si besoin
     * de faire des jointures pour le prédicat.
     *
     * Les n-uplets sont ordonnées avec le champ id.
     * On part du postulat que toute tables de nos bases
     * de données ont toujours une clé primaire
     * nommée "id".
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
            //Exécution de la requête.
            requete = connexion.createStatement();
            ResultSet resultatLignes = requete.executeQuery(requeteString);

            //Lecture des lignes du résultat de la requête.
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
     * Cette méthode retourne le premier n-uplet trouvé par cette autre méthode :
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#chercherNUpletsAvecPredicat(String, Class) 
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
     *
     * Cette méthode utilise cette autre méthode, sans prédicat :
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#chercherNUpletAvecPredicat(String, Class)
     */
    public List<Entite> chercherTousLesNUplets(@NotNull Class entiteClasse) {
        return chercherNUpletsAvecPredicat("", entiteClasse);
    }

    /**
     * Compter le nombre de n-uplets
     * d'une table avec un prédicat.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#chercherNUplets(String)
     *
     * @param predicat
     * @param entiteClasse
     * @return
     */
    public Integer compterNUpletsAvecPredicat(@NotNull String predicat, @NotNull Class entiteClasse) {
        //Récupération des métadonnées de la table.
        String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);

        //Construction de la requête.
        String requeteString = "SELECT COUNT(FROM_TABLE.*) AS NOMBRE_NUPLETS FROM " + nomTable + " AS FROM_TABLE " + predicat + ";";

        //Exécution de la requête.
        Integer nombreNUplets = ((Long) chercherNUplets(requeteString).get(0).get("NOMBRE_NUPLETS")).intValue();

        return nombreNUplets;
    }

    /**
     * Compter le nombre total de
     * n-uplets d'une table.
     *
     * Cette méthode utilise cette
     * autre méthode, sans prédicat :
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#compterNUpletsAvecPredicat(String, Class) 
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
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#supprimerNUpletsAvecPredicat(String, Class) 
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
     * Cette méthode utilise cette autre
     * méthode, sans prédicat :
     *  @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#supprimerNUpletsAvecPredicat(String, Class)
     *
     * @param entiteClasse
     */
    public void supprimerTousLesNUplets(@NotNull Class entiteClasse) {
        supprimerNUpletsAvecPredicat("", entiteClasse);
    }

    /**
     * Réinitialiser une séquence d'id
     * à une valeur de départ.
     *
     * @param entiteClasse
     */
    public void reinitialiserSequenceId(int idDebut, @NotNull Class entiteClasse) {
        String requeteString = null;
        try {
            //Récupération des métadonnées de la table.
            String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);
            //Contruction de la requête.
            requeteString = "ALTER SEQUENCE " + nomTable.toLowerCase() + "_id_seq RESTART WITH " + idDebut + ";";
            Statement requete = connexion.createStatement();
            //Exécution de la requête.
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
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#reinitialiserSequenceId(int, Class) 
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
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#supprimerTousLesNUplets(Class)
     * @see fr.ul.miage.m1.projet_genie_logiciel.orm.ORM#reinitialiserSequenceIdA1(Class) 
     * 
     * @param entiteClasse
     */
    public void reinitialiserTable(@NotNull Class entiteClasse) {
        supprimerTousLesNUplets(entiteClasse);
        reinitialiserSequenceIdA1(entiteClasse);
    }
}