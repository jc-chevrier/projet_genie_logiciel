package fr.ul.miage.m1.projet_genie_logiciel.orm;

import fr.ul.miage.m1.projet_genie_logiciel.Main;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * ORM, pour la sélection, et la manipualtion
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
            configuration.load(Main.class.getResourceAsStream("./configuration/configuration_bdd.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur ! Problème au cours du chargement de la configuration de la base de données !");
            System.exit(0);
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
            System.exit(0);
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
        Map<String, Class> structure = EntiteMetadonnee.getEntiteStructure(entiteClasse);

        //Construction de la reuqête.
        Statement requete = null;
        String requeteString = "SELECT * FROM " + nomTable + " " + predicat + ";";

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
                    } else if (type.equals(Double.class)) {
                       valeur = resultatLignes.getDouble(attribut);
                    } else if (type.equals(String.class)) {
                       valeur = resultatLignes.getString(attribut);
                    } else if (type.equals(Date.class)) {
                       valeur = resultatLignes.getDate(attribut);
                    }
                    nUpletAttributs.put(attribut, valeur);
                }
                Entite nUplet = EntiteMetadonnee.instancierNUplet(entiteClasse, nUpletAttributs);
                listeNUplets.add(nUplet);
            }

            //Fin de la lecture du résultat de la requête.
            resultatLignes.close();
            requete.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête se sélection a échouée : \"" + requeteString + "\" !");
            System.exit(0);
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
        return chercherNUpletsAvecPredicat(predicat, entiteClasse).get(0);
    }

    /**
     * Chercher tous les n-uplets d'une table.
     */
    public List<Entite> chercherTousLesNUplets(@NotNull Class entiteClasse) {
        return chercherNUpletsAvecPredicat("", entiteClasse);
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

            //Construction de la requête.
            //Cas insertion.
            if(nUplet.getId() == null) {
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
                        requeteString += "'" + nUplet.get(attribut) + "',";
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
                        requeteString += attribut + " = '" + nUplet.get(attribut) + "',";
                    }
                }
                requeteString = requeteString.substring(0, requeteString.length() - 1) +
                                " WHERE ID = " + nUplet.getId() + ";";
            }

            //Execution nde la erquête.
            Statement requete = connexion.createStatement();
            requete.executeUpdate(requeteString);
            connexion.commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête de persistence a échouée : \"" + requeteString + "\" !");
            System.exit(0);
        }
    }

    /**
     * Supprimer un n-uplet.
     *
     * @param nUplet
     */
    public void supprimerNUplet(@NotNull Entite nUplet) {
        String requeteString = null;
        try {
            //Récupération des métadonnées de la table.
            Class entiteClasse = nUplet.getClass();
            String nomTable = EntiteMetadonnee.getEntiteNomTable(entiteClasse);

            //Requête.
            if(nUplet.getId() != null) {
                //Construction de la requête.
                Statement requete = connexion.createStatement();
                requeteString = "DELETE FROM " + nomTable + " WHERE ID = " + nUplet.getId()  + ";";

                //Execution de la requête.
                requete.executeUpdate(requeteString);
                connexion.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur ! Une requête de suppression a échouée : \"" + requeteString + "\" !");
            System.exit(0);
        }
    }
}
