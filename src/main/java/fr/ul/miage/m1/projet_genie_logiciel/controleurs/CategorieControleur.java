package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

/**
 * Controleur pour les catégories des plats.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CategorieControleur extends Controleur {
    /**
     * Lister les catégories.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des catégories :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if(categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée dans le cataloque !");
        } else {
            //Litsing.
            ui.listerNUplets(categories);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Ajouter une catégorie.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une catégorie :");

        //Questions et entrées.
        String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde : insertion d'une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle(libelle);
        orm.persisterNUplet(categorie);

        //Message de résultat.
        ui.afficher("Categorie ajoutée !");
        ui.afficher(categorie.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Modifier une catégorie.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'une catégorie :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if (categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée dans le catalogue !");
        //Sinon.
        } else {
            //Questions et entrées.
            int idCategorie = ui.poserQuestionListeNUplets("Sélectionner une catégorie :", categories);
            String libelle = ui.poserQuestion("Saisir un nouveau libellé :", UI.REGEX_CHAINE_DE_CARACTERES);
            Categorie categorie = (Categorie) filtrerListeNUpletsAvecId(categories, idCategorie);

            //Sauvegarde : modification de la catégorie.
            categorie.setLibelle(libelle);
            orm.persisterNUplet(categorie);

            //Message de résultat.
            ui.afficher("Catégorie modifiée !");
            ui.afficher(categorie.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Supprimer une catégorie.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une catégorie :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if(categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée dans le cataloque !");
        //Sinon.
        } else {
            //Questions et entrées.
            int idCategorie = ui.poserQuestionListeNUplets("Sélectionner une catégorie :", categories);
            Categorie categorie = (Categorie) filtrerListeNUpletsAvecId(categories, idCategorie);

            //Si la catégorie est utilisée par des plats.
            if (categorie.estUtiliseParPlat()) {
                //Message d'erreur.
                ui.afficher("Cette catégorie est utilisée par des plats, elle ne peut pas être supprimée !");
            //Sinon.
            } else {
                //Sauvegarde : suppression de l'unité.
                orm.supprimerNUplet(categorie);

                //Message de résultat.
                ui.afficher("Catégorie supprimée !");
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
    /**
     * Lister les catégories des plats disponibles dans la carte du jour.
     */
    public static void listerPlatsDisponibles() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des catégories des plats disponibles de la carte du jour :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherNUpletsAvecPredicat(" WHERE FROM_TABLE.ID IN ( " +
                                                                            "SELECT C.ID " +
                                                                            "FROM CATEGORIE AS C " +
                                                                            "INNER JOIN PLAT AS P " +
                                                                            "ON P.ID_CATEGORIE = C.ID " +
                                                                            "INNER JOIN PLAT_INGREDIENTS AS PI " +
                                                                            "ON PI.ID_PLAT = P.ID " +
                                                                            "INNER JOIN INGREDIENT AS I " +
                                                                            "ON I.ID = PI.ID_INGREDIENT " +
                                                                            "WHERE PI.QUANTITE <= I.STOCK AND P.CARTE = 1)", Categorie.class);


        //Si pas de catégories trouvées.
        if(categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée dans le cataloque !");
        } else {
            //Litsing.
            ui.listerNUplets(categories);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }


}