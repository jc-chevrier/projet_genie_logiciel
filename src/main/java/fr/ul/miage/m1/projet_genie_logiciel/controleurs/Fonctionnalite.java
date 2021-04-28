package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fonctionnalité.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Fonctionnalite {
    //Fonctionnalités par rôle.
    private static Map<Integer, List<Fonctionnalite>> FONCTIONNALITES_PAR_ROLE;

    //Libellé de la focntionanlité.
    private String libelle;
    //Fonctionnalité.
    private Runnable runnable;

    static {
        //Fonctionnalités du cuisiner.
        List<Fonctionnalite> fonctionnalitesCuisinier =
                Arrays.asList(new Fonctionnalite("Ajouter une unité", UniteControleur::ajouterUnite),
                              new Fonctionnalite("Modifier une unité", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Supprimer une unité", UniteControleur::supprimer), //TODO à modifier
                              new Fonctionnalite("Lister les unités", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Ajouter un ingrédient au catalogue des ingrédients", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Supprimer un ingrédient au catalogue des ingrédients", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Modifier un ingrédient", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Lister les ingrédients", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Ajouter un plat au catalogue des plats", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Supprimer un plat du catalogue des plats", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Modifier un plat", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Lister les plats du catalogue des plats", AccueilControleur::get), //TODO à modifier
                              new Fonctionnalite("Se déconnecter", AuthControleur::seDeconnecter),
                              new Fonctionnalite("Quitter", () -> System.exit(0)));

        //TODO ajouter pour autres rôles.

        FONCTIONNALITES_PAR_ROLE = new HashMap<Integer, List<Fonctionnalite>>();
        FONCTIONNALITES_PAR_ROLE.put(Role.CUISINIER, fonctionnalitesCuisinier);
    }

    /**
     * Obtenir les fonctionnalités pourr un rôle, avec  l'identifiant du rôle.
     *
     * @param idRole
     * @return
     */
    public static List<Fonctionnalite> getRokeFonctionnalite(int idRole) {
        return FONCTIONNALITES_PAR_ROLE.get(idRole);
    }

    /**
     * Obtenir une fonctionnalité, avec l'identifiant du rôle, et l'indice de la fonctionnalité.
     *
     * @param idRole
     * @return
     */
    public static Fonctionnalite getFonctionnalite(int idRole, int indexFonctionnalite) {
        return FONCTIONNALITES_PAR_ROLE.get(idRole).get(indexFonctionnalite);
    }

    /**
     * Obtenir les libellés des fonctionnalité pour un rôle, avec l'identifiant du rôle.
     *
     * @param idRole
     * @return
     */
    public static List<String> getRoleFonctionnalitesLibelles(int idRole) {
        return getRokeFonctionnalite(idRole).stream().map(Fonctionnalite::getLibelle).collect(Collectors.toList());
    }

    public Fonctionnalite(String libelle, Runnable runnable) {
        this.libelle = libelle;
        this.runnable = runnable;
    }

    /**
     * Exécuter une fonctionnalité.
     */
    public void executer() {
        runnable.run();
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
