package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * Contrôleur pour la gestion des tables du restaurant.
 *
 * Les tables du restaurant sont appelées "ploces".
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlaceControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUNE_TROUVEE = "Aucun table déclarée pour le restaurant !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner une table :";

    //Formateurs en chaine de caractères courants.
    private final static Function<Entite, String> FORMATEUR_ETAT = entite -> ((Place) entite).toEtatString();
    private final static Function<Entite, String> FORMATEUR_ETAT_SERVEUR = entite -> ((Place) entite).toEtatServeurString();

    /**
     * Lister toutes les tables du restaurant.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables du restaurant :");

        //Récupération des tables existantes dans le restaurant.
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si des tables déclarées dans le restaurant ont été trouvées.
        if (!ui.afficherSiListeNUpletsVide(places, MESSAGE_AUCUNE_TROUVEE)) {
            //Listing.
            ui.listerNUplets(places, FORMATEUR_ETAT_SERVEUR);
        }
    }

    /**
     * Ajouter une table au restaurant.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une table :");

        //Sauvegarde : insertion d'une table.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //Message de résultat.
        ui.afficher("Table ajoutée !\nLa table ajoutée a ce numéro : #" + place.getId() + ".\n" + place);
    }

    /*
     * Suuprimer une table du restaurant.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une table :");

        //Récupération des tables existantes dans le restaurant.
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si des tables déclarées dans le restaurant ont été trouvées.
        if (!ui.afficherSiListeNUpletsVide(places, MESSAGE_AUCUNE_TROUVEE)) {
            //Question et saisies.
            //Choix de la table.
            Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, places);

            //Si la table n'est pas utilisée par des commandes et n'est pas allouée à un serveur.
            String messageErreur = "Cette table est utilisée par des commandes et / ou est alloué à un serveur, " +
                                   "\nelle ne peut pas être supprimée !";
            if(!ui.afficherSiPredicatVrai(place.estUtiliseeParCommandeOuServeur(), messageErreur)) {
                //Sauvegarde.
                //Suppression de la table.
                orm.supprimerNUplet(place);

                //Message de résultat.
                ui.afficher("Table supprimée !");
            }
        }
    }

    /**
     * Lister les tables disponibles.
     */
    public static void listerDisponibles() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables disponibles :");

        //Récupération des tables disponibles / libres dans le restaurant.
        List<Entite> placesDisponibles = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);

        //Si des tables sont disponibles / libres dans le restaurant.
        String messageErreur = "Aucune table n'est disponible dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(placesDisponibles, messageErreur)) {
            //Listing.
            ui.listerNUplets(placesDisponibles, FORMATEUR_ETAT);
        }
    }

    /**
     * Lister les tables à préparer.
     */
    public static void listerAPreparer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables à préparer :");

        //Récupération des tables à préparer / sales dans le restaurant.
        List<Entite> placesAPreparer = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);

        //Si des tables sont à préparer / sales dans le restaurant.
        String messageErreur = "Aucune table à préparer dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(placesAPreparer, messageErreur)) {
            //Listing.
            ui.listerNUplets(placesAPreparer, FORMATEUR_ETAT);
        }
    }

    /**
     * Valider la préparation d'une table.
     */
    public static void validerPreparation() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation de la préparation d'une table :");

        //Récupération des tables à préparer / sales dans le restaurant.
        List<Entite> placesAPreparer = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);

        //Si des tables sont à préparer / sales dans le restaurant.
        String messageErreur = "Aucune table à préparer dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(placesAPreparer, messageErreur)) {
            //Questions et entrées.
            //Choix de la table.
            Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesAPreparer, FORMATEUR_ETAT);

            //Sauvegarde : modification de la table.
            //Une table préparée passe à l'état libre.
            place.setEtat("libre");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Table préparée !\n" + place);
        }
    }

    /**
     * Lister les tables pour le serveur.
     */
    public static void listerAlloueesPourServeur() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing de mes tables :");

        //Récupération des tables du serveur connecté.
        List<Entite> mesPlaces = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " +
                                                                 getUtilisateurConnecte().getId(),
                                                                  Place.class);

        //Si des tables sont alllouées au serveur connecté.
        String messageErreur = "Aucune table allouée pour moi dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(mesPlaces, messageErreur)) {
            //Listing.
            ui.listerNUplets(mesPlaces, FORMATEUR_ETAT_SERVEUR);
        }
    }

    /**
     * Allouer une table à un serveur.
     */
    public static void allouerPourServeur() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Allouer une table à un serveur :");

        //Récupération des serveurs actifs dans le restaurant.
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);
        //Récuération des tables du restautant qui ne sont pas allouées à des serveurs.
        List<Entite> placesSansServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR IS NULL", Place.class);

        //Si des serveurs actifs dans le restaurant ont été trouvés.
        String messageErreur = "Aucun serveur actif déclaré dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(comptesServeurs, messageErreur)) {
            //Si des tables sans serveur associé ont été trouvées.
            messageErreur = "Aucune table non allouée à des serveurs dans le restaurant !";
            if (!ui.afficherSiListeNUpletsVide(placesSansServeurs, messageErreur)) {
                //Questions et saisies.
                //Choix du serveur.
                int idCompteServeur = ui.poserQuestionListeNUplets("Sélectionner un serveur :", comptesServeurs).getId();
                //Choix de la table.
                Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesSansServeurs, FORMATEUR_ETAT_SERVEUR);

                //Sauvegarde : modification de la table.
                place.setIdCompteServeur(idCompteServeur);
                orm.persisterNUplet(place);

                //Message de résultat.
                ui.afficher("Nouvelle allocation de la table réussie !\n" + place.toEtatServeurString());
            }
        }
    }

    /**
     * Désallouer une table à un serveur.
     */
    public static void desallouerPourServeur() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Désallocation d'une table à un serveur :");

        //Récupération des serveurs actifs dans le restaurant.
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);

        //Si des serveurs actifs dans le restaurant ont été trouvés.
        String messageErreur = "Aucun serveur actif déclaré dans le restaurant !";
        if (!ui.afficherSiListeNUpletsVide(comptesServeurs, messageErreur)) {
            //Questions et saisies.
            //Choix du serveur.
            int idCompteServeur = ui.poserQuestionListeNUplets("Sélectionner un serveur :", comptesServeurs).getId();
            //Choix de la table.
            List<Entite> placesServeur = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + idCompteServeur, Place.class);

            //Si des tables allouées au serveur sélectionné ont été trouvées.
            messageErreur = "Le serveur n'a aucune table allouée !";
            if (!ui.afficherSiListeNUpletsVide(placesServeur, messageErreur)) {
                //Questions et saisies.
                Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesServeur, FORMATEUR_ETAT_SERVEUR);

                //Sauvegarde : modification de la table.
                place.setIdCompteServeur(null);
                orm.persisterNUplet(place);

                //Message de résultat.
                ui.afficher("Désallocation de la table réussie !\n" + place.toEtatServeurString());
            }
        }
    }

    /**
     * Allouer une table à un client.
     */
    public static void allouerPourClient() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Allouer une table à un client :");

        //Afficher la liste des tables disponibles ou réservées.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' OR ETAT = 'réservé'", Place.class);

        //Si des tables disponibles ou réservées ont été trouvées.
        String messageErreur = "Aucune table n'est disponible ou réservée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(places, messageErreur)) {
            //Questions et saisies.
            //Choix de la table.
            Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, places);

            //Sauvegarde.
            //La réservation n'a plus lieu d'être retenue.
            if(place.getDatetimeReservation() != null) {
                place.setDatetimeReservation(null);
                place.setNomReservation(null);
                place.setPrenomReservation(null);
            }
            //Passage de la table à l'état "occupé".
            place.setEtat("occupé");
            orm.persisterNUplet(place);
            //Mise à jour du nombre de clients du jour.
            StatControleur.incrementerNombreClients();

            //Message de résultat.
            ui.afficher("Nouvelle allocation de la table réussie !\n" + place);
        }
    }

    /**
     * Désallouer une table à un client.
     */
    public static void desallouerPourClient() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Désallocation d'une table à un client :");

        //Afficher la liste des tables libres ou réservées.
        List<Entite> placesOccupees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);

        //Si des tables occupées ont été trouvées.
        String messageErreur = "Aucune table occupée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesOccupees, messageErreur)) {
            //Questions et saisies.
            //Choix de la table.
            Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesOccupees, FORMATEUR_ETAT);

            //Sauvegarde : modification de la table.
            //Lorsqu'un client quitte la table, elle passe à l'état sale.
            place.setEtat("sale");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Désallocation de la table réussie !\n" + place);
        }
    }

    /**
     * Lister les tables réservées.
     */
    public static void listerReservees() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables réservées :");

        //Récupération des tables réservées dans le restaurant.
        List<Entite> placesReservees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'réservé'", Place.class);

        //Si des tables réservées dans le restaurant ont été trouvées.
        String messageErreur = "Aucune table réservée trouvée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesReservees, messageErreur)) {
            //Listing.
            ui.listerNUplets(placesReservees);
        }
    }

    /**
     * Réserver une table.
     */
    public static void reserver() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Réservation d'une table :");

        //Récupération de la liste des tables libres.
        List<Entite> placesLibres = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);

        //Si des tables disponibles dans le restaurant ont été trouvées.
        String messageErreur = "Aucune table n'est disponible dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesLibres, messageErreur)) {
            //Questions et saisies.
            //Choix de la table.
            Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesLibres);
            //Saisie des informations pour la réservation d'une table.
            String nom = ui.poserQuestion("Saisir le nom pour la réservation :", UI.REGEX_CHAINE_DE_CARACTERES);
            String prenom = ui.poserQuestion("Saisir le prénom pour la réservation :", UI.REGEX_CHAINE_DE_CARACTERES);

            //Sauvegarde : modification de la table.
            place.setNomReservation(nom);
            place.setPrenomReservation(prenom);
            place.setDatetimeReservation(new Date());
            place.setEtat("réservé");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Nouvelle réservation de la table réussie !\n" + place);
        }
    }

    /**
     * Annuler la réservation d'une table.
     */
    public static void annulerReservation() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Annulation d'une réservation dans le restaurant :");

        //Récupération de la liste des tables réservées.
        List<Entite> placesReservees = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'réservé'", Place.class);

        //Si des tables réservées dans le restaurant ont été trouvées.
        String messageErreur = "Aucune table réservée trouvée dans le restaurant !";
        if(!ui.afficherSiListeNUpletsVide(placesReservees, messageErreur)) {
            //Questions et saisies.
            //Saisie des informations pour la réservation d'une table.
            String nom = ui.poserQuestion("Saisir le nom pour la réservation :", UI.REGEX_CHAINE_DE_CARACTERES).toLowerCase();
            String prenom = ui.poserQuestion("Saisir le prénom pour la réservation :", UI.REGEX_CHAINE_DE_CARACTERES).toLowerCase();
            placesReservees = orm.chercherNUpletsAvecPredicat("WHERE LOWER(NOM_RESERVATION) = '" + nom + "'"  +
                                                              "AND LOWER(PRENOM_RESERVATION) = '" + prenom  + "'",
                                                               Place.class);
            //Si des tables réservées ont été trouvées pour pour ces nom et prénom.
            messageErreur = "Aucune table réservée trouvée pour ces nom et prénom !";
            if(!ui.afficherSiListeNUpletsVide(placesReservees, messageErreur)) {
                //Questions et saisies.
                //Choix de la table.
                Place place = (Place) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, placesReservees);

                //Sauvegarde
                //Modification de la table.
                place.setNomReservation(null);
                place.setPrenomReservation(null);
                place.setDatetimeReservation(null);
                place.setEtat("libre");
                orm.persisterNUplet(place);

                //Message de résultat.
                ui.afficher("Annulation de la réservation réussie !\n" + place);
            }
        }
    }
}