package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

public class PlaceControleur extends Controleur{

    /**
     * Allouer une table à un serveur.
     */
    public static void allouerPourServeur(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur( "Allouer une table à un serveur :");

        //Afficher la liste des tables et la liste des comptes serveurs
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);
        List<Entite> places = orm.chercherTousLesNUplets(Place.class);

        //Si pas de tables à allouer pour le serveur
        if(places.isEmpty()){
            //Message d'erreur.
            ui.afficher("Aucune table déclarée dans le restaurant !");
            //Sinon
        } else {
            if(comptesServeurs.isEmpty()){
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
               Place place = (Place) filterListeNUpletsAvecId(places,idPlace);
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
}
