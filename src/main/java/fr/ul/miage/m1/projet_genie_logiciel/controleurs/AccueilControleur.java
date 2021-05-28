package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

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
    public static void consulter() {
        //Connexion.
        if(!utilisateurConnecte()) {
            AuthControleur.seConnecter();
        }

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Accueil :");

        //Question et saisie fonctionnalités.
        int idRole = getUtilisateurConnecte().getIdRole();
        //Choix du menu.
        List<String> menusFonctionnaliteLibelles = MenuFonctionnalite.getRoleMenusFonctionnaliteLibelles(idRole);
        int indexMenuFonctionnalite = ui.poserQuestionListeOptions("Sélectionner un menu :", menusFonctionnaliteLibelles);
        //Choix de la fonctionnalité.
        List<String> fonctionnalitesLibelles = MenuFonctionnalite.getFonctionnalitesLibelles(idRole, indexMenuFonctionnalite);
        int indexFonctionnalite = ui.poserQuestionListeOptions("Sélectionner une action :", fonctionnalitesLibelles);

        //Exécution de la fonctionnalité sélectionnée.
        Fonctionnalite fonctionnalite = MenuFonctionnalite.getFonctionnalite(idRole, indexMenuFonctionnalite, indexFonctionnalite);
        fonctionnalite.executer();
    }
}