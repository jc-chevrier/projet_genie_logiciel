package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

public class PlaceControleur extends Controleur{
    public static void desallouerPourClient(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur( "Désallocation d'une table à un client :");

        //Afficher la liste des tables libres ou réservées.
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'occupé' ", Place.class);

        //Si pas de tables à désallouer pour le client.
        if(places.isEmpty()){
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
