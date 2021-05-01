package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

public class PlaceControleur extends Controleur{
    /**
     * Lister les tables disponibles
     */
    public static void lister(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        ui.afficher("\n" + UI.DELIMITEUR + "\nLister toutes les tables disponibles :");

        //Afficher la liste des tables
        List<Entite>  places =  orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'libre' ", Place.class);

        if(places.isEmpty()){
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucune table n'est disponible !");
            //Sinon
        } else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nListe des tables disponibles :");
            ui.listerNUplets(places);
        }
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}