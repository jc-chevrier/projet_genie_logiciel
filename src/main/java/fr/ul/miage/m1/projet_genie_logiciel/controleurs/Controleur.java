package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    protected static Compte getUtilisateurConnecte()  {
        return getUI().getUtilisateurConnecte();
    }

    /**
     * Savoir si un utilisateur est connecté.
     *
     * @return
     */
    protected static boolean utilisateurConnecte()  {
        return getUtilisateurConnecte() != null;
    }

    /**
     * Récupérer un n-uplet en filtrant une liste de n-uplets.
     */
    protected static Entite filterListeNUpletsAvecId(@NotNull List<Entite> listeNUplets, int idNUplet) {
        return listeNUplets.stream()
                            .filter((nUplet) -> nUplet.getId().equals(idNUplet))
                            .findFirst()
                            .get();
    }
}
