package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

/**
 * Contrôleur pour l'accueil.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class AccueilControlleur extends Controleur {
    /**
     * Obtenir l'accueil de l'interface.
     */
    public static void getAccueil() {
        //UI.
        UI ui = getUI();

        //Connexion.
        AuthControleur.seConnecter();

        //Question fonctionnalités.
        int idRoleUtilisateurCourant = getUtilisateurCourant().getIdRole();
        List<String> fonctionnalitesLibelles = Fonctionnalite.getRoleFonctionnalitesLibelles(idRoleUtilisateurCourant);
        int indexFonctionnaliteSelectionnee = ui.poserQuestionListeOptions(fonctionnalitesLibelles);

        //Exécution de la focntionnalité.
        Fonctionnalite fonctionnalite = Fonctionnalite.getFonctionnalite(idRoleUtilisateurCourant, indexFonctionnaliteSelectionnee);
        fonctionnalite.executer();
    }
}
