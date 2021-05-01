package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        if(places.isEmpty()) {
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
}
