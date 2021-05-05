package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

public class CategorieControleur extends Controleur {
    /**
     * Lister les unités.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des catégories :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if(categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie déclarée !");
        } else {
            //Litsing.
            ui.listerNUplets(categories);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
