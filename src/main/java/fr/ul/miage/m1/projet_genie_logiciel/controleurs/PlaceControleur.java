package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

public class PlaceControleur extends Controleur{

    /**
     * Lister les tables à préparer
     */
    public static void listerAPreparer(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        ui.afficher("\n" + UI.DELIMITEUR + "\nLister les tables à préparer :");

        //Afficher la liste des tables à préparer
        List<Entite> places =  orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale' ", Place.class);

        //Si pas de tables à préparer
        if(places.isEmpty()){
            //Message d'erreur.
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucune table à préparer !");
            //Sinon
        } else {
            //Litsing.
            ui.afficher("\n" + UI.DELIMITEUR + "\nListe des tables à préparer :");
            ui.listerNUplets(places);
        }
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
