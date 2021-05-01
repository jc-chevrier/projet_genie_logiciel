package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceControleur extends Controleur {
    /**
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

        //Si pas de tables déclarée dans le restuarant.
        if(places.isEmpty()) {
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
}
