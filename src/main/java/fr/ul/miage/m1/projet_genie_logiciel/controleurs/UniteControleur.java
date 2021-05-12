package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

/**
 * Controleur pour les unités.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class UniteControleur extends Controleur{
    /**
     * Lister les unités.
     */
    public static void lister() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des unités :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si pas d'unités dans le catalogue.
        if(unites.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune unité trouvée dans le cataloque !");
        //Sinon.
        } else {
            //Litsing.
            ui.listerNUplets(unites);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Ajouter une unité.
     */
    public static void ajouter() {
        //UI.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une unité :");

        //Questions et entrées.
        String libelle = ui.poserQuestion("Saisir un libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde : insertion d'une unité.
        Unite unite = new Unite();
        unite.setLibelle(libelle);
        orm.persisterNUplet(unite);

        //Message de résultat.
        ui.afficher("Unité ajoutée !");
        ui.afficher(unite.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Modifier une unité.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'une unité :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si pas d'unités trouvées.
        if(unites.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune unité trouvée dans le cataloque !");
        //Sinon.
        } else {
            //Questions et saisies.
            int idUnite = ui.poserQuestionListeNUplets("Sélectionner une unité :", unites);
            String libelle = ui.poserQuestion("Saisir un nouveau libellé : ", UI.REGEX_CHAINE_DE_CARACTERES);
            Unite unite = (Unite) filtrerListeNUpletsAvecId(unites, idUnite);;

            //Sauvegarde : suppression de l'unité.
            unite.setLibelle(libelle);
            orm.persisterNUplet(unite);

            //Message de résultat.
            ui.afficher("Unité modifiée !");
            ui.afficher(unite.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Supprimer une unité.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une unité :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si pas d'unités trouvées.
        if(unites.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune unité trouvée dans le cataloque !");
        //Sinon.
        } else {
            //Questions et saisies.
            int idUnite = ui.poserQuestionListeNUplets("Sélectionner une unité :", unites);
            Unite unite = (Unite) filtrerListeNUpletsAvecId(unites, idUnite);
            //Si l'unité a été utilisé par des ingrédinents.
            if(unite.estUtiliseParIngredient()) {
                //Message d'erreur.
                ui.afficher("Cette unité est utilisée par des ingrédients, elle ne peut pas être supprimée !");
            //Sinon.
            } else {
                //Sauvegarde : suppression de l'unité.
                orm.supprimerNUplet(unite);

                //Message de résultat.
                ui.afficher("Unité supprimée !");
            }
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}