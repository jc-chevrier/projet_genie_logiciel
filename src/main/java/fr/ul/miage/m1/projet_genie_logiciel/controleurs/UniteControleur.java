package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

public class UniteControleur extends Controleur{
    /**
     * Obtenir l'accueil de l'interface.
     */
    public static void ajouterUnite() {
        //UI.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et entrées.
        ui.afficher("\n" + UI.DELIMITEUR + "\nAjouter une unité :");
        String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES, false);

        //Insertion d'une unité.
        Unite unite = new Unite();
        unite.setLibelle(libelle);
        orm.persisterNUplet(unite);
        ui.afficher("Unité ajoutée !");

        //Retour vers l'accueil.
        AccueilControleur.get();
    }
}
