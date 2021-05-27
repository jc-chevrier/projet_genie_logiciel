package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Contrôleur pour la gestion des ingrédients.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class IngredientControleur extends Controleur {
    //Messages.
    private final static String MESSAGE_AUCUN_TROUVE = "Aucun ingrédient trouvé dans le cataloque !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner un ingrédient :";

    /**
     * Lister les ingrédients.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des ingrédients :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si des ingrédients ont été trouvés dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(ingredients, MESSAGE_AUCUN_TROUVE)) {
            //Listing.
            ui.listerNUplets(ingredients);
        }
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder un ingrédient,
     * qu'il soit déjà existant en base de données ou non.
     *
     * @param ingredient
     */
    private static void editerEtPersister(@NotNull Ingredient ingredient) {
        //Questions et entrées.
        //Saisie du libellé de l'ingrédient.
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES);
        //Choix de l'unité de l'ingrédient.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);
        int idUnite = ui.poserQuestionListeNUplets("Sélectionner une unité :", unites).getId();

        //Sauvegarde.
        //Insertion / mise à jour de l'ingrédient.
        ingredient.setLibelle(libelle);
        ingredient.setIdUnite(idUnite);
        orm.persisterNUplet(ingredient);
    }

    /**
     * Ajouter un ingrédient.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un ingrédient :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si des unités ont été trouvées dans le catalogue.
        String messageErreur = "Aucune unité trouvée dans le cataloque à associer pour l'ingrédient à ajouter !\n" +
                               "Ajoutez d'abord une unité avant d'ajouter un ingrédient !";
        if(!ui.afficherSiListeNUpletsVide(unites, messageErreur)) {
            //Saisie et sauvegarde.
            Ingredient ingredient = new Ingredient();
            ingredient.setStock(0.0);
            editerEtPersister(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient ajouté !\n" + ingredient);
        }
    }

    /**
     * Modifier un ingrédient.
     */
    public static void modifier() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'un ingrédient :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si des ingrédients ont été trouvés dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(ingredients, MESSAGE_AUCUN_TROUVE)) {
            //Questions et saisies.
            //Choix de l'ingrédient.
            Ingredient ingredient = (Ingredient) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, ingredients);

            //Saisie et sauvegarde.
            editerEtPersister(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient modifié !\n" + ingredient);
        }
    }

    /**
     * Supprimer un ingrédient.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un ingrédient :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si des ingrédients ont été trouvés dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(ingredients, MESSAGE_AUCUN_TROUVE)) {
            //Questions et saisies.
            //Choix de l'ingrédient.
            Ingredient ingredient = (Ingredient) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, ingredients);

            //Si l'ingrédient n'est pas utilisé par des plats.
            String messageErreur = "Cet ingrédient est utilisé par des plats, il ne peut pas être supprimé !";
            if(!ui.afficherSiPredicatVrai(ingredient.estUtiliseParPlat(), messageErreur)) {
                //Sauvegarde : suppression de l'ingrédient.
                orm.supprimerNUplet(ingredient);

                //Message de résultat.
                ui.afficher("Ingrédient supprimé !");
            }
        }
    }

    /**
     * Modifier le stock d'un ingrédient.
     */
    public static void modifierStock() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification du stock d'un ingrédient :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si des ingrédients ont été trouvés dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(ingredients, MESSAGE_AUCUN_TROUVE)) {
            //Questions et saisies.
            //Choix de l'ingrédient.
            Ingredient ingredient = (Ingredient) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, ingredients);

            //(La vérification est trop complexe à faire avec une expression
            //régulière, on l'a fait avec une boucle).
            double stockActuel = ingredient.getStock();
            boolean nouveauStockNegatif;
            double quantite;
            do {
                quantite = ui.poserQuestionDecimal("Saisir le stock que vous voulez ajouter ou retirer :",
                                                   UI.REGEX_GRAND_DECIMAL_POSITIF_OU_NEGATIF);
                nouveauStockNegatif = quantite < 0 && (stockActuel + quantite) < 0;
                if(nouveauStockNegatif) {
                    ui.afficher("La quantité à retirer est plus grande que le stock actuel !");
                }
            } while(nouveauStockNegatif);

            //Sauvegarde.
            //Modification du stock de l'ingrédient.
            ingredient.setStock(ingredient.getStock() + quantite);
            orm.persisterNUplet(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient modifié !\n" + ingredient);
        }
    }
}