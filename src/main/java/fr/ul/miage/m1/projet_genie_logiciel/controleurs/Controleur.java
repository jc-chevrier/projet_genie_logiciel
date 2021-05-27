package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

/**
 * Superclasse abstraite de tous les contrôleurs,
 * fournissant des utilitaires aux contrôleurs.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public abstract class Controleur {
    //ORM pour la communication avec la base de données.
    protected static ORM orm = ORM.getInstance();

    //UI pour la communication avec les utilisateurs via l'interface console.
    protected static UI ui = UI.getInstance();

    /**
     * Obtenir l'utilisateur courant connecté.
     *
     * @return
     */
    protected static Compte getUtilisateurConnecte()  {
        return ui.getUtilisateurConnecte();
    }

    /**
     * Savoir si un utilisateur est connecté.
     *
     * @return
     */
    protected static boolean utilisateurConnecte()  {
        return getUtilisateurConnecte() != null;
    }
}
