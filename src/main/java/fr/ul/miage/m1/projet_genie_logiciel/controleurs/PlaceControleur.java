package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

public class PlaceControleur extends Controleur {
    /**
     * Ajouter une table.
     */
    public static void ajouter() {
        //UI.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une table :");

        //Sauvegarde : insertion d'une table.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //Message de résultat.
        ui.afficher("Table ajoutée !");
        ui.afficher("La table ajoutée a ce numéro : #" + place.getId() + ".");
        ui.afficher(place.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
