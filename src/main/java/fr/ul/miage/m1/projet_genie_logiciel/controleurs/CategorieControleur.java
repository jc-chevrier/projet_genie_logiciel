package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

/**
 * Controleur pour les catégories.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CategorieControleur extends Controleur{
    /**
     * Supprimer une catégorie.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une catégorie :");

        //Récupération des unités existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if(categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée dans le cataloque !");
            //Sinon.
        } else {
            //Questions et entrées.
            int idCategorie = ui.poserQuestionListeNUplets(categories);
            Categorie categorie = (Categorie) filterListeNUpletsAvecId(categories, idCategorie);
            //Si la catégorie contient des plats disponibles.
            if(categorie.estUtiliseParPlat()) {
                //Message d'erreur.
                ui.afficher("Cette catégorie est utilisée par des plats, elle ne peut pas être supprimée !");
                //Sinon.
            } else {
                //Sauvegarde : suppression de l'unité.
                orm.supprimerNUplet(categorie);

                //Message de résultat.
                ui.afficher("Catégorie supprimée !");
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}