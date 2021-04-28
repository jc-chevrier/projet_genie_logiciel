package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion du catalogue
 * des plats.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlatControleur extends Controleur {
    /**
     * Choisir un ingrédient pour un plat,
     * et saisir la quantité associée pour le plat.
     *
     * La méthode prend en paramètre la composition du plat actuelle.
     *
     * @param composition
     * @return
     */
    private static PlatIngredients saisirPlatIngredient(@NotNull List<PlatIngredients> composition) {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Ingrédients pas encore sélectionnés.
        List<Entite> ingredients;
        //Si la composition actuelle du plat est vide.
        if(composition.isEmpty()) {
            ingredients = orm.chercherTousLesNUplets(Ingredient.class);
        //Sinon.
        } else {
            //On n'autorise pas les ingrédients déjà présents dans la composition.
            List<String> idsIngredientsDejaSelectionnes = composition
                                                          .stream()
                                                          .map(platIngredient -> platIngredient.getIdIngredient().toString())
                                                          .collect(Collectors.toList());
            ingredients = orm.chercherNUpletsAvecPredicat(
                                             "WHERE ID NOT IN (" + String.join(",", idsIngredientsDejaSelectionnes) + ")",
                                             Ingredient.class);
        }

        //Si plus d'ingrédients disponibles pour la composition du plat.
        if(ingredients.isEmpty()) {
            return null;
        //Sinon.
        } else {
            //Questions.
            ui.afficher("Ajout d'un ingrédient au plat :");
            int idIngredient = ui.poserQuestionListeNUplets(ingredients);
            double quantite = ui.poserQuestionDecimal("Saisir une quantité : ", UI.REGEX_GRAND_DECIMAL_POSITIF, false);

            //Création du n-uplet PlatIngredients.
            PlatIngredients platIngredient = new PlatIngredients();
            platIngredient.setQuantite(quantite);
            platIngredient.setIdIngredient(idIngredient);

            return platIngredient;
        }
    }

    /**
     * Composer un plat.
     *
     * @return
     */
    private static List<PlatIngredients> composer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Saisie des ingrédients du plat.
        ui.afficher("\n" + "Composition du plat :");
        List<PlatIngredients> platIngredients = new ArrayList<PlatIngredients>();
        boolean continuerComposition;
        do {
            PlatIngredients platIngredient = saisirPlatIngredient(platIngredients);
            if(platIngredient == null) {
                ui.afficher("Plus d'ingrédients disponibles !");
                continuerComposition = false;
            } else {
                platIngredients.add(platIngredient);
                ui.afficher("Ingrédient ajouté !");
                continuerComposition = ui.poserQuestionFermee("Voulez-vous ajouter un autre ingrédient ?", false);
            }
        } while (continuerComposition);
        ui.afficher("Composition du plat terminée !");

        return platIngredients;
    }

    /**
     * Ajouter un plat.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Plat.
        ui.afficher("\n" + UI.DELIMITEUR + "\nAjout d'un plat au catalogue :");
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES, false);
        Double prix = ui.poserQuestionDecimal("Saisir un prix : ", UI.REGEX_DECIMAL_POSITIF, false);

        //Composition du plat.
        List<PlatIngredients> platIngredients = composer();

        //Sauvegarde.
        Plat plat = new Plat();
        plat.setLibelle(libelle);
        plat.setPrix(prix);
        plat.setCarte(0);
        orm.persisterNUplet(plat);
        platIngredients.forEach((platIngredient) -> {
            platIngredient.setIdPlat(plat.getId());
            orm.persisterNUplet(platIngredient);
        });
        ui.afficher("Plat ajouté !");
        ui.afficher(plat.toString());

        //Retour vers l'accueil.
        AccueilControleur.get();
    }

    /**
     * Lister les plats.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Listing.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);
        //Si pas de plats dans le cataloque.
        if(plats.isEmpty()) {
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucun plat trouvé dans le cataloque !");
        //Sinon.
        } else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nListing des plats du catalogue :");
            ui.listerNUplets(plats);
        }

        //Retour vers l'accueil.
        AccueilControleur.get();
    }
}
