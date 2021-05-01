package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Controleur pour les ingrédients.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class IngredientControleur extends Controleur {
    /**
     * Lister les ingrédients.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des ingrédients :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si pas d'ingrédients dans le catalogue.
        if(ingredients.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun ingrédient trouvé dans le catalogue !");
        //Sinon.
        } else {
            //Listing.
            ui.listerNUplets(ingredients);
        }

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Ajouter un ingrédient.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un ingrédient :");

        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);
        //Si pas d'unités dans le catalogue.
        if(unites.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune unité trouvée dans le cataloque à associer pour l'ingrédient à ajouter !");
            ui.afficher("Ajoutez d'abord une unité avant d'ajouter un ingrédient !");
        //Sinon.
        } else {
            //Questions et saisies.
            String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);;
            ui.afficher("Saisir une unité :");
            int idUnite = ui.poserQuestionListeNUplets(unites);

            //Sauvegarde : insertion de l'ingrédient.
            Ingredient ingredient = new Ingredient();
            ingredient.setLibelle(libelle);
            ingredient.setStock(0.0);
            ingredient.setIdUnite(idUnite);
            orm.persisterNUplet(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient ajouté !");
            ui.afficher(ingredient.toString());
        }

        //retourner à l'accueil
        AccueilControleur.consulter();
    }

    /**
     * Modifier un ingédient.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'un ingrédient :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si pas d'ingrédoents dans le catalogue.
        if (ingredients.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun ingrédient trouvé dans le cataloque !");
        //Sinon.
        } else {
            //Questions et saisies.
            int idIngredient = ui.poserQuestionListeNUplets(ingredients);
            Ingredient ingredient = (Ingredient) filterListeNUpletsAvecId(ingredients, idIngredient);
            String libelle = ui.poserQuestion("Saisir le nouveau libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);
            ui.afficher("Saisier une nouvelle unité :");
            List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);
            int idUnite = ui.poserQuestionListeNUplets(unites);

            //Sauvegarde : modification de l'ingrédient.
            ingredient.setLibelle(libelle);
            ingredient.setIdUnite(idUnite);
            orm.persisterNUplet(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient modifié !");
            ui.afficher(ingredient.toString());
        }

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Supprimer un ingédient.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un ingrédient :");

        //Ingrédients.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si pas d'ingrédients dans le catalogue.
        if(ingredients.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun ingrédient trouvé dans le catalogue !");
        } else {
            //Questions et saisies.
            int idIngredient = ui.poserQuestionListeNUplets(ingredients);
            Ingredient ingredient = (Ingredient) filterListeNUpletsAvecId(ingredients, idIngredient);

            //Sauvegarde : suppression de l'ingrédient.
            orm.supprimerNUplet(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient supprimé !");
        }

        //retourner à l'accueil
        AccueilControleur.consulter();
    }
    /**
     * Modifier le stock d'un ingédient.
     */
    public static void incrementerStock() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Incrementation du stock d'un ingrédient :");

        //Récupération des ingrédients existants.
        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si pas d'ingrédoents dans le catalogue.
        if (ingredients.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun ingrédient trouvé dans le cataloque !");
            //Sinon.
        } else {
            //Questions et saisies.
            int idIngredient = ui.poserQuestionListeNUplets(ingredients);
            Ingredient ingredient = (Ingredient) filterListeNUpletsAvecId(ingredients, idIngredient);
            Double stock = ui.poserQuestionDecimal("Saisir le stock que vous voulez ajouter :" , UI.REGEX_GRAND_DECIMAL_POSITIF);
            //Sauvegarde : incrémentation du stock de l'ingrédient.
            ingredient.setStock(ingredient.getStock()+stock);
            orm.persisterNUplet(ingredient);

            //Message de résultat.
            ui.afficher("Ingrédient modifié !");
            ui.afficher(ingredient.toString());
        }

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }
}