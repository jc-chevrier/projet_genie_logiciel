package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

public class PlaceControleur extends Controleur{

    /**
     * Désallouer une table à un serveur.
     */
    public static void desallouerPourServeur(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur( "Désallocation d'une table à un serveur :");

        //Afficher la liste des comptes serveurs
        List<Entite> comptesServeurs = orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4 AND ACTIF = 1", Compte.class);

        //Si pas de tables à désallouer pour le serveur
        if(comptesServeurs.isEmpty()){
            //Message d'erreur.
            ui.afficher("Aucun serveur actif trouvé dans le restaurant !");
            //Sinon
        } else {
            //Questions et saisies.
            ui.afficher("Saisir un serveur :");
            Integer idCompteServeur = ui.poserQuestionListeNUplets(comptesServeurs);
            List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR = " + idCompteServeur, Place.class);

            if(places.isEmpty()){
                ui.afficher("Le serveur n'a aucune table allouée !");
            }else{
                ui.afficher("Saisir une table :");
                Integer idPlace = ui.poserQuestionListeNUplets(places);

                //Sauvegarde : désallocation de table
                Place place = (Place) filterListeNUpletsAvecId(places,idPlace);
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
}

