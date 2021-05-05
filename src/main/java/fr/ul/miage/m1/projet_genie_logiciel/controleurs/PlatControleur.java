package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour le catalogue des plats.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlatControleur extends Controleur {
    /**
     * Lister les plats.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats du catalogue :");

        //Récupération des plats exsitants.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si pas de plats dans le cataloque.
        if(plats.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun plat trouvé dans le cataloque !");
        //Sinon.
        } else {
            //Listing.
            ui.listerNUplets(plats);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

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
            //Questions et saisies.
            ui.afficher("Ajout d'un ingrédient au plat :");
            int idIngredient = ui.poserQuestionListeNUplets(ingredients);
            double quantite = ui.poserQuestionDecimal("Saisir une quantité : ", UI.REGEX_GRAND_DECIMAL_POSITIF);

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
                continuerComposition = ui.poserQuestionFermee("Voulez-vous ajouter un autre ingrédient ?");
            }
        } while (continuerComposition);
        ui.afficher("Composition du plat terminée !");

        return platIngredients;
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder un plat,
     * qu'il soit déjà ajouté ou non.
     *
     * @param plat
     */
    private static void editerEtPersister(Plat plat) {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Plat.
        //Questions et saisies.
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES);
        Double prix = ui.poserQuestionDecimal("Saisir un prix : ", UI.REGEX_DECIMAL_POSITIF);

        //Composition du plat.
        List<PlatIngredients> platIngredients = composer();

        //Sauvegarde : insertion du plat et de sa composition.
        //Plat.
        plat.setLibelle(libelle);
        plat.setPrix(prix);
        plat.setCarte(0);
        orm.persisterNUplet(plat);
        //Composition.
        platIngredients.forEach((platIngredient) -> {
            platIngredient.setIdPlat(plat.getId());
            orm.persisterNUplet(platIngredient);
        });
    }

    /**
     * Ajouter un plat.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un plat au catalogue :");

        List<Entite> ingredients = orm.chercherTousLesNUplets(Ingredient.class);

        //Si pas d'ingrdéient dans le catalogue.
        if(ingredients.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun ingrédient trouvé dans le cataloque pour composer le plat !");
            ui.afficher("Ajoutez d'abord des ingrédients avant d'ajouter un plat !");
        //Sinon.
        } else {
            //Saisie et sauvegarde.
            Plat plat = new Plat();
            editerEtPersister(plat);

            //Message de résultat.
            ui.afficher("Plat ajouté !");
            ui.afficher(plat.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Modifier une plat.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'un plat :");

        //Récupération des plats existantes.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si pas d'unités trouvées.
        if(plats.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune plat trouvé dans le cataloque !");
            //Sinon.
        } else {
            //Saisie du plat à modofier.
            int idPlat = ui.poserQuestionListeNUplets(plats);
            Plat plat = (Plat) filterListeNUpletsAvecId(plats, idPlat);

            //Suppression de l'ancienne composition du plat.
            orm.chercherNUpletsAvecPredicat("WHERE ID_PLAT = " + idPlat, PlatIngredients.class)
               .forEach(orm::supprimerNUplet);

            //Saisie et sauvegarde.
            editerEtPersister(plat);

            //Message de résultat.
            ui.afficher("Plat modifié !");
            ui.afficher(plat.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Suuprimer un plat.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un plat :");

        //Récupération des plats exsitants.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si pas de plats dans le cataloque.
        if(plats.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun plat trouvé dans le cataloque !");
        //Sinon.
        } else {
            //Question et saisies.
            int idPlat = ui.poserQuestionListeNUplets(plats);
            Plat plat = (Plat) filterListeNUpletsAvecId(plats, idPlat);

            //Sauvegarde : suppression du plat et de sa composition.
            List<Entite> platIngredients = orm.chercherNUpletsAvecPredicat("WHERE ID_PLAT = " + plat.getId(),
                                                                           PlatIngredients.class);
            platIngredients.forEach(orm::supprimerNUplet);
            orm.supprimerNUplet(plat);

            //Message de résultat.
            ui.afficher("Plat supprimé !");
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
