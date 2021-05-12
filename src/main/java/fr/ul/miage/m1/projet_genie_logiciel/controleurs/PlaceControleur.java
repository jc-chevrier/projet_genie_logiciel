package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

/**
 * Controleur pour les tables du restaurant.
 *
 * Les tables du restaurant sont appelées "ploces".
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlaceControleur extends Controleur {
    /**
     * Lister les tables du restaurant.
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
            ui.listerNUplets(places, (place) -> ((Place) place).toEtatServeurString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Ajouter une table au restaurant.
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
     * Suuprimer une table du restaurant.
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
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places);
            Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : suppression d'une table.
            orm.supprimerNUplet(place);

            //Message de résultat.
            ui.afficher("Table supprimée !");
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les tables disponibles
     */
    public static void listerDisponibles() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables disponibles :");

        //Récupération des tables libres / disponibles dans le restaurant.
        List<Entite>  places =  orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' ", Place.class);

        //Si pas de tables disponibles.
        if(places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table n'est disponible dans le restaurant !");
        //Sinon
        } else {
            //Listing.
            ui.listerNUplets(places, (place) -> ((Place) place).toEtatString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les tables à préparer
     */
    public static void listerAPreparer() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables à préparer :");

        //Récupération des tables à préparer dans le restaurant.
        List<Entite> places =  orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale' ", Place.class);

        //Si pas de tables à préparer.
        if(places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table à préparer !");
        //Sinon
        } else {
            //Litsing.
            ui.listerNUplets(places, (place) -> ((Place) place).toEtatString());
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
        ui.afficherAvecDelimiteurEtUtilisateur("Validation de la préparation d'une table :");

        //Récupération des tables sales qui étaient à préparer.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale' ", Place.class);

        //Si pas d'unités dans le catalogue.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table sale trouvée dans le cataloque !");
        } else {
            //Questions et entrées.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places, (place) -> ((Place) place).toEtatString());
            Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : modification de la table.
            place.setEtat("libre");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Table préparée !");
            ui.afficher(place.toEtatString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les tables pour le serveur.
     */
    public static void listerAlloueesPourServeur() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing de mes tables :");

        //Récupération des tables du serveur connecté.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + getUtilisateurConnecte().getId(), Place.class);

        //Si pas de tables à lister pour le serveur
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table allouée pour moi dans le restaurant !");
        //Sinon
        } else {
            //Listing.
            ui.listerNUplets(places, (place) -> ((Place) place).toEtatServeurString());
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

        //Récupération des serveurs actifs dans le restaurant.
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);
        //Récuération des tables du restautant qui ne sont pas allouées à des serveurs.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR IS NULL", Place.class);

        //Si pas de serveurs actifs dans le restaurant.
        if (comptesServeurs.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun serveur actif déclaré dans le restaurant !");
        //Sinon
        } else {
            //Si pas de tables à allouer.
            if (places.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Aucune table non allouée à des serveurs dans le restaurant !");
            //Sinon
            } else {
                //Questions et saisies.
                int idCompteServeur = ui.poserQuestionListeNUplets("Sélectionner un serveur :", comptesServeurs);
                int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places, (place) -> ((Place) place).toEtatServeurString());

                //Sauvegarde : modification de la table.
                Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);
                place.setIdCompteServeur(idCompteServeur);
                orm.persisterNUplet(place);

                //Message de résultat.
                ui.afficher("Nouvelle allocation de la table réussie !");
                ui.afficher(place.toEtatServeurString());
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

        //Récupération des serveurs actifs dans le restaurant.
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);

        //Si pas de tables à désallouer pour le serveur.
        if (comptesServeurs.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun serveur actif trouvé dans le restaurant !");
        //Sinon
        } else {
            //Questions et saisies.
            int idCompteServeur = ui.poserQuestionListeNUplets("Sélectionner un serveur :", comptesServeurs);
            List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + idCompteServeur, Place.class);

            //Si pas de tables allouées au serveur.
            if (places.isEmpty()) {
                //Message d'erreur.
                ui.afficher("Le serveur n'a aucune table allouée !");
            //Sinon.
            } else {
                //Questions et saisies.
                int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places, (place) -> ((Place) place).toEtatServeurString());

                //Sauvegarde : modification de la table.
                Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);
                place.setIdCompteServeur(null);
                orm.persisterNUplet(place);

                //Message de résultat.
                ui.afficher("Désallocation de la table réussie !");
                ui.afficher(place.toEtatServeurString());
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
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' OR ETAT = 'réservé'", Place.class);

        //Si pas de tables à allouer pour le client.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table n'est disponible ou réservée dans le restaurant !");
        //Sinon.
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places);
            Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : modification de la table.
            if(place.getDatetimeReservation() != null) {
                place.setDatetimeReservation(null);
                place.setNomReservation(null);
                place.setPrenomReservation(null);
            }
            place.setEtat("occupé");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Nouvelle allocation de la table réussie !");
            ui.afficher(place.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Désallouer une table à un client.
     */
    public static void desallouerPourClient() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Désallocation d'une table à un client :");

        //Afficher la liste des tables libres ou réservées.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);

        //Si pas de tables à désallouer pour le client.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table à désallouer dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets("Sélectionner une table :", places, (place) -> ((Place) place).toEtatString());
            Place place = (Place) filtrerListeNUpletsAvecId(places, idPlace);

            //Sauvegarde : modification de la table.
            place.setEtat("sale");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Désallocation de la table réussie !");
            ui.afficher(place.toEtatString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Lister les tables réservées
     */
    public static void listerReserver() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des tables réservées :");

        //Récupération des tables réservées dans le restaurant.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'réservé' ", Place.class);

        //Si pas de tables réservées.
        if (places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table n'est réservées dans le restaurant !");
            //Sinon
        } else {
            //Listing.
            ui.listerNUplets(places);
        }
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}