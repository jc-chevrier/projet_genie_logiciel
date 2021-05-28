package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Commande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.LigneCommande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Contrôleur pour la gestion des commandes.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CommandeControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUNE_TROUVEE = "Aucune commande trouvée !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner une commande :";

    /**
     * Lister toutes les commandes.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des commandes :");

        //Récupération des commandes existantes.
        List<Entite> commandes = orm.chercherTousLesNUplets(Commande.class);

        //Si des commandes ont été trouvées.
        if(!ui.afficherSiListeNUpletsVide(commandes, MESSAGE_AUCUNE_TROUVEE)) {
            //Listing.
            ui.listerNUplets(commandes);
        }
    }

    /**
     * Obtenir l'expression régulière définissant le nombre d'occurences
     * commandables pour un plat en fonction des stocks.
     *
     * @param idPlat
     */
    private static String obtenirRegexNombreOccurences(int idPlat) {
        //Calcul du nombre d'occurences commendable.
        int nbOccurencesMax = orm.chercherNUpletsAvecPredicat("WHERE ID_PLAT = " + idPlat, PlatIngredients.class)
                                 .stream()
                                 .mapToInt(entite -> {
                                    PlatIngredients platIngredients = (PlatIngredients) entite;
                                    Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat(
                                                                        "WHERE ID = " + platIngredients.getIdIngredient(),
                                                                        Ingredient.class);
                                    return (int) Math.floor(ingredient.getStock() / platIngredients.getQuantite());
                                 })
                                 .min()
                                 .getAsInt();

        //Construction de l'expression régulière.
        String regexNbOccurencesPossibles = "";
        for(int i = 1; i <= nbOccurencesMax; i++) {
            regexNbOccurencesPossibles += i + "{1}" + (i == nbOccurencesMax ? "" : "|");
        }

       return regexNbOccurencesPossibles;
    }

    /**
     * Saisir une ligne de commande pour la composition d'une
     * commande.
     *
     * La méthode prend en paramètre la composition actuelle.
     *
     * La méthode renvoie la nouvelle ligne de commande, ou null
     * si plus de plats commandables pour la commande.
     *
     * @param composition
     * @return
     */
    private static LigneCommande saisirLigneCommande(@NotNull List<LigneCommande> composition) {
        //Si la composition ne contient encore aucune ligne de commande.
        List<Entite> platsDisponibles;
        if (composition.isEmpty()) {
            platsDisponibles = orm.chercherNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK, Plat.class);
        } else {
            //On autorise pas les plats déjà sélectionnés pour la commande.
            List<String> idsPlatsDejaSelectionnes = composition.stream()
                                                                .map(ligneCommande -> ligneCommande.getIdPlat()
                                                                                                   .toString())
                                                                .collect(Collectors.toList());

            platsDisponibles = orm.chercherNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK + " " +
                                                               "AND FROM_TABLE.ID NOT IN (" +
                                                               String.join(",", idsPlatsDejaSelectionnes) + ")",
                                                               Plat.class);
        }

        //Si plus de plats disponibles non utilisés dans la composition.
        if (platsDisponibles.isEmpty()) {
            return null;
        } else {
            //Questions et saisies.
            ui.afficher("Ajout d'un plat à la commande :");
            //Choix du plat.
            int idPlat = ui.poserQuestionListeNUplets("Sélectionner un plat :", platsDisponibles).getId();
            //Saisie du nombre d'occurences.
            String regexNbOccurencesPossibles = obtenirRegexNombreOccurences(idPlat);
            int nbOccurences = ui.poserQuestionEntier("Saisir un nombre d'occurences : ", regexNbOccurencesPossibles);

            //Création de la la ligne de commande.
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
        //Message de titre.
        ui.afficher("\nComposition d'une commande :");

        //Saisies de la composition.
        ArrayList<LigneCommande> lignesCommande = new ArrayList<LigneCommande>();
        boolean continuerComposition;
        do {
            LigneCommande ligneCommande = saisirLigneCommande(lignesCommande);
            //Si plus de plats commandables pour la commande.
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
     * Gérer le stock nécessaire pour toutes les lignes d'une commande,
     * en précisant l'opération de gestion à utiliser.
     *
     * @param composition
     * @param operation
     */
    private static void gererStock(@NotNull List composition, @NotNull BiFunction<Double, Double, Double> operation) {
        composition.forEach(entite -> {
            LigneCommande ligneCommande = (LigneCommande) entite;
            orm.chercherNUpletsAvecPredicat("WHERE ID_PLAT = " + ligneCommande.getIdPlat(), PlatIngredients.class)
               .forEach(entite2 -> {
                    //Récupération de l'ingrédient.
                    PlatIngredients platIngredients = (PlatIngredients) entite2;
                    Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = " +
                                                                                        platIngredients.getIdIngredient(),
                                                                                        Ingredient.class);

                    //Calcul du nouveau stock.
                    double ancienStock = ingredient.getStock();
                    double quantiteNecessaire = ligneCommande.getNbOccurences() * platIngredients.getQuantite();
                    double nouveauStock = operation.apply(ancienStock, quantiteNecessaire);

                    //Sauvegarde.
                    //Modification du stock de l'ingrédient.
                    ingredient.setStock(nouveauStock);
                    orm.persisterNUplet(ingredient);
               });
        });
    }

    /**
     * Allouer le stock nécessaire pour toutes les lignes d'une commande.
     *
     * @param composition
     */
    private static void allouerStock(@NotNull List<LigneCommande> composition) {
        gererStock(composition, (ancienStock, quantiteNecessaire) -> ancienStock - quantiteNecessaire);
    }

    /**
     * Désallouer le stock nécessaire pour toutes les lignes d'une commande.
     *
     * @param composition
     */
    private static void desallouerStock(@NotNull List<Entite> composition) {
        gererStock(composition, (ancienStock, quantiteNecessaire) -> ancienStock + quantiteNecessaire);
    }

    /**
     * Calculer le coût total d'une commande.
     *
     * @param lignesCommande
     * @return
     */
    private static double calculerCoutTotal(@NotNull List<LigneCommande> lignesCommande) {
        return lignesCommande.stream()
                             .mapToDouble(ligneCommande -> {
                                //Récupération du plat associé à la ligne de commande.
                                Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = " + ligneCommande.getIdPlat(),
                                                                                   Plat.class);

                                //Calcul du prix de la ligne de commande.
                                return ((double) ligneCommande.getNbOccurences()) * plat.getPrix();
                             })
                            .reduce(Double::sum)
                            .getAsDouble();
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder une commande,
     * qu'elle soit déjà existante en base de données ou non.
     *
     * @param commande
     */
    private static void editerEtPersister(@NotNull Commande commande) {
        //Récupération des tables occupées du restaurant.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);

        //Questions et saisies.
        //Choix d'une table du restaurant.
        int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees,
                                                    entite -> ((Place)entite).toEtatServeurString())
                        .getId();
        //Composition des commandes.
        List<LigneCommande> composition = composer();

        //Sauvegarde.
        //Insertion ou mise à jour de la commande.
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(idPlace);
        commande.setEtat("en attente");
        commande.setCoutTotal(calculerCoutTotal(composition));
        orm.persisterNUplet(commande);
        //Insertion de la (nouvelle) composition.
        composition.forEach(ligneCommande -> {
            ligneCommande.setIdCommande(commande.getId());
            orm.persisterNUplet(ligneCommande);
        });
        //Allocation du stock pour la commande.
        allouerStock(composition);
    }

    /**
     * Ajouter une commande.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une commande :");

        //Récupération du nombre de plats disponibles du restaurant (en fonction des stocks).
        int nbPlatsDisponibles = orm.compterNUpletsAvecPredicat("WHERE " + Plat.PREDICAT_DISPONIBLE_EN_STOCK, Plat.class);

        String messageErreur = "Aucun plat disponible en stock : pour aucun plat les quantités d'ingrédients requises " +
                               "sont toutes présentes en stock !";
        //Si des plats sont disponibles en stock.
        if(!ui.afficherSiNombreNul(nbPlatsDisponibles, messageErreur)) {
            //Saisie et sauvegarde.
            Commande commande = new Commande();
            editerEtPersister(commande);

            //Message de résultat.
            ui.afficher("Commande ajoutée !\n" + commande);
        }
    }

    /**
     * Suuprimer une commande.
     *
     * On ne peut supprimer une commande que si toutes ses lignes
     * sont encore en attente.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une commande :");

        //Récupération des tables occupées dans le restaurant.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);

        //Si des tables sont occupées par des clients.
        String messageErreur = "Aucune table occupée trouvée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesOccupees, messageErreur)) {
            //Question et saisies.
            //Choix de la table.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees).getId();

            //Récupération des commandes existantes en attente de préparation.
            List<Entite> commandesEnAttente = orm.chercherNUpletsAvecPredicat("INNER JOIN LIGNE_COMMANDE AS LC " +
                                                                               "ON LC.ID_COMMANDE = FROM_TABLE.ID "+
                                                                               "WHERE LC.ETAT = 'en attente' " +
                                                                               "AND FROM_TABLE.ID_PLACE = " + idPlace,
                                                                                Commande.class);

            //Si des commandes sont en attente.
            messageErreur = "Aucune commande en attente de préparation trouvée pour cette table !";
            if (!ui.afficherSiListeNUpletsVide(commandesEnAttente, messageErreur)) {
                //Question et saisies.
                //Choix de la commande.
                Commande commande = (Commande) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, commandesEnAttente);
                int idCommande = commande.getId();

                //Récupération de la composition de la commande.
                List<Entite> composition = orm.chercherNUpletsAvecPredicat("WHERE ID_COMMANDE = " + idCommande, LigneCommande.class);

                //Sauvegarde.
                //Désallocation du stock pour cette commande.
                desallouerStock(composition);
                //Suppression de la composition de la commande.
                orm.supprimerNUpletsAvecPredicat("WHERE ID_COMMANDE = " + idCommande, LigneCommande.class);
                //Suppression de la commande.
                orm.supprimerNUplet(commande);

                //Message de résultat.
                ui.afficher("Commande supprimée !");
            }
        }
    }

    /**
     * Lister les plats prêts pas encore servis dans le restaurant,
     * pour les tables du serveur connecté.
     */
    public static void listeLignesPretes() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats prêts pour mes tables :");

        //Récupération des plats prêts non servis.
        String predicat = "INNER JOIN COMMANDE AS C " +
                          "ON C.ID = FROM_TABLE.ID_COMMANDE " +
                          "INNER JOIN PLACE AS P " +
                          "ON P.ID = C.ID_PLACE " +
                          "WHERE FROM_TABLE.ETAT = 'prêt' " +
                          "AND P.ID_COMPTE_SERVEUR = " +
                          getUtilisateurConnecte().getId();
        List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat(predicat, LigneCommande.class);

        //Si des plats prêts non servis ont été trouvés.
        String messageErreur = "Aucun plat prêt attendant d'être servi !";
        if(!ui.afficherSiListeNUpletsVide(lignesCommande, messageErreur)) {
            //Message de résultat.
            ui.listerNUplets(lignesCommande);
        }
    }

    /**
     * Lister tous les plats à préparer.
     */
    public static void listerLignesAPreparer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats à préparer des commandes :");

        //Récupération des plats à préparer.
        List<Entite> lignesCommandeAPreparer = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", LigneCommande.class);

        //Si des plats à préparer ont été trouvés.
        String messageErreur = "Aucun plat n'est à préparer dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(lignesCommandeAPreparer, messageErreur)) {
            //Listing.
            ui.listerNUplets(lignesCommandeAPreparer);
        }
    }

    /**
     * Valider la préparation d'un plat d'une commande.
     */
    public static void validerPreparation() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation de la préparation d'un plat d'une commande :");

        //Récupération des commandes en attente.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", Commande.class);

        //Si des commandes en attente ont été trouvés.
        String messageErreur = "Aucune commande en attente trouvée !";
        if (!ui.afficherSiListeNUpletsVide(commandes, messageErreur)) {
            //Question et saisies.
            //Choix de la commande.
            Commande commande = (Commande) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, commandes);
            int idCommande = commande.getId();

            //Récupération des plats en attente de la commande sélectionnée.
            List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente' " +
                                                                          "AND ID_COMMANDE = " + idCommande,
                                                                           LigneCommande.class);

            //Si des plats en attente ont été trouvés.
            messageErreur = "Aucun plat en attente trouvé pour la commande sélectionnée !";
            if (!ui.afficherSiListeNUpletsVide(lignesCommande, messageErreur)) {
                //Question et saisies.
                //Choix de la ligne de commande.
                LigneCommande ligneCommande = (LigneCommande) ui.poserQuestionListeNUplets("Sélectionner un ligne de commande :",
                                                                                           lignesCommande);

                //Sauvegarde.
                //La ligne de commande pas à l'état "prêt".
                ligneCommande.setEtat("prêt");
                orm.persisterNUplet(ligneCommande);

                //Message de résultat.
                ui.afficher("Plat prêt !\n" + ligneCommande);
            }
        }
    }

    /**
     * Lister les plats prêts pas encore servis dans le restaurant,
     * pour une des tables du serveur connecté.
     */
    public static void listerLignesPretesPlace() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats prêts pour une de mes tables :");

        //Récupération des tables occupées du serveur dans le restaurant.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' " +
                                                                      "AND ID_COMPTE_SERVEUR = " +
                                                                      getUtilisateurConnecte().getId(),
                                                                      Place.class);

        //Si des tables du serveur connecté sont occupées dans le restaurant.
        String messageErreur = "Aucune de vos tables n'est occupée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesOccupees, messageErreur)) {
            //Questions et saisies.
            //Choix de la table.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees).getId();

            //Récupération des plats prêts non servis.
            List<Entite> lignesCommandePretes = orm.chercherNUpletsAvecPredicat("INNER JOIN COMMANDE AS C " +
                                                                                "ON C.ID = FROM_TABLE.ID_COMMANDE " +
                                                                                "WHERE FROM_TABLE.ETAT = 'prêt' "  +
                                                                                "AND C.ID_PLACE = " + idPlace,
                                                                                 LigneCommande.class);

            //Si des plats prêts non servis pour la table sélectionnée ont été trouvés.
            messageErreur = "Aucun plat prêt attendant d'être servi pour cette table !";
            if(!ui.afficherSiListeNUpletsVide(lignesCommandePretes, messageErreur)) {
                //Listing.
                ui.listerNUplets(lignesCommandePretes);
            }
        }
    }

    /**
     * Valider le service d'un plat d'une commandz : c'est-à-dire
     * que le plat a été servi à la table.
     */
    public static void validerService() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation du service d'un plat d'une commande :");

        //Récupération des commandes en attente.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", Commande.class);

        //Si des commandes en attente ont été trouvés.
        String messageErreur = "Aucune commande en attente pas encore servie trouvée !";
        if (!ui.afficherSiListeNUpletsVide(commandes, messageErreur)) {
            //Question et saisies.
            //Choix de la commande.
            Commande commande = (Commande) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, commandes);
            int idCommande = commande.getId();

            //Récupération des plats prêts attendant d'être servi de la commande sélectionnée.
            List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'prêt' " +
                                                                          "AND ID_COMMANDE = " + idCommande,
                                                                           LigneCommande.class);

            //Si des plats en attente ont été trouvés.
            messageErreur = "Aucun plat prêt attendant d'être servi trouvé !";
            if (!ui.afficherSiListeNUpletsVide(lignesCommande, messageErreur)) {
                //Question et saisies.
                //Choix de la ligne de commande.
                LigneCommande ligneCommande = (LigneCommande) ui.poserQuestionListeNUplets("Sélectionner une ligne de commande :",
                                                                                            lignesCommande);

                //Sauvegarde.
                //La ligne de commande pas à l'état "servi".
                ligneCommande.setEtat("servi");
                orm.persisterNUplet(ligneCommande);
                //Récupération du nombre de plats encore en attente de la commande sélectionnée.
                int nbPlatsEncoreEnAttente = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'prêt' " +
                                                                            "AND ID_COMMANDE = " + idCommande,
                                                                             LigneCommande.class);
                //Si tous les plats des la commande ont été servis.
                if(nbPlatsEncoreEnAttente == 0) {
                    //La commande passe à l'état "servi".
                    commande.setEtat("servi");
                    orm.persisterNUplet(commande);
                    //Mise à jour du temps de préparation du jour.
                    StatControleur.mettreAjourTempsPreparation(commande);
                }

                //Message de résultat.
                ui.afficher("Plat servi !\n" + ligneCommande);
            }
        }
    }

    /**
     * Valider le paiement d'une commande.
     */
    public static void validerPaiement() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Valider le paiement d'une commande :");

        //Récupération des commandes servies.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);

        //Si pas de commande servie.
        String messageErreur = "Aucune commande servie trouvée !";
        if(!ui.afficherSiListeNUpletsVide(commandes, messageErreur)) {
            //Question et saisies.
            //Choix de la commande.
            Commande commande = (Commande) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, commandes);

            //Sauvegarde.
            //Passage de la commande à l'état "payé".
            commande.setEtat("payé");
            orm.persisterNUplet(commande);
            //Mise à jour du nombre de commandes du jour.
            StatControleur.incrementerNombreCommandesPayees();
            //Mise à jour du chiffre d'affaire du jour.
            StatControleur.mettreAjourChiffreAffaire(commande);

            //Message de résultat.
            ui.afficher("Commande payée !\n" + commande);
        }
    }
}