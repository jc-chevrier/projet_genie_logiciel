package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.entites.PlatIngredients;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.*;
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
        List<Fonctionnalite> fonctionnalitesDirecteur = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Ajouter un plat à la carte du jour", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Supprimer un plat de la carte du jour", AccueilControleur::consulter)));//TODO à modifier

        List<Fonctionnalite> fonctionnalitesMaitreHotel = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Lister toutes les tables", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Lister toutes les tables disponibles", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Ajouter une table", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Supprimer une table", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Allouer une table à un client", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Désallouer une table à un client", PlaceControleur::desallouerPourClient),
                              new Fonctionnalite("Allouer une table à un serveur", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Désallouer une table à un serveur", AccueilControleur::consulter)));//TODO à modifier

        List<Fonctionnalite> fonctionnalitesCuisinier = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Lister les unités", UniteControleur::lister),
                              new Fonctionnalite("Ajouter une unité", UniteControleur::ajouter),
                              new Fonctionnalite("Modifier une unité", UniteControleur::modifier),
                              new Fonctionnalite("Supprimer une unité", UniteControleur::supprimer),
                              new Fonctionnalite("Lister les ingrédients", IngredientControleur::lister),
                              new Fonctionnalite("Ajouter un ingrédient au catalogue des ingrédients", IngredientControleur::ajouter),
                              new Fonctionnalite("Modifier un ingrédient", IngredientControleur::modifier),
                              new Fonctionnalite("Supprimer un ingrédient au catalogue des ingrédients", IngredientControleur::supprimer),
                              new Fonctionnalite("Lister les catégories", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Ajouter une catégorie", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Modifier une catégorie", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Supprimer une catégorie", AccueilControleur::consulter),//TODO à modifier
                              new Fonctionnalite("Incrémenter le stock d'un ingrédient", AccueilControleur::consulter), //TODO à modifier
                              new Fonctionnalite("Lister les plats du catalogue des plats", PlatControleur::lister),
                              new Fonctionnalite("Ajouter un plat au catalogue des plats", PlatControleur::ajouter),
                              new Fonctionnalite("Modifier un plat", AccueilControleur::consulter), //TODO à modifier
                              new Fonctionnalite("Supprimer un plat du catalogue des plats", PlatControleur::supprimer)));

        List<Fonctionnalite> fonctionnalitesServeur = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Lister tous les plats de la carte", AccueilControleur::consulter), //TODO à modifier
                              new Fonctionnalite("Lister tous les plats disponibles de la carte", AccueilControleur::consulter), //TODO à modifier
                              new Fonctionnalite("Lister les plats disponibles de la carte pour une catégorie", AccueilControleur::consulter), //TODO à modifier
                             new Fonctionnalite("Lister les catégories de plats disponibles de la carte", AccueilControleur::consulter))); //TODO à modifier

        List<Fonctionnalite> fonctionnalitesAssistantService = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Lister les tables à préparer", AccueilControleur::consulter), //TODO à modifier
                              new Fonctionnalite("Valider la préparation d'une table", AccueilControleur::consulter))); //TODO à modifier

        fonctionnalitesDirecteur.addAll(0, fonctionnalitesServeur);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesMaitreHotel);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesCuisinier);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesAssistantService);

        fonctionnalitesServeur.add(0, new Fonctionnalite("Lister mes tables", AccueilControleur::consulter));

        List<Fonctionnalite> fonctionnalitesUtilisateur = new ArrayList<Fonctionnalite>(
                Arrays.asList(new Fonctionnalite("Se déconnecter", AuthControleur::seDeconnecter),
                              new Fonctionnalite("Quitter", () -> UI.getInstance().afficherAvecDelimiteurEtUtilisateur("Fin."))));

        fonctionnalitesDirecteur.addAll(fonctionnalitesUtilisateur);
        fonctionnalitesCuisinier.addAll(fonctionnalitesUtilisateur);
        fonctionnalitesMaitreHotel.addAll(fonctionnalitesUtilisateur);
        fonctionnalitesServeur.addAll(fonctionnalitesUtilisateur);
        fonctionnalitesAssistantService.addAll(fonctionnalitesUtilisateur);

        FONCTIONNALITES_PAR_ROLE = new HashMap<Integer, List<Fonctionnalite>>();
        FONCTIONNALITES_PAR_ROLE.put(Role.DIRECTEUR, fonctionnalitesDirecteur);
        FONCTIONNALITES_PAR_ROLE.put(Role.MAITRE_HOTEL, fonctionnalitesMaitreHotel);
        FONCTIONNALITES_PAR_ROLE.put(Role.CUISINIER, fonctionnalitesCuisinier);
        FONCTIONNALITES_PAR_ROLE.put(Role.SERVEUR, fonctionnalitesServeur);
        FONCTIONNALITES_PAR_ROLE.put(Role.ASSISTANT_SERVICE, fonctionnalitesAssistantService);
    }

    /**
     * Obtenir les fonctionnalités pour un rôle, avec  l'identifiant du rôle.
     *
     * @param idRole
     * @return
     */
    public static List<Fonctionnalite> getRoleFonctionnalite(int idRole) {
        return FONCTIONNALITES_PAR_ROLE.get(idRole);
    }

    /**
     * Obtenir une fonctionnalité, avec l'identifiant du rôle, et l'indice de la fonctionnalité.
     *
     * @param idRole
     *  @param indexFonctionnalite
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
        return getRoleFonctionnalite(idRole).stream().map(Fonctionnalite::getLibelle).collect(Collectors.toList());
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
