package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import java.util.List;

/**
 * Contrôleur pour l'accueil.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class AccueilControleur extends Controleur {
    /**
     * Obtenir l'accueil de l'interface.
     */
    public static void get() {
        //UI.
        UI ui = getUI();

        //Connexion.
        if(!utilisateurConnecte()) {
            AuthControleur.seConnecter();
        }

        //Question fonctionnalités.
        int idRole = getUtilisateurConnecte().getIdRole();
        List<String> fonctionnalitesLibelles = Fonctionnalite.getRoleFonctionnalitesLibelles(idRole);
        int indexFonctionnalite = ui.poserQuestionListeOptions(fonctionnalitesLibelles);

        //Exécution de la fonctionnalité sélectionnée.
        Fonctionnalite fonctionnalite = Fonctionnalite.getFonctionnalite(idRole, indexFonctionnalite);
        fonctionnalite.executer();
    }
}