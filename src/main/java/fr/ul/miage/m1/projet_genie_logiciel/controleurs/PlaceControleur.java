package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

public class PlaceControleur extends Controleur{

    /**
     * Lister les tables pour le serveur
     */
    public static void listerAlloueesPourServeur(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur( "Listing de mes tables :");

        //Afficher la liste des tables pour le serveur
        List<Entite> places =  orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = "+ getUtilisateurConnecte()
                                                                                                    .getId(), Place.class);
        //Si pas de tables à lister pour le serveur
        if(places.isEmpty()){
            //Message d'erreur.
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucune table allouée pour moi dans le restaurant !");
            //Sinon
        } else {
            //Listing.
            ui.listerNUplets(places);
        }
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}