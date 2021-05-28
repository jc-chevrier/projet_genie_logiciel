package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe pour la définition des menus de fonctionnalité,
 * et également pour le référencement et le parcours des
 * menus et des fonctionnalités.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class MenuFonctionnalite {
    //Menu de fonctionnalité par rôle.
    private static Map<Integer, List<MenuFonctionnalite>> MENU_FONCTIONNALITE_PAR_ROLE;

    //Libellé du menu.
    private String libelle;
    //Liste des fonctionnalités liées au menu.
    private List<Fonctionnalite> fonctionnalites;

    static {
        //Déclarations des fonctionnalités et des menus.

        //Fonctionnalités du directeur.
        List<Fonctionnalite> fonctionnalitesGestionCarte = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister tous les plats de la carte", PlatControleur::listerCarte),
                new FonctionnaliteInterne("Lister tous les plats disponibles de la carte", PlatControleur::listerDisponiblesCarte),
                new FonctionnaliteInterne("Lister les plats disponibles de la carte pour une catégorie", PlatControleur::listerDisponiblesPourCategorie),
                new FonctionnaliteInterne("Lister les catégories de plats disponibles de la carte", CategorieControleur::listerDisponiblesCarte),
                new FonctionnaliteInterne("Ajouter un plat à la carte du jour", PlatControleur::ajouterACarte),
                new FonctionnaliteInterne("Supprimer un plat de la carte du jour", PlatControleur::supprimerDeCarte)));
        MenuFonctionnalite menuFonctionnaliteGestionCarte =
                new MenuFonctionnalite("Gestion de la carte du jour", fonctionnalitesGestionCarte);

        List<Fonctionnalite> fonctionnalitesConsultationStats = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Consulter les statistiques générales", StatControleur::consulterGenerales),
                new FonctionnaliteInterne("Consulter les statistiques sur le chiffre d'affaire", StatControleur::consulterChiffresAffaire)));
        MenuFonctionnalite menuFonctionnaliteConsultationStats =
                new MenuFonctionnalite("Statistiques", fonctionnalitesConsultationStats);

        List<Fonctionnalite> fonctionnalitesGestionSalaries = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les salariés", CompteControleur::lister),
                new FonctionnaliteInterne("Ajouter un salarié", CompteControleur::ajouter),
                new FonctionnaliteInterne("Modifier les informations d'un salarié", CompteControleur::modifier),
                new FonctionnaliteInterne("Supprimer un salarié", CompteControleur::supprimer)));
        MenuFonctionnalite menuFonctionnaliteGestionSalaries =
                new MenuFonctionnalite("Gestion des salariés", fonctionnalitesGestionSalaries);
        //Rappels : le directeur peut utiliser toutes les fonctionnalités également des autres rôles
        //qui suivent après.


        //Fonctionnalités du maitre d'hôtel.
        List<Fonctionnalite> fonctionnalitesGestionTables = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister toutes les tables", PlaceControleur::lister),
                new FonctionnaliteInterne("Lister toutes les tables disponibles", PlaceControleur::listerDisponibles),
                new FonctionnaliteInterne("Ajouter une table", PlaceControleur::ajouter),
                new FonctionnaliteInterne("Supprimer une table", PlaceControleur::supprimer),
                new FonctionnaliteInterne("Allouer une table à un client", PlaceControleur::allouerPourClient),
                new FonctionnaliteInterne("Désallouer une table à un client", PlaceControleur::desallouerPourClient),
                new FonctionnaliteInterne("Allouer une table à un serveur", PlaceControleur::allouerPourServeur),
                new FonctionnaliteInterne("Désallouer une table à un serveur", PlaceControleur::desallouerPourServeur)));
        MenuFonctionnalite menuFonctionnaliteGestionTables =
                new MenuFonctionnalite("Gestion des tables", fonctionnalitesGestionTables);

        List<Fonctionnalite> fonctionnalitesGestionAllocationsTables = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Allouer une table à un client", PlaceControleur::allouerPourClient),
                new FonctionnaliteInterne("Désallouer une table à un client", PlaceControleur::desallouerPourClient),
                new FonctionnaliteInterne("Allouer une table à un serveur", PlaceControleur::allouerPourServeur),
                new FonctionnaliteInterne("Désallouer une table à un serveur", PlaceControleur::desallouerPourServeur)));
        MenuFonctionnalite menuFonctionnaliteGestionAllocationsTables =
                new MenuFonctionnalite("Gestion des allocations de table", fonctionnalitesGestionAllocationsTables);

        List<Fonctionnalite> fonctionnalitesGestionReservationsTables = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les tables réservées", PlaceControleur::listerReservees),
                new FonctionnaliteInterne("Réserver une table", PlaceControleur::reserver),
                new FonctionnaliteInterne("Annuler la réservation d'une table", PlaceControleur::annulerReservation),
                new FonctionnaliteInterne("Valider le paiement d'une commande", CommandeControleur::validerPaiement)));
        MenuFonctionnalite menuFonctionnaliteGestionReservationsTables  =
                new MenuFonctionnalite("Gestion des réservations de table", fonctionnalitesGestionReservationsTables);

        List<Fonctionnalite> fonctionnalitesGestionPaiementsCommandes = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Valider le paiement d'une commande", CommandeControleur::validerPaiement)));
        MenuFonctionnalite menuFonctionnaliteGestionPaiementsCommandes =
                new MenuFonctionnalite("Gestion des paiements des commandes", fonctionnalitesGestionPaiementsCommandes);


        //Fonctionnalités du cuisiner.
        List<Fonctionnalite> fonctionnalitesGestionUnites = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les unités", UniteControleur::lister),
                new FonctionnaliteInterne("Ajouter une unité", UniteControleur::ajouter),
                new FonctionnaliteInterne("Modifier une unité", UniteControleur::modifier),
                new FonctionnaliteInterne("Supprimer une unité", UniteControleur::supprimer)));
        MenuFonctionnalite menuFonctionnaliteGestionUnites =
                new MenuFonctionnalite("Gestion du catalogue des unités", fonctionnalitesGestionUnites);

        List<Fonctionnalite> fonctionnalitesGestionIngredients = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les ingrédients", IngredientControleur::lister),
                new FonctionnaliteInterne("Ajouter un ingrédient au catalogue des ingrédients", IngredientControleur::ajouter),
                new FonctionnaliteInterne("Modifier un ingrédient", IngredientControleur::modifier),
                new FonctionnaliteInterne("Supprimer un ingrédient au catalogue des ingrédients", IngredientControleur::supprimer)));
        MenuFonctionnalite menuFonctionnaliteGestionIngredients =
                new MenuFonctionnalite("Gestion du catalogue des ingrédients", fonctionnalitesGestionIngredients);

        List<Fonctionnalite> fonctionnalitesGestionStocksIngredients = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Modifier le stock d'un ingrédient", IngredientControleur::modifierStock)));
        MenuFonctionnalite menuFonctionnaliteGestionStocksIngredients =
                new MenuFonctionnalite("Gestion des stocks des ingrédients", fonctionnalitesGestionStocksIngredients);

        List<Fonctionnalite> fonctionnalitesGestionCategories = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les catégories", CategorieControleur::lister),
                new FonctionnaliteInterne("Ajouter une catégorie", CategorieControleur::ajouter),
                new FonctionnaliteInterne("Modifier une catégorie", CategorieControleur::modifier),
                new FonctionnaliteInterne("Supprimer une catégorie", CategorieControleur::supprimer)));
        MenuFonctionnalite menuFonctionnaliteGestionCategories =
                new MenuFonctionnalite("Gestion du catalogue des catégories", fonctionnalitesGestionCategories);

        List<Fonctionnalite> fonctionnalitesGestionPlats = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les plats", PlatControleur::lister),
                new FonctionnaliteInterne("Ajouter un plat", PlatControleur::ajouter),
                new FonctionnaliteInterne("Modifier un plat", PlatControleur::modifier),
                new FonctionnaliteInterne("Supprimer un plat", PlatControleur::supprimer)));
        MenuFonctionnalite menuFonctionnaliteGestionPlats =
                new MenuFonctionnalite("Gestion du cataloque des plats", fonctionnalitesGestionPlats);

        List<Fonctionnalite> fonctionnalitesPreparationsCommandes = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les plats à préparer des commandes", CommandeControleur::listerLignesAPreparer),
                new FonctionnaliteInterne("Valider la préparation d'un plat d'une commande",  CommandeControleur::validerPreparation)));
        MenuFonctionnalite menuFonctionnaliteGestionPreparationsCommandes =
                new MenuFonctionnalite("Gestion des préparations des commandes", fonctionnalitesPreparationsCommandes);


        //Fonctionnalités des serveurs.
        List<Fonctionnalite> fonctionnalitesMesTables = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister mes tables", PlaceControleur::listerAlloueesPourServeur)));
        MenuFonctionnalite menuFonctionnaliteMesTables =
                new MenuFonctionnalite("Mes tables", fonctionnalitesMesTables);

        List<Fonctionnalite> fonctionnalitesGestionCommandes = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Ajouter une commande pour une de mes tables", CommandeControleur::ajouter),
                new FonctionnaliteInterne("Supprimer une commande", CommandeControleur::supprimer),
                new FonctionnaliteInterne("Lister les plats prêts pour mes tables", CommandeControleur::listeLignesPretes),
                new FonctionnaliteInterne("Lister les plats prêts pour une de mes tables", CommandeControleur::listerLignesPretesPlace)));
        MenuFonctionnalite menuFonctionnaliteGestionCommandes =
                new MenuFonctionnalite("Gestion des commandes de mes tables", fonctionnalitesGestionCommandes);

        List<Fonctionnalite> fonctionnalitesConsultationCarte = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister tous les plats de la carte", PlatControleur::listerCarte),
                new FonctionnaliteInterne("Lister tous les plats disponibles de la carte", PlatControleur::listerDisponiblesCarte),
                new FonctionnaliteInterne("Lister les plats disponibles de la carte pour une catégorie", PlatControleur::listerDisponiblesPourCategorie),
                new FonctionnaliteInterne("Lister les catégories de plats disponibles de la carte", CategorieControleur::listerDisponiblesCarte)));
        MenuFonctionnalite menuFonctionnaliteConsultationCarte =
                new MenuFonctionnalite("Carte du jour", fonctionnalitesConsultationCarte);
        
        List<Fonctionnalite> fonctionnalitesRecherchePlat = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Chercher un plat avec son libellé", PlatControleur::chercherAvecLibelle)));
        MenuFonctionnalite menuFonctionnaliteRecherchePlat =
                new MenuFonctionnalite("Recherche de plat", fonctionnalitesRecherchePlat);


        //Fonctionnalités des assistants de service.
        List<Fonctionnalite> fonctionnalitesGestionPreparationsTables = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Lister les tables à préparer", PlaceControleur::listerAPreparer),
                new FonctionnaliteInterne("Valider la préparation d'une table", PlaceControleur::validerPreparation)));
        MenuFonctionnalite menuFonctionnaliteGestionPreparationsTables =
                new MenuFonctionnalite("Gestion des préparations des tables", fonctionnalitesGestionPreparationsTables);


        //Fonctionnalités communes.
        List<Fonctionnalite> fonctionnalitesSortie = new ArrayList<Fonctionnalite>(Arrays.asList(
                new FonctionnaliteInterne("Se déconnecter", AuthControleur::seDeconnecter),
                new Fonctionnalite("Quitter", () -> UI.getInstance().afficherAvecDelimiteurEtUtilisateur("Fin."))));
        MenuFonctionnalite menuFonctionnaliteSortie =
                new MenuFonctionnalite("Sorties", fonctionnalitesSortie);


        //Répartition des menus de fonctionnalitéscc par rôle.
        MENU_FONCTIONNALITE_PAR_ROLE = new HashMap<Integer, List<MenuFonctionnalite>>();

        MENU_FONCTIONNALITE_PAR_ROLE.put(Role.DIRECTEUR, Arrays.asList(
                menuFonctionnaliteGestionUnites,
                menuFonctionnaliteGestionIngredients,
                menuFonctionnaliteGestionStocksIngredients,
                menuFonctionnaliteGestionCategories,
                menuFonctionnaliteGestionPlats,
                menuFonctionnaliteRecherchePlat,
                menuFonctionnaliteGestionCarte,
                menuFonctionnaliteGestionTables,
                menuFonctionnaliteGestionAllocationsTables,
                menuFonctionnaliteGestionReservationsTables,
                menuFonctionnaliteGestionPreparationsTables,
                menuFonctionnaliteGestionPreparationsCommandes,
                menuFonctionnaliteGestionPaiementsCommandes,
                menuFonctionnaliteGestionSalaries,
                menuFonctionnaliteConsultationStats,
                menuFonctionnaliteSortie));

        MENU_FONCTIONNALITE_PAR_ROLE.put(Role.MAITRE_HOTEL, Arrays.asList(
                menuFonctionnaliteGestionTables,
                menuFonctionnaliteGestionAllocationsTables,
                menuFonctionnaliteGestionReservationsTables,
                menuFonctionnaliteGestionPaiementsCommandes,
                menuFonctionnaliteSortie));

        MENU_FONCTIONNALITE_PAR_ROLE.put(Role.CUISINIER, Arrays.asList(
                menuFonctionnaliteGestionUnites,
                menuFonctionnaliteGestionIngredients,
                menuFonctionnaliteGestionStocksIngredients,
                menuFonctionnaliteGestionCategories,
                menuFonctionnaliteGestionPlats,
                menuFonctionnaliteGestionPreparationsCommandes,
                menuFonctionnaliteSortie));

        MENU_FONCTIONNALITE_PAR_ROLE.put(Role.SERVEUR, Arrays.asList(
                menuFonctionnaliteMesTables,
                menuFonctionnaliteGestionCommandes,
                menuFonctionnaliteConsultationCarte,
                menuFonctionnaliteRecherchePlat,
                menuFonctionnaliteSortie));

        MENU_FONCTIONNALITE_PAR_ROLE.put(Role.ASSISTANT_SERVICE, Arrays.asList(
                menuFonctionnaliteGestionPreparationsTables,
                menuFonctionnaliteSortie));
    }

    /**
     * Obtenir les menus de fonctionnalité pour un rôle, avec l'identifiant du rôle.
     *
     * @param idRole
     * @return
     */
    public static List<MenuFonctionnalite> getRoleMenusFonctionnalite(int idRole) {
        return MENU_FONCTIONNALITE_PAR_ROLE.get(idRole);
    }

    /**
     * Obtenir les libellés des menus de fonctionnalité pour un rôle, avec l'identifiant du rôle.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.controleurs.MenuFonctionnalite#getRoleMenusFonctionnalite(int)
     *
     * @param idRole
     * @return
     */
    public static List<String> getRoleMenusFonctionnaliteLibelles(int idRole) {
        return getRoleMenusFonctionnalite(idRole)
              .stream()
              .map(MenuFonctionnalite::getLibelle)
              .collect(Collectors.toList());
    }

    /**
     * Obtenir un menu de fonctionnalité, avec l'identifiant du rôle, et l'indice
     * du menu de fonctionnalité.
     *
     * @param idRole
     * @param indexMenuFonctionnalite
     * @return
     */
    public static MenuFonctionnalite getMenuFonctionnalite(int idRole, int indexMenuFonctionnalite) {
        return MENU_FONCTIONNALITE_PAR_ROLE.get(idRole)
                                           .get(indexMenuFonctionnalite);
    }

    /**
     * Obtenir les fonctionnalités d'un menu, avec l'identifiant du rôle, et l'indice
     * du menu de fonctionnalité.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.controleurs.MenuFonctionnalite#getMenuFonctionnalite(int, int)
     *
     * @param idRole
     * @param indexMenuFonctionnalite
     * @return
     */
    public static List<Fonctionnalite> getFonctionnalites(int idRole, int indexMenuFonctionnalite) {
        return getMenuFonctionnalite(idRole, indexMenuFonctionnalite)
              .getFonctionnalites();
    }

    /**
     * Obtenir les libellés des fonctionnalités d'un menu, avec l'identifiant du rôle, et l'indice
     * du menu de fonctionnalités.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.controleurs.MenuFonctionnalite#getFonctionnalites(int, int)
     *
     * @param idRole
     * @param indexMenuFonctionnalite
     * @return
     */
    public static List<String> getFonctionnalitesLibelles(int idRole, int indexMenuFonctionnalite) {
        return getFonctionnalites(idRole, indexMenuFonctionnalite)
               .stream()
               .map(Fonctionnalite::getLibelle)
               .collect(Collectors.toList());
    }

    /**
     * Obtenir les libellés des fonctionnalités d'un menu, avec l'identifiant du rôle, l'indice
     * du menu de fonctionnalités, et l'indice de la fonctionnalité.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.controleurs.MenuFonctionnalite#getFonctionnalites(int, int)
     *
     * @param idRole
     * @param indexMenuFonctionnalite
     * @param indexFonctionnalite
     * @return
     */
    public static Fonctionnalite getFonctionnalite(int idRole, int indexMenuFonctionnalite, int indexFonctionnalite) {
        return getFonctionnalites(idRole, indexMenuFonctionnalite)
               .get(indexFonctionnalite);
    }

    public MenuFonctionnalite(@NotNull String libelle, @NotNull List<Fonctionnalite> fonctionnalites) {
        this.libelle = libelle;
        this.fonctionnalites = fonctionnalites;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Fonctionnalite> getFonctionnalites() {
        return fonctionnalites;
    }

    public void setFonctionnalites(@NotNull List<Fonctionnalite> fonctionnalites) {
        this.fonctionnalites = fonctionnalites;
    }
}