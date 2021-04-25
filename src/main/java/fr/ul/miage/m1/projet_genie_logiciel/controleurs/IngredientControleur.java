package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

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

        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle(libelle);
        ingredient.setStock(stock);
        ingredient.setIdUnite(idUnite);

        orm.persisterNUplet(ingredient);
        ui.afficher("Ingrédient ajouté! ");

        AccueilControleur.get();
    }
    /**
     * Lister ingédients.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nLister les ingrédient :");
        List<Entite> liste = orm.chercherTousLesNUplets(Ingredient.class);
        ui.listerNUplets(liste);
        AccueilControleur.get();

    }
}
