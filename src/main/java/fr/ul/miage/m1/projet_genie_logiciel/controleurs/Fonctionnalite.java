package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe pour le référencement des
 * fonctionnalités.
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
                Arrays.asList(new FonctionnaliteInterne("Ajouter un plat à la carte du jour", PlatControleur::ajouterACarte),
                              new FonctionnaliteInterne("Supprimer un plat de la carte du jour", PlatControleur::supprimerDeCarte),
                              new FonctionnaliteInterne("Consulter les statistiques générales", StatControleur::consulterGenerales),
                              new FonctionnaliteInterne("Consulter les statistiques sur le chiffre d'affaire", StatControleur::consulterChiffresAffaire),
                              new FonctionnaliteInterne("Lister les salariés", CompteControleur::lister),
                              new FonctionnaliteInterne("Ajouter un salarié", CompteControleur::ajouter),
                              new FonctionnaliteInterne("Modifier les informations d'un salarié", CompteControleur::modifier),
                              new FonctionnaliteInterne("Supprimer un salarié", CompteControleur::supprimer)));

        List<Fonctionnalite> fonctionnalitesMaitreHotel = new ArrayList<Fonctionnalite>(
                Arrays.asList(new FonctionnaliteInterne("Lister toutes les tables", PlaceControleur::lister),
                              new FonctionnaliteInterne("Lister toutes les tables disponibles", PlaceControleur::listerDisponibles),
                              new FonctionnaliteInterne("Ajouter une table", PlaceControleur::ajouter),
                              new FonctionnaliteInterne("Supprimer une table", PlaceControleur::supprimer),
                              new FonctionnaliteInterne("Allouer une table à un client", PlaceControleur::allouerPourClient),
                              new FonctionnaliteInterne("Désallouer une table à un client", PlaceControleur::desallouerPourClient),
                              new FonctionnaliteInterne("Allouer une table à un serveur", PlaceControleur::allouerPourServeur),
                              new FonctionnaliteInterne("Désallouer une table à un serveur", PlaceControleur::desallouerPourServeur),
                              new FonctionnaliteInterne("Lister les tables réservées", PlaceControleur::listerReservees),
                              new FonctionnaliteInterne("Réserver une table", PlaceControleur::reserver),
                              new FonctionnaliteInterne("Annuler la réservation d'une table", PlaceControleur::annulerReservation),
                              new FonctionnaliteInterne("Valider le paiement d'une commande", CommandeControleur::validerPaiement)));

        List<Fonctionnalite> fonctionnalitesCuisinier = new ArrayList<Fonctionnalite>(
                Arrays.asList(new FonctionnaliteInterne("Lister les unités", UniteControleur::lister),
                              new FonctionnaliteInterne("Ajouter une unité", UniteControleur::ajouter),
                              new FonctionnaliteInterne("Modifier une unité", UniteControleur::modifier),
                              new FonctionnaliteInterne("Supprimer une unité", UniteControleur::supprimer),
                              new FonctionnaliteInterne("Lister les ingrédients", IngredientControleur::lister),
                              new FonctionnaliteInterne("Ajouter un ingrédient au catalogue des ingrédients", IngredientControleur::ajouter),
                              new FonctionnaliteInterne("Modifier un ingrédient", IngredientControleur::modifier),
                              new FonctionnaliteInterne("Supprimer un ingrédient au catalogue des ingrédients", IngredientControleur::supprimer),
                              new FonctionnaliteInterne("Modifier le stock d'un ingrédient", IngredientControleur::modifierStock),
                              new FonctionnaliteInterne("Lister les catégories", CategorieControleur::lister),
                              new FonctionnaliteInterne("Ajouter une catégorie", CategorieControleur::ajouter),
                              new FonctionnaliteInterne("Modifier une catégorie", CategorieControleur::modifier),
                              new FonctionnaliteInterne("Supprimer une catégorie", CategorieControleur::supprimer),
                              new FonctionnaliteInterne("Lister les plats du catalogue des plats", PlatControleur::lister),
                              new FonctionnaliteInterne("Ajouter un plat au catalogue des plats", PlatControleur::ajouter),
                              new FonctionnaliteInterne("Modifier un plat", PlatControleur::modifier),
                              new FonctionnaliteInterne("Supprimer un plat du catalogue des plats", PlatControleur::supprimer),
                              new FonctionnaliteInterne("Lister les plats à préparer des commandes", CommandeControleur::listerLignesAPreparer),
                              new FonctionnaliteInterne("Valider la préparation d'un plat d'une commande",  CommandeControleur::validerPreparation)));

        List<Fonctionnalite> fonctionnalitesServeur = new ArrayList<Fonctionnalite>(
                Arrays.asList(new FonctionnaliteInterne("Lister tous les plats de la carte", PlatControleur::listerCarte),
                              new FonctionnaliteInterne("Lister tous les plats disponibles de la carte", PlatControleur::listerDisponiblesCarte),
                              new FonctionnaliteInterne("Lister les plats disponibles de la carte pour une catégorie", PlatControleur::listerDisponiblesPourCategorie),
                              new FonctionnaliteInterne("Lister les catégories de plats disponibles de la carte", CategorieControleur::listerDisponiblesCarte),
                              new FonctionnaliteInterne("Chercher un plat avec son libellé", PlatControleur::chercherAvecLibelle),
                              new FonctionnaliteInterne("Ajouter une commande", CommandeControleur::ajouter),
                              new FonctionnaliteInterne("Supprimer une commande", CommandeControleur::supprimer),
                              new FonctionnaliteInterne("Lister les plats prêts pour mes tables", CommandeControleur::listeLignesPretes),
                              new FonctionnaliteInterne("Lister les plats prêts pour une de mes tables", CommandeControleur::listerLignesPretesPlace)));

        List<Fonctionnalite> fonctionnalitesAssistantService = new ArrayList<Fonctionnalite>(
                Arrays.asList(new FonctionnaliteInterne("Lister les tables à préparer", PlaceControleur::listerAPreparer),
                              new FonctionnaliteInterne("Valider la préparation d'une table", PlaceControleur::validerPreparation)));

        fonctionnalitesDirecteur.addAll(0, fonctionnalitesServeur);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesMaitreHotel);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesCuisinier);
        //fonctionnalitesDirecteur.addAll(fonctionnalitesAssistantService);

        fonctionnalitesServeur.add(0, new FonctionnaliteInterne("Lister mes tables", PlaceControleur::listerAlloueesPourServeur));

        List<Fonctionnalite> fonctionnalitesUtilisateur = new ArrayList<Fonctionnalite>(
                Arrays.asList(new FonctionnaliteInterne("Se déconnecter", AuthControleur::seDeconnecter),
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

    public Fonctionnalite(@NotNull String libelle, @NotNull Runnable runnable) {
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