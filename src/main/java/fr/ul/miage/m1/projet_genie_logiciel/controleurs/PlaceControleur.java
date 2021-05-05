package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

public class PlaceControleur extends Controleur{

    /**
     * Allouer une table à un client.
     */
    public static void allouerPourClient(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur( "Allouer une table à un client :");

        //Afficher la liste des tables libres ou réservées
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' OR ETAT = 'réservé' ", Place.class);

        //Si pas de tables à allouer pour le client
        if(places.isEmpty()){
            //Message d'erreur.
            ui.afficher("Aucune table n'est disponible ou réservée dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);
            place.setEtat("occupé");

            //Sauvegarde : allocation de table
            orm.persisterNUplet(place);

            //Message de résultat
            ui.afficher("Nouvelle allocation de la table réussie !");
            ui.afficher(places.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
