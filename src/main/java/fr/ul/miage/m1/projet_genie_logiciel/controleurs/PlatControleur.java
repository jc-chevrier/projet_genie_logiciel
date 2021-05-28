package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la gestion des plats.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlatControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUN_TROUVE = "Aucun plat trouvé dans le cataloque !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner un plat :";

    /**
     * Lister les plats.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats du catalogue :");

        //Récupération des plats existants.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si des plats ont été trouvés dans le catalogue.
        if (!ui.afficherSiListeNUpletsVide(plats, MESSAGE_AUCUN_TROUVE)) {
            //Listing.
            ui.listerNUplets(plats);
        }
    }

    /**
     * Choisir un ingrédient à ajouter à un plat, et saisir la
     * quantité associée pour le plat.
     *
     * La méthode prend en paramètre la composition actuelle.
     *
     * La méthode renvoie la ligne de composition ajoutée,
     * ou null si plus d'ingrédient à ajouter au plat.
     *
     * @param composition
     * @return
     */
    private static PlatIngredients saisirPlatIngredient(@NotNull List<PlatIngredients> composition) {
        //Ingrédients pas encore sélectionnés.
        List<Entite> ingredients;
        //Si la composition actuelle du plat est vide.
        if (composition.isEmpty()) {
            ingredients = orm.chercherTousLesNUplets(Ingredient.class);
        } else {
            //On n'autorise pas les ingrédients déjà présents dans la composition.
            List<String> idsIngredientsDejaSelectionnes = composition
                                                         .stream()
                                                         .map(platIngredient -> platIngredient.getIdIngredient().toString())
                                                         .collect(Collectors.toList());
            ingredients = orm.chercherNUpletsAvecPredicat("WHERE ID NOT IN (" +
                                                          String.join(",", idsIngredientsDejaSelectionnes) + ")",
                                                          Ingredient.class);
        }

        //Si plus d'ingrédients disponibles pour la composition du plat.
        if (ingredients.isEmpty()) {
            return null;
        } else {
            //Questions et saisies.
            ui.afficher("Ajout d'un ingrédient au plat :");
            int idIngredient = ui.poserQuestionListeNUplets("Sélectionner un ingrédient :", ingredients).getId();
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
        //Saisie des ingrédients du plat.
        ui.afficher("\n" + "Composition du plat :");
        List<PlatIngredients> platIngredients = new ArrayList<PlatIngredients>();
        boolean continuerComposition;
        do {
            PlatIngredients platIngredient = saisirPlatIngredient(platIngredients);
            if (platIngredient == null) {
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
        //Récupération des catégories.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Plat.
        //Questions et saisies.
        //Choix de la catégorie.
        int idCategorie = ui.poserQuestionListeNUplets("Sélectionner une catégorie :", categories).getId();
        //Saisie des caractéristiques du plat.
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES);
        Double prix = ui.poserQuestionDecimal("Saisir un prix :", UI.REGEX_DECIMAL_POSITIF);
        //Composition du plat.
        List<PlatIngredients> platIngredients = composer();

        //Sauvegarde : insertion du plat et de sa composition.
        //Plat.
        plat.setIdCategorie(idCategorie);
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
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un plat au catalogue :");

        //Récupération du nombre de catégories et du nombre d'ingrédients.
        int nbCategories = orm.compterTousLesNUplets(Categorie.class);
        int nbIngredients = orm.compterTousLesNUplets(Ingredient.class);

        //Si pas de catégories trouvées.
        String messageErreur = "Aucune catégorie trouvée pour les plats !\n" +
                               "Ajoutez d'abord des catégories avant d'ajouter un plat !";
        if (!ui.afficherSiNombreNul(nbCategories, messageErreur)) {
            //Si pas d'ingrédient dans le catalogue.
            messageErreur = "Aucun ingrédient trouvé dans le cataloque pour composer le plat !\n" +
                            "Ajoutez d'abord des ingrédients avant d'ajouter un plat !";
            if (!ui.afficherSiNombreNul(nbIngredients, messageErreur)) {
                //Saisies et sauvegarde.
                Plat plat = new Plat();
                editerEtPersister(plat);

                //Message de résultat.
                ui.afficher("Plat ajouté !\n" + plat);
            }
        }
    }

    /**
     * Modifier une plat.
     */
    public static void modifier() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'un plat :");

        //Récupération des plats existantes.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si des plats ont été trouvés dans le catalogue.
        if (!ui.afficherSiListeNUpletsVide(plats, MESSAGE_AUCUN_TROUVE)) {
            //Question et saisies.
            //Saisie du plat à modifier.
            Plat plat = (Plat) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, plats);

            //Saisies et sauvegarde.
            //Suppression de l'ancienne composition du plat.
            orm.supprimerNUpletsAvecPredicat("WHERE ID_PLAT = " + plat.getId(), PlatIngredients.class);
            //Nouvelles caractéristiques, et nouvelle composition.
            editerEtPersister(plat);

            //Message de résultat.
            ui.afficher("Plat modifié !\n" + plat);
        }
    }

    /**
     * Suuprimer un plat.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un plat :");

        //Récupération des plats existants.
        List<Entite> plats = orm.chercherTousLesNUplets(Plat.class);

        //Si des plats ont été trouvés dans le catalogue.
        if (!ui.afficherSiListeNUpletsVide(plats, MESSAGE_AUCUN_TROUVE)) {
            //Question et saisies.
            Plat plat = (Plat) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, plats);

            //Si le plat n'est pas utilisé par des lignes de commande.
            String messageErreur = "Ce plat est utilisé par des lignes de commande, il ne peut pas être supprimé !";
            if(!ui.afficherSiPredicatVrai(plat.estUtiliseParLigneCommande(), messageErreur)) {
                //Sauvegarde.
                //Suppression de la composition du plat.
                orm.supprimerNUpletsAvecPredicat("WHERE ID_PLAT = " + plat.getId(), PlatIngredients.class);
                //Suppression du plat.
                orm.supprimerNUplet(plat);

                //Message de résultat.
                ui.afficher("Plat supprimé !");
            }
        }
    }

    /**
     * Lister tous les plats de la carte.
     */
    public static void listerCarte() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing de tous les plats de la carte :");

        //Récupération des plats de la carte.
        List<Entite> platsCarte = orm.chercherNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);

        //Si la carte n'est pas vide.
        String messageErreur = "La carte est vide !";
        if (!ui.afficherSiListeNUpletsVide(platsCarte, messageErreur)) {
            //Listing.
            ui.listerNUplets(platsCarte);
        }
    }

    /**
     * Ajouter un plat à la carte du jour.
     */
    public static void ajouterACarte() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un plat à la carte du jour :");

        //Récupération des plats qui ne font pas partie de la carte.
        List<Entite> plats = orm.chercherNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);

        //Si des plats ne faisant pas parti de la carte ont été trouvés.
        String messageErreur = "Aucun plat ne faisant pas déjà parti de la carte trouvé !";
        if (!ui.afficherSiListeNUpletsVide(plats, messageErreur)) {
            //Question et saisies.
            Plat plat = (Plat) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, plats);

            //Sauvegarde : modification du plat.
            //Ajout du plat à la carte.
            plat.setCarte(1);
            orm.persisterNUplet(plat);

            //Message de résultat.
            ui.afficher("Plat ajouté à la carte du jour !\n" + plat);
        }
    }

    /**
     * Supprimer un plat de la carte du jour.
     */
    public static void supprimerDeCarte() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un plat de la carte du jour :");

        //Récupération des plats qui font partie de la carte.
        List<Entite> platsCarte = orm.chercherNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);

        //Si la carte n'est pas vide.
        String messageErreur = "La carte est vide !";
        if (!ui.afficherSiListeNUpletsVide(platsCarte, messageErreur)) {
            //Question et saisies.
            Plat plat = (Plat) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, platsCarte);

            //Sauvegarde : modification du plat.
            //Suppression du plat de la carte.
            plat.setCarte(0);
            orm.persisterNUplet(plat);

            //Message de résultat.
            ui.afficher("Plat supprimé de la carte du jour !\n" + plat);
        }
    }

    /**
     * Lister les plats disponibles de la carte.
     */
    public static void listerDisponiblesCarte() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats disponibles de la carte du jour :");

        //Récupération des plats disponibles de la carte.
        String predicat = "WHERE CARTE = 1 " +
                          "AND ID NOT IN " +
                            "(SELECT P.ID " +
                            "FROM PLAT AS P "+
                            "INNER JOIN PLAT_INGREDIENTS AS PI " +
                            "ON PI.ID_PLAT = P.ID " +
                            "INNER JOIN INGREDIENT AS I " +
                            "ON I.ID = PI.ID_INGREDIENT " +
                            "WHERE PI.QUANTITE > I.STOCK) " ;
        List<Entite> platsDisponiblesCarte = orm.chercherNUpletsAvecPredicat(predicat, Plat.class);

        //Si des plats de la carte sont disponibles.
        String messageErreur = "Aucun plat de la carte disponible trouvé !";
        if (!ui.afficherSiListeNUpletsVide(platsDisponiblesCarte, messageErreur)) {
            //Listing.
            ui.listerNUplets(platsDisponiblesCarte);
        }
    }

    /**
     * Lister les plats disponibles de la carte du jour
     * pour une catégorie.
     */
    public static void listerDisponiblesPourCategorie() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats disponibles dans la carte du jour pour une catégorie :");

        //Récupération des catégories.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Question et saisies.
        int idCategorie = ui.poserQuestionListeNUplets("Sélectionner une catégorie :", categories).getId();

        //Récupération des plats disponibles pour une categorie.
        String predicat = "WHERE FROM_TABLE.ID IN ( " +
                          "SELECT P.ID " +
                          "FROM PLAT AS P " +
                          "INNER JOIN CATEGORIE AS C " +
                          "ON P.ID_CATEGORIE = C.ID " +
                          "INNER JOIN PLAT_INGREDIENTS AS PI " +
                          "ON PI.ID_PLAT = P.ID " +
                          "INNER JOIN INGREDIENT AS I " +
                          "ON I.ID = PI.ID_INGREDIENT " +
                          "WHERE PI.QUANTITE <= I.STOCK " +
                          "AND P.CARTE = 1 " +
                          "AND C.ID = " + idCategorie + ")";
        List<Entite> platsDisponiblesCarte = orm.chercherNUpletsAvecPredicat(predicat, Plat.class);

        //Si des plats de la carte sont disponibles,
        //pour la catégorie sélectionnée.
        String messageErreur = "Aucun plat disponible de la carte trouvé pour cette catégorie !";
        if (!ui.afficherSiListeNUpletsVide(platsDisponiblesCarte, messageErreur)) {
            //Listing.
            ui.listerNUplets(platsDisponiblesCarte);
        }
    }

    /**
     * Chercher un plat avec son libellé.
     */
    public static void chercherAvecLibelle() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Chercher un plat avec libellé :\n\n" +
                                               "La recherche supporte la casse.\n" +
                                               "Le libellé peut être saisi partiellement.");

        //Questions et saisies.
        String libelle = ui.poserQuestion("Saisir un libellé de plat :", UI.REGEX_CHAINE_DE_CARACTERES).toLowerCase();

        //Récupération des plats existants correspond au libellé.
        List<Entite> plats = orm.chercherNUpletsAvecPredicat("WHERE LOWER(LIBELLE) LIKE '%" + libelle + "%'", Plat.class);

        //Si des plats ont été trouvés pour ce libellé.
        String messageErreur = "Aucun plat trouvé pour ce libellé !";
        if (!ui.afficherSiListeNUpletsVide(plats, messageErreur)) {
            //Listing.
            ui.listerNUplets(plats);
        }
    }
}