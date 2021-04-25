package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

/**
 * Superclasse asbtraite de tous les contrôleurs,
 * fournissant des utilitaires aux contrôleurs.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public abstract class Controleur {
    /**
     * Obtenir l'ORM.
     *
     * @return
     */
    protected static ORM getORM()  {
        return ORM.getInstance();
    }

    /**
     * Obtenir l'UI.
     *
     * @return
     */
    protected static UI getUI()  {
        return UI.getInstance();
    }

    /**
     * Obtenir l'utilisateur courant connecté.
     *
     * @return
     */
    protected static Compte getUtilisateurCourant()  {
        return getUI().getUtilisateurCourant();
    }
}
