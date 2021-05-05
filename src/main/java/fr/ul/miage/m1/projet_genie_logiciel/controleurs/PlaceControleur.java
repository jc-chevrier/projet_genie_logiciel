package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

/**
 * Controleur pour les places.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlaceControleur extends Controleur{
    /**
     * Valider la préparation d'une table.
     */
    public static void validerPreparation() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Validation la préparation d'une table :");

        //Récupération des tables sales.
        System.out.print("Nous affichons uniquement les tables sales.\n");
        List<Entite> places = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'sale' ",Place.class);

        //Si pas d'unités dans le catalogue.
        if(places.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune table sale trouvée dans le cataloque !");
        }else{
            //Questions et entrées.
            int idPlace = ui.poserQuestionListeNUplets(places);
            Place place = (Place) filterListeNUpletsAvecId(places, idPlace);;

            //Sauvegarde : préparation de la table.
            place.setEtat("libre");
            orm.persisterNUplet(place);

            //Message de résultat.
            ui.afficher("Table préparée !");
            ui.afficher(place.toString());

        }
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
