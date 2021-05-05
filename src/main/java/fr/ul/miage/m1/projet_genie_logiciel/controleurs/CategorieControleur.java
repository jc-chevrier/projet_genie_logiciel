package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

public class CategorieControleur extends Controleur {
    /**
     * Modifier une catégorie.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'une catégorie :");

        //Récupération des catégories existantes.
        List<Entite> categories = orm.chercherTousLesNUplets(Categorie.class);

        //Si pas de catégories trouvées.
        if (categories.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune catégorie trouvée !");
        //Sinon.
        } else {
            //Questions et entrées.
            int idCategorie = ui.poserQuestionListeNUplets(categories);
            String libelle = ui.poserQuestion("Saisir un nouveau libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);
            Categorie categorie = (Categorie) filterListeNUpletsAvecId(categories, idCategorie);

            //Sauvegarde : modification de la catégorie.
            categorie.setLibelle(libelle);
            orm.persisterNUplet(categorie);

            //Message de résultat.
            ui.afficher("Catégorie modifiée !");
            ui.afficher(categorie.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
