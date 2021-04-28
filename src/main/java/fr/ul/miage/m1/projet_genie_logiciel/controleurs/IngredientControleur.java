package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;
/**
 * Controleur pour les ingrédient.
 */

public class IngredientControleur extends Controleur {
    /**
     * Ajouter un ingrédient.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nAjout d'un ingrédient :");

        String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES, false);
        Double stock = ui.poserQuestionDecimal("Saisir le stock" , UI.REGEX_GRAND_DECIMAL_POSITIF , false);
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        int idUnite = ui.poserQuestionListeNUplets(liste);

        //insertion d'un ingredient
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle(libelle);
        ingredient.setStock(stock);
        ingredient.setIdUnite(idUnite);

        //Ajouter l'ingrédient' dans la base de données
        orm.persisterNUplet(ingredient);

        ui.afficher("Ingrédient ajouté! ");

        //retourner à l'accueil
        AccueilControleur.get();
    }
    /**
     * Lister les ingédients.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nLister les ingrédient :");
        //récupérer les ingrédients de la base
        List<Entite> liste = orm.chercherTousLesNUplets(Ingredient.class);
        //le cas ou la liste est vide :
        if(liste.isEmpty()){
            ui.afficher("\n"+UI.DELIMITEUR+ "\nAucun ingrédient trouvé dans la liste !");
        }//sinon
        else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucun ingrédient trouvé dans la liste !");
            //retourner la liste des ingrédients
            ui.listerNUplets(liste);
        }
        //Retour à l'accueil
        AccueilControleur.get();
    }
    /**
     * Modifier un ingédient.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nModifier un ingrédient :");

        //Afficher les ingrédients de la base
        List<Entite> liste = orm.chercherTousLesNUplets(Ingredient.class);
        if (liste.isEmpty()){
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucun ingrédient trouvé dans le cataloque !");
        }
        else{
            ui.afficher("\n" + UI.DELIMITEUR + "\nListing des ingrédients du catalogue :");
            int idIngredient = ui.poserQuestionListeNUplets(liste);
            //Modifier l'ingrédient choisi
            Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = " + idIngredient, Ingredient.class);
            String libelle = ui.poserQuestion("Saisir le nouveau libellé : ", UI.REGEX_CHAINE_DE_CARACTERES, false);
            ingredient.setLibelle(libelle);

            //Ajouter la modification dans la base de données
            orm.persisterNUplet(ingredient);

            ui.afficher("Ingrédient modifié! ");

        }

        //retourner à l'accueil
        AccueilControleur.get();


    }
    /**
     * Supprimer un ingédient.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nModifier un ingrédient :");

        //Afficher tous les ingrédients de la base
        List<Entite> liste = orm.chercherTousLesNUplets(Ingredient.class);
        //si la liste des ingrédients est vide
        if(liste.isEmpty()){
            ui.afficher("\n"+UI.DELIMITEUR+"\nAucun ingrédient trouvé dans le catalogue!");
        }
        //sinon
        else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nListing des ingrédients du catalogue :");
            int idIngredient = ui.poserQuestionListeNUplets(liste);

            //Supprimer l'ingrédient sélectionné de la base
            Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = " + idIngredient, Ingredient.class);
            orm.supprimerNUplet(ingredient);
            ui.afficher("Ingrédient supprimé! ");
        }
        //retourner à l'accueil
        AccueilControleur.get();
    }
}
