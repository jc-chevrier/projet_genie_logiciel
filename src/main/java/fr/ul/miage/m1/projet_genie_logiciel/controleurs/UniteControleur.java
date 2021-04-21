package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

/**
 * Contrôleur pour l'unité.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class UniteControleur extends Controleur{
    /**
     * Ajouter une unité
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
        ui.afficher("\n" + UI.DELIMITEUR + "\nModifier une unité :");

        //Afficher la liste des unités
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        int idUnite = ui.poserQuestionListeNUplets(liste);

        //Modifer une unité
        Unite unite = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = " + idUnite, Unite.class);
        unite.setLibelle("test2");
        orm.persisterNUplet(unite);
        ui.afficher("Unité modifié !");

        //Retour vers l'accueil.
        AccueilControleur.get();
    }

    /**
     * Lister les unités
     */
    public static void lister(){
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        ui.afficher("\n" + UI.DELIMITEUR + "\nLister les unités :");

        //Afficher la liste des unités
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        int idUnite = ui.poserQuestionListeNUplets(liste);

        if(liste.isEmpty()){
            ui.afficher("\n" + UI.DELIMITEUR + "\nAucune liste d'unité trouvé dans le cataloque !");
        //Sinon
        } else {
            ui.afficher("\n" + UI.DELIMITEUR + "\nLister les unités :");
            Unite unite = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = " + idUnite, Unite.class);
            orm.persisterNUplet(unite);
            ui.afficher("Unité listé !");
        }
        //Retour vers l'accueil.
        AccueilControleur.get();
    }
}