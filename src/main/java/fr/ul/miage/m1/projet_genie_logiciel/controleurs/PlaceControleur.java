package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

public class PlaceControleur extends Controleur {
    /**
     * Lister les plats.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables du restaurant :");

        //Récupération des tables existantes dans le restaurants.
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si pas de tables déclarées dans le restaurant.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table déclarée dans le restaurant !");
            //Sinon.
        } else {
            //Listing.
            ui.listerNUplets(places);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Ajouter une table.
     */
    public static void ajouter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une table :");

        //Sauvegarde : insertion d'une table.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //Message de résultat.
        ui.afficher("Table ajoutée !");
        ui.afficher("La table ajoutée a ce numéro : #" + place.getId() + ".");
        ui.afficher(place.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /*
     * Suuprimer un table.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une table :");

        //Récupération des tables existantes dans le restaurant.
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si pas de tables déclarées dans le restaurant.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table déclarée dans le restaurant !");
            //Sinon.
        } else {
            //Question et saisies.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : suppression d'une table.
            orm.supprimerNUplet(place);

            //Message de résultat.
            ui.afficher("Table supprimée !");
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Valider la préparation d'une table.
     */
    public static void validerPreparation() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation la préparation d'une table :");

        //Récupération des tables sales.
        ui.afficher("Nous affichons uniquement les tables sales.");
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale' ", Place.class);

        //Si pas d'unités dans le catalogue.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table sale trouvée dans le cataloque !");
        } else {
            //Questions et entrées.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);
            ;

            //Sauvegarde : préparation de la table.
            place.setEtat("libre");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Table préparée !");
            ui.afficher(place.toString());

        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les tables pour le serveur
     */
    public static void listerAlloueesPourServeur() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing de mes tables :");

        //Afficher la liste des tables pour le serveur
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + getUtilisateurConnecte().getId(), Place.class);
        //Si pas de tables à lister pour le serveur
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table allouée pour moi dans le restaurant !");
            //Sinon
        } else {
            //Listing.
            ui.listerNUplets(places);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Allouer une table à un serveur.
     */
    public static void allouerPourServeur() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Allouer une table à un serveur :");

        //Afficher la liste des tables et la liste des comptes serveurs
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si pas de tables à allouer pour le serveur
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table déclarée dans le restaurant !");
            //Sinon
        } else {
            if (comptesServeurs.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucun serveur actif déclaré dans le restaurant !");
                //Sinon
            } else {
                //Questions et saisies.
                ui.afficher("Saisir une table :");
                Integer idPlace = ui.poserQuestionListeNUplets(places);
                ui.afficher("Saisir un serveur :");
                Integer idCompteServeur = ui.poserQuestionListeNUplets(comptesServeurs);

                //Sauvegarde : allocation de table
                Place place = (Place) filterListeNUpletsAvecId(places, idPlace);
                place.setIdCompteServeur(idCompteServeur);
                orm.persisterNUplet(place);

                //Message de résultat
                ui.afficher("Nouvelle allocation de la table réussie !");
                ui.afficher(place.toString());
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Désallouer une table à un serveur.
     */
    public static void desallouerPourServeur() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Désallocation d'une table à un serveur :");

        //Afficher la liste des comptes serveurs
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);

        //Si pas de tables à désallouer pour le serveur
        if (comptesServeurs.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun serveur actif trouvé dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            ui.afficher("Saisir un serveur :");
            Integer idCompteServeur = ui.poserQuestionListeNUplets(comptesServeurs);
            List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + idCompteServeur, Place.class);

            if (places.isEmpty()) {
                ui.afficher("Le serveur n'a aucune table allouée !");
            } else {
                ui.afficher("Saisir une table :");
                Integer idPlace = ui.poserQuestionListeNUplets(places);

                //Sauvegarde : désallocation de table
                Place place = (Place) filterListeNUpletsAvecId(places, idPlace);
                place.setIdCompteServeur(null);
                orm.persisterNUplet(place);

                //Message de résultat
                ui.afficher("Désallocation de la table réussie !");
                ui.afficher(place.toString());
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Allouer une table à un client.
     */
    public static void allouerPourClient() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Allouer une table à un client :");

        //Afficher la liste des tables libres ou réservées
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' OR ETAT = 'réservé' ", Place.class);

        //Si pas de tables à allouer pour le client
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table n'est disponible ou réservée dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : allocation de table
            place.setEtat("occupé");
            orm.persisterNUplet(place);

            //Message de résultat
            ui.afficher("Nouvelle allocation de la table réussie !");
            ui.afficher(places.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    public static void desallouerPourClient() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Désallocation d'une table à un client :");

        //Afficher la liste des tables libres ou réservées.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' ", Place.class);

        //Si pas de tables à désallouer pour le client.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table à désallouer dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : déallocation de table
            place.setEtat("sale");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Désallocation de la table réussie !");
            ui.afficher(place.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}