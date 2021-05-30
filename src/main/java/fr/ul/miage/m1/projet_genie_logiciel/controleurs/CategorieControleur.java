package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Contrôleur pour la gestion des catégories des plats.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CategorieControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUNE_TROUVEE = "Aucune catégorie trouvée dans le cataloque !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner une catégorie :";

    /**
     * Lister les catégories.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des catégories :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si des catégories ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(categories, MESSAGE_AUCUNE_TROUVEE)) {
            //Listing.
            ui.listerNUplets(categories);
        }
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder une catégorie,
     * qu'elle soit déjà existante en base de données ou non.
     *
     * @param categorie
     */
    private static void editerEtPersister(@NotNull Categorie categorie) {
        //Questions et entrées.
        //Saisie du libellé de la catégorie.
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde.
        //Insertion / mise à jour de la catégorie.
        categorie.setLibelle(libelle);
        orm.persisterNUplet(categorie);
    }

    /**
     * Ajouter une catégorie.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une catégorie :");

        //Saisie et sauvegarde.
        Categorie categorie = new Categorie();
        editerEtPersister(categorie);

        //Message de résultat.
        ui.afficher("Categorie ajoutée !\n" + categorie);
    }

    /**
     * Modifier une catégorie.
     */
    public static void modifier() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'une catégorie :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si des catégories ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(categories, MESSAGE_AUCUNE_TROUVEE)) {
            //Questions et entrées.
            //Choix de la catégorie.
            Categorie categorie = (Categorie) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, categories);

            //Saisie et sauvegarde.
            editerEtPersister(categorie);

            //Message de résultat.
            ui.afficher("Categorie modifiée !\n" + categorie);
        }
    }

    /**
     * Supprimer une catégorie.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une catégorie :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si des catégories ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(categories, MESSAGE_AUCUNE_TROUVEE)) {
            //Questions et entrées.
            //Choix de la catégorie.
            Categorie categorie = (Categorie) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, categories);

            //Si la catégorie sélectionnée n'est pas utilisée par des plats.
            String messageErreur = "Cette catégorie est utilisée par des plats, elle ne peut pas être supprimée !";
            if (!ui.afficherSiPredicatVrai(categorie.estUtiliseeParPlat(), messageErreur)) {
                //Sauvegarde.
                //Suppression de la catégorie.
                orm.supprimerNUplet(categorie);

                //Message de résultat.
                ui.afficher("Catégorie supprimée !");
            }
        }
    }

    /**
     * Lister les catégories de plats disponibles de la carte du jour.
     */
    public static void listerDisponiblesCarte() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des catégories des plats disponibles de la carte du jour :");

        //Récupération des catégories existantes avec des plats disponibles.
        String predicat = "WHERE FROM_TABLE.ID IN ( " +
                          "SELECT C.ID " +
                          "FROM CATEGORIE AS C " +
                          "INNER JOIN PLAT AS P " +
                          "ON P.ID_CATEGORIE = C.ID " +
                          "INNER JOIN PLAT_INGREDIENTS AS PI " +
                          "ON PI.ID_PLAT = P.ID " +
                          "INNER JOIN INGREDIENT AS I " +
                          "ON I.ID = PI.ID_INGREDIENT " +
                          "WHERE PI.QUANTITE <= I.STOCK " +
                          "AND P.CARTE = 1)";
        List<Entite> categories = orm.chercherNUpletsAvecPredicat(predicat, Categorie.class);

        //Si des catégories avec des plats disponibles de la carte ont été trouvées.
        String messageErreur = "Pour aucune catégorie du cataloque, des plats de la carte sont disponibles !";
        if(!ui.afficherSiListeNUpletsVide(categories, messageErreur)) {
            //Listing.
            ui.listerNUplets(categories);
        }
    }
}