package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Commande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.LigneCommande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        } else {
            //Si pas de disponibles dans le restaurant.
            if(platsDisponibles.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucun plat disponible en stock : pour aucun plat les quantités d'ingrédients " +
                                     "requises sont toutes présentes en stock !");
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
     * Lister les plats prêts dans le restaurant,
     * pour les tables du serveur connecté,
     */
    public static void listerToutesLignesPretes() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats prêts pour mes tables :");

        //Récupération des plats prêts non servis.
        List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("INNER JOIN COMMANDE AS C " +
                                                                               "ON C.ID = FROM_TABLE.ID_COMMANDE " +
                                                                               "INNER JOIN PLACE AS P " +
                                                                               "ON P.ID = C.ID_PLACE " +
                                                                               "WHERE FROM_TABLE.ETAT = 'prêt' " +
                                                                               "AND P.ID_COMPTE_SERVEUR = " +
                                                                                getUtilisateurConnecte().getId(),
                                                                                LigneCommande.class);

        //Si pas de plats prêts non servis.
        if(lignesCommande.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun plat prêt attendant d'être servi !");
        } else {
            //Message de résultat.
            ui.listerNUplets(lignesCommande);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /*
    * Lister tous les plats à préparer.
     */
    public static void listerLignesAPreparer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats à préparer des commandes :");

        //Récupération des lignes de commandes à préparer.
        List<Entite> lignesCommandeAPreparer = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", LigneCommande.class);

        //Si pas de plats en attente.
        if (lignesCommandeAPreparer.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun plat n'est à préparer dans le restaurant !");
        } else {
            //Listing.
            ui.listerNUplets(lignesCommandeAPreparer);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Valider la préparation d'un plat d'une commande.
     */
    public static void validerPreparation() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation de la préparation d'un plat d'une commande :");

        //Récupération des commandes en attente.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", Commande.class);

        //Si pas de commande en attente.
        if (commandes.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune commande en attente trouvée !");
        } else {
            //Question et saisies.
            int idCommande = ui.poserQuestionListeNUplets("Sélectionner une commande :", commandes);
            Commande commande = (Commande) filtrerListeNUpletsAvecId(commandes, idCommande);

            //Récupération des plats en attente de la commande sélectionnée
            List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente' " +
                                                                            "AND ID_COMMANDE = " + idCommande,
                                                                             LigneCommande.class);

            //Si pas de plat en attente.
            if (lignesCommande.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucun plat en attente trouvé pour la commande sélectionnée !");
            } else {
                //Question et saisies.
                int idLigneCommande = ui.poserQuestionListeNUplets("Sélectionner un ligne de commande :", lignesCommande);
                LigneCommande ligneCommande = (LigneCommande) filtrerListeNUpletsAvecId(lignesCommande, idLigneCommande);

                //Sauvegarde : modification de la ligne de commande.
                ligneCommande.setEtat("prêt");
                orm.persisterNUplet(ligneCommande);

                //Récupération du nombre de plats encore en attente de la commande sélectionnée.
                int nbPlatsEncoreEnAttente = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'en attente' " +
                                                                                     "AND ID_COMMANDE = " + idCommande,
                                                                                     LigneCommande.class);

                if(nbPlatsEncoreEnAttente == 0) {
                    //Sauvegarde : modification de la commande (si nécessaire).
                    commande.setEtat("servi");
                    orm.persisterNUplet(commande);

                    //Sauvegarde : mise à jour du temps de préparation du jour.
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    String aujourdhui = dateFormat.format(new Date());
                    StatGeneral statGeneral = (StatGeneral) orm.chercherNUpletAvecPredicat("WHERE TO_CHAR(DATE_JOUR, 'mm-dd-yyyy') = '" +
                                                                                                    aujourdhui + "'",
                                                                                                    StatGeneral.class);
                    int tempsPreparation = (int) ((new Date()).getTime() - commande.getDatetimeCreation().getTime()) / 1000 / 60;
                    if(statGeneral == null) {
                        statGeneral = new StatGeneral();
                        statGeneral.setDateJour(new Date());
                        statGeneral.setTempsPreparation(tempsPreparation);
                        statGeneral.setNbCommandesPreparees(1);
                    } else {
                        Integer tempsPreparationActuel = statGeneral.getTempsPreparation();
                        if(tempsPreparationActuel == null) {
                            statGeneral.setTempsPreparation(tempsPreparation);
                            statGeneral.setNbCommandesPreparees(1);
                        } else {
                            statGeneral.setTempsPreparation(tempsPreparationActuel + tempsPreparation);
                            statGeneral.setNbCommandesPreparees(statGeneral.getNbCommandesPreparees() + 1);
                        }
                    }
                    orm.persisterNUplet(statGeneral);
                }

                //Message de résultat.
                ui.afficher("Plat prêt !");
                ui.afficher(ligneCommande.toString());
            }
        }

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les plats prêts dans le restaurant,
     * pour une table.
     */
    public static void listerLignesPretesPlace() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des plats prêts pour une de mes tables :");

        //Récupération des tables occupées du serveur dans le restaurant.
        List<Entite>  placesOccupees =  orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' " +
                        "AND ID_COMPTE_SERVEUR = " +
                        getUtilisateurConnecte().getId(),
                Place.class);

        //Si pas de tables du serveur occupées dans le restaurant.
        if(placesOccupees.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune de vos tables n'est occupée dans le restaurant !");
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", placesOccupees);

            //Récupération des plats prêts non servis.
            List<Entite> lignesCommande = orm.chercherNUpletsAvecPredicat("INNER JOIN COMMANDE AS C " +
                                                                                    "ON C.ID = FROM_TABLE.ID_COMMANDE " +
                                                                                    "WHERE FROM_TABLE.ETAT = 'prêt' "  +
                                                                                    "AND C.ID_PLACE = " + idPlace,
                                                                                     LigneCommande.class);

            //Si pas de plats prêts non servis pour la table sélectionnée.
            if(lignesCommande.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucun plat prêt attendant d'être servi pour cette table !");
            } else {
                //Message de résultat.
                ui.listerNUplets(lignesCommande);
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
        } else {
            //Question et saisies.
            int idCommande = ui.poserQuestionListeNUplets("Sélectionner une commande :", commandes);
            Commande commande = (Commande) filtrerListeNUpletsAvecId(commandes, idCommande);

            //Sauvegarde : modification du plat.
            commande.setEtat("payé");
            orm.persisterNUplet(commande);

            //Sauvegarde : mise à jour du nombre de commandes du jour.
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Date aujourdhuiDate = new Date();
            String aujourdhui = dateFormat.format(new Date());
            StatGeneral statGeneral = (StatGeneral) orm.chercherNUpletAvecPredicat("WHERE TO_CHAR(DATE_JOUR, 'mm-dd-yyyy') = '" +
                                                                                    aujourdhui + "'",
                                                                                    StatGeneral.class);
            if(statGeneral == null) {
                statGeneral = new StatGeneral();
                statGeneral.setDateJour(new Date());
                statGeneral.setNbCommandesPayees(1);
            } else {
                Integer nbCommandesActuel = statGeneral.getNbCommandesPayees();
                if(nbCommandesActuel == null) {
                    statGeneral.setNbCommandesPayees(1);
                } else {
                    statGeneral.setNbCommandesPayees(nbCommandesActuel + 1);
                }
            }
            orm.persisterNUplet(statGeneral);
            //Mise à jour du chiffre d'affaire
            List<Entite> statChiffreAffaires = orm.chercherNUpletsAvecPredicat("INNER JOIN LIGNE_COMMANDE AS LC "+
                                                                                " ON LC.ID_PLAT = FROM_TABLE.ID_PLAT "+
                                                                                " WHERE TO_CHAR(DATE_JOUR, 'mm-dd-yyyy') = '" +
                                                                                aujourdhui + "' AND LC.ID_COMMANDE ="+
                                                                                commande.getId(),
                                                                                StatChiffreAffaire.class);
            //Liste vide : pas de stat pour la commande sélectionnée aujourdui.
            if(statChiffreAffaires.isEmpty()){
                 orm.chercherNUpletsAvecPredicat("WHERE ID_COMMANDE ="+commande.getId(),LigneCommande.class)
                    .forEach(entite -> {
                        LigneCommande ligneCommande = (LigneCommande) entite;
                        StatChiffreAffaire statChiffreAffaire = new StatChiffreAffaire();
                        statChiffreAffaire.setDateJour(aujourdhuiDate);
                        statChiffreAffaire.setIdPlat(ligneCommande.getIdPlat());
                        Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID ="+ligneCommande.getIdPlat(),Plat.class);
                        Double chiffreAffaire = plat.getPrix()*ligneCommande.getNbOccurences();
                        //Vérifier si le plat a été commandé pour le déjeuner ou le dinner
                        if(aujourdhuiDate.getHours()>11 && aujourdhuiDate.getHours()<14){
                            statChiffreAffaire.setChiffreAffaireDejeuner(chiffreAffaire);
                            statChiffreAffaire.setChiffreAffaire(chiffreAffaire);
                        }else{
                            statChiffreAffaire.setChiffreAffaireDiner(chiffreAffaire);
                            statChiffreAffaire.setChiffreAffaire(chiffreAffaire);

                        }
                        orm.persisterNUplet(statChiffreAffaire);
                    });
             //Sinon : Remettre à jour les stat
            }else{
                statChiffreAffaires.forEach(entite -> {
                    StatChiffreAffaire statChiffreAffaire = (StatChiffreAffaire) entite;
                    Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID ="+statChiffreAffaire.getIdPlat(),Plat.class);
                    LigneCommande ligneCommande = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID_PLAT = "+plat.getId()+
                                                                                                          " AND ID_COMMANDE = "+commande.getId(),
                                                                                                           LigneCommande.class);
                    Double chiffreAffaire = plat.getPrix()*ligneCommande.getNbOccurences();
                    if(aujourdhuiDate.getHours()>11 && aujourdhuiDate.getHours()<14){
                        if(statChiffreAffaire.getChiffreAffaireDejeuner() == null){
                            statChiffreAffaire.setChiffreAffaireDejeuner(chiffreAffaire);
                        }else{
                            statChiffreAffaire.setChiffreAffaireDejeuner(statChiffreAffaire.getChiffreAffaireDejeuner() +chiffreAffaire);
                        }
                        statChiffreAffaire.setChiffreAffaire(statChiffreAffaire.getChiffreAffaire() + chiffreAffaire);
                    }else{
                        if(statChiffreAffaire.getChiffreAffaireDiner() == null){
                            statChiffreAffaire.setChiffreAffaireDiner(chiffreAffaire);
                        }else{
                            statChiffreAffaire.setChiffreAffaireDiner(statChiffreAffaire.getChiffreAffaireDiner() + chiffreAffaire);
                        }
                        statChiffreAffaire.setChiffreAffaire(statChiffreAffaire.getChiffreAffaire() + chiffreAffaire);
                    }
                    orm.persisterNUplet(statChiffreAffaire);
                });
            }
            //Message de résultat.
            ui.afficher("Commande payée !");
            ui.afficher(commande.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}