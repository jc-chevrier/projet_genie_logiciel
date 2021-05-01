package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Controleur pour les catégories.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CategorieControleur extends Controleur {
    /**
     * Ajouter une catégorie.
     */
    public static void ajouter() {
        //UI.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une catégorie :");

        //Questions et entrées.
        String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde : insertion d'une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle(libelle);
        orm.persisterNUplet(categorie);

        //Message de résultat.
        ui.afficher("Categorie ajoutée !");
        ui.afficher(categorie.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}