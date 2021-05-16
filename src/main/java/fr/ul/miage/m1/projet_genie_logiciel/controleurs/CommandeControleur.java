package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Controleur pour les commandes.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CommandeControleur extends Controleur {
    /**
     * Saisir une ligne de ocmmande
     * pour la compsotion d'une commande.
     *
     * @param composition
     * @return
     */
    private static LigneCommande saisirLigneCommande(@NotNull List<LigneCommande> composition) {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Si la composition ne contient encore aucune ligne de commande.
        List<Entite> platsDisponibles;
        if (composition.isEmpty()) {
            platsDisponibles = orm.chercherNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK,
                                                                        Plat.class);
        //Sinon.
        } else {
            //On autorise pas les plats déjà slécionnés pour la commande.
            List<String> idsPlatsDejaSelectionnes = composition.stream()
                                                                .map(ligneCommande -> ligneCommande.getIdPlat()
                                                                                                       .toString())
                                                                .collect(Collectors.toList());

            platsDisponibles = orm.chercherNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK + " " +
                                                                        "AND FROM_TABLE.ID NOT IN (" +
                                                                        String.join(",", idsPlatsDejaSelectionnes) + ")",
                                                                        Plat.class);
        }

        //Si plus de plats disponibles no nutilisés dans la composition.
        if (platsDisponibles.isEmpty()) {
            return null;
        //Sinon.
        } else {
            //Questions et saisies.
            ui.afficher("Ajout d'un plat à la commande :");
            int idPlat = ui.poserQuestionListeNUplets("Sélectionner un plat :", platsDisponibles);
            int nbOccurencesMax = orm.chercherNUpletsAvecPredicat( "WHERE ID_PLAT = " + idPlat, PlatIngredients.class)
                                     .stream()
                                     .mapToInt(platIngredients -> {
                                        PlatIngredients platIngredients_ = (PlatIngredients) platIngredients;
                                        Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat(
                                                                                "WHERE ID = " + platIngredients_.getIdIngredient(),
                                                                                         Ingredient.class);
                                        return (int) Math.floor(ingredient.getStock() / platIngredients_.getQuantite());
                                    })
                                    .min()
                                    .getAsInt();
            String regexNbOccurencesPossibles = "^";
            for(int i = 1; i <= nbOccurencesMax; i++) {
                regexNbOccurencesPossibles += i + "{1}" + (i == nbOccurencesMax ? "" : "|");
            }
           regexNbOccurencesPossibles += "$";
           int nbOccurences = ui.poserQuestionEntier("Saisir un nombre d'occurences : ", regexNbOccurencesPossibles);

            //Création du n-uplet de la la ligne de commande.
            LigneCommande ligneCommande = new LigneCommande();
            ligneCommande.setIdPlat(idPlat);
            ligneCommande.setEtat("en attente");
            ligneCommande.setNbOccurences(nbOccurences);

            return ligneCommande;
        }
    }

    /**
     * Composer une commande.
     *
     * @return
     */
    private static List<LigneCommande> composer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficher("\nComposition d'une commande :");

        //Saisies de la composition.
        ArrayList lignesCommande = new ArrayList();
        boolean continuerComposition;
        do {
            LigneCommande ligneCommande = saisirLigneCommande(lignesCommande);
            if (ligneCommande == null) {
                ui.afficher("Plus de plats disponibles !");
                continuerComposition = false;
            } else {
                lignesCommande.add(ligneCommande);
                ui.afficher("Plat(s) ajouté(s) à la commande !");
                continuerComposition = ui.poserQuestionFermee("Voulez-vous ajouter un autre plat à la commande ?");
            }
        } while(continuerComposition);

        ui.afficher("Composition de la commande terminée !");
        return lignesCommande;
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder une commande,
     * qu'elle soit déjà ajoutée ou non.
     *
     * @param commande
     */
    private static void editerEtPersister(@NotNull Commande commande) {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Récupération des tables occupées du restaurant allouées au serveur connecté.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' " +
                                                                              "AND ID_COMPTE_SERVEUR = " +
                                                                               getUtilisateurConnecte().getId(),
                                                                               Place.class);

        //Questions et saisies.
        //Choix d'une table du restaurant.
        int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees,
                                                    (place) -> ((Place)place).toEtatServeurString());
        //Composition des commandes.
        List<LigneCommande> ligneCommandes = composer();

        //Sauvegarde d'une commande.
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(idPlace);
        commande.setEtat("en attente");
        commande.setCoutTotal(ligneCommandes.stream().mapToDouble(ligneCommande -> {
            Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = " + ligneCommande.getIdPlat(),
                                                                       Plat.class);
            return (double) ligneCommande.getNbOccurences() * plat.getPrix();
        }).reduce(Double::sum).getAsDouble());
        orm.persisterNUplet(commande);

        //On alloue le stock pour cette commande.
        ligneCommandes.forEach(ligneCommande ->
                orm.chercherNUpletsAvecPredicat( "WHERE ID_PLAT = " + ligneCommande.getIdPlat(), PlatIngredients.class)
                   .forEach(platIngredients -> {
                       PlatIngredients platIngredients_ = (PlatIngredients) platIngredients;
                       Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat(
                                                            "WHERE ID = " + platIngredients_.getIdIngredient(),
                                                                     Ingredient.class);
                       ingredient.setStock(ingredient.getStock() - (ligneCommande.getNbOccurences() * platIngredients_.getQuantite()));
                       orm.persisterNUplet(ingredient);
                   })
        );

        //Sauvegarde des lignes de la commande.
        ligneCommandes.forEach((ligneCommande) -> {
            ligneCommande.setIdCommande(commande.getId());
            orm.persisterNUplet(ligneCommande);
        });
    }

    /**
     * Ajouter une commande.
     */
    public static void ajouter() {
        ///UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une commande :");

        //Récupération des tables occupées du restaurant allouées au serveur connecté.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' " +
                                                                               "AND ID_COMPTE_SERVEUR = " +
                                                                                getUtilisateurConnecte().getId(),
                                                                                Place.class);
        //Récupération des plats disponibles du restaurant par rapport aux stocks.
        List<Entite> platsDisponibles = orm.chercherNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK,
                                                                         Plat.class);

        //Si aucune table du serveur n'est occupée par des clients.
        if(placesOccupees.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table occupée parmi mes tables dans le restaurant !");
        //Sinon.
        } else {
            //Si pas de disponibles dans le restaurant.
            if(platsDisponibles.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucun plat disponible en stock : pour aucun plat les quantités d'ingrédients " +
                                     "requises sont toutes présentes en stock !");
            //Sinon.
            } else {
                //Saisie et sauvegarde.
                Commande commande = new Commande();
                editerEtPersister(commande);

                //Message de résultat.
                ui.afficher("Commande ajoutée !");
                ui.afficher(commande.toString());
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Suuprimer une commande.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une commande :");

        //Récupération des tables occupées dans le restaurant.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé'" +
                                                                               "AND ID_COMPTE_SERVEUR = " +
                                                                                getUtilisateurConnecte().getId(),
                                                                                Place.class);

        //Si pas de tables occupées trouvées dans le restaurant.
        if (placesOccupees.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table occupée trouvée parmi mes tables dans le restaurant !");
        //Sinon.
        } else {
            //Question et saisies.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees);
            //Récupération des commandes existantes en attente de préparation.
            List<Entite> commandesEnAttente = orm.chercherNUpletsAvecPredicat("INNER JOIN LIGNE_COMMANDE AS LC " +
                                                                                       "ON LC.ID_COMMANDE = FROM_TABLE.ID "+
                                                                                       "WHERE LC.ETAT = 'en attente' " +
                                                                                       "AND FROM_TABLE.ID_PLACE = " + idPlace,
                                                                                        Commande.class);

            //Si pas de commandes en attente de préparation trouvée.
            if (commandesEnAttente.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucune commande en attente de préparation trouvée pour cette table !");
                //Sinon.
            } else {
                //Question et saisies.
                int idPCommande = ui.poserQuestionListeNUplets("Sélectionner une commande :", commandesEnAttente);
                Commande commande = (Commande) filtrerListeNUpletsAvecId(commandesEnAttente, idPCommande);

                //Sauvegarde : suppression de la commande et de sa composition.
                List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("WHERE ID_COMMANDE = " +
                                                                                        commande.getId(),
                                                                                        LigneCommande.class);

                //On désalloue le stock pour cette commande.
                lignesCommande.forEach(ligneCommande -> {
                    LigneCommande ligneCommande_ = ((LigneCommande) ligneCommande);
                     orm.chercherNUpletsAvecPredicat( "WHERE ID_PLAT = " + ligneCommande_.getIdPlat(), PlatIngredients.class)
                        .forEach(platIngredients -> {
                            PlatIngredients platIngredients_ = (PlatIngredients) platIngredients;
                            Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat(
                                    "WHERE ID = " + platIngredients_.getIdIngredient(),
                                    Ingredient.class);
                            ingredient.setStock(ingredient.getStock() + (ligneCommande_.getNbOccurences() * platIngredients_.getQuantite()));
                            orm.persisterNUplet(ingredient);
                        });
                });

                lignesCommande.forEach(orm::supprimerNUplet);
                orm.supprimerNUplet(commande);

                //Message de résultat.
                ui.afficher("Commande supprimée !");
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Valider le paiement d'une commande.
     */
    public static void validerPaiement() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Valider le paiement d'une commande :");

        //Récupération des commandes servies.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);
        //Si pas de commande servie.
        if(commandes.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune commande servie trouvée !");
            //Sinon.
        } else {
            //Question et saisies.
            int idCommande = ui.poserQuestionListeNUplets("Sélectionner une commande :", commandes);
            Commande commande = (Commande) filtrerListeNUpletsAvecId(commandes, idCommande);

            //Sauvegarde : modification du plat.
            commande.setEtat("payé");
            orm.persisterNUplet(commande);

            //Message de résultat.
            ui.afficher("Commande payée !");
            ui.afficher(commande.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}