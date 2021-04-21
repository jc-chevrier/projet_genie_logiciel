package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

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
    /**
     * Modifier une unité
     */
    public static void modifier(){
        //UI.
        UI ui = getUI();
        ORM orm = getORM();

        //Afficher la liste des unités
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        int idUnite = ui.poserQuestionListeNUplets(liste);

        //Si pas d'unité dans le catalogue
        if(liste.isEmpty()){
            ui.afficher("\n" + UI.DELIMITEUR + "\nUnité à modifier non trouver :");
        //Sinon modifer une unité
        } else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nModifier une unité :");
            Unite unite = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = " + idUnite, Unite.class);
            unite.setLibelle("testD");
            orm.persisterNUplet(unite);
            ui.afficher("Unité modifié !");
        }
        //Retour vers l'accueil.
        AccueilControleur.get();
    }
}
