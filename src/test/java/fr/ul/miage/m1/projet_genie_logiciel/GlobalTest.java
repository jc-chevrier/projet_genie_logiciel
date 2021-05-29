package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Classe fournissant des utilitaires aux tests.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class GlobalTest {
    protected static ORM orm;
    protected static UI ui;

    static {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
        ui = UI.getInstance();
    }

    /**
     * Se connecter en tant qu'un compte.
     *
     * @param idCompte
     */
    private static void seConnecter(int idCompte) {
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = " + idCompte, Compte.class));
    }

    /**
     * Se connecter en tant que directeur.
     */
    protected static void seConnecterEnDirecteur() {
        seConnecter(1);
    }

    /**
     * Se connecter en tant que maitre d'hôtel.
     */
    protected static void seConnecterEnMaitreHotel() {
        seConnecter(2);
    }

    /**
     * Se connecter en tant que cuisinier.
     */
    protected static void seConnecterEnCuisinier() {
        seConnecter(3);
    }

    /**
     * Se connecter en tant que serveur.
     */
    protected static void seConnecterEnServeur() {
        seConnecter(4);
    }

    /**
     * Se connecter en tant qu'assistant de service.
     */
    protected static void seConnecterEnAssistantService() {
        seConnecter(5);
    }

    /**
     * Charger un fichier de saisie.
     *
     * @param cheminFichierSaisie
     */
    protected static void chargerSaisies(@NotNull String cheminFichierSaisie) {
        System.setIn(GlobalTest.class.getResourceAsStream(cheminFichierSaisie));
        ui.reinitialiserScanner();
    }

    /**
     * Ajouter un compte.
     *
     * @param nom
     * @param prenom
     * @param actif
     * @param idRole
     * @return
     */
    protected static Compte ajouterCompte(@NotNull String nom, @NotNull String prenom, int actif, int idRole) {
        Compte compte = new Compte();
        compte.setNom(nom);
        compte.setPrenom(prenom);
        compte.setActif(actif);
        compte.setIdRole(idRole);
        orm.persisterNUplet(compte);
        return compte;
    }

    /**
     * Ajouter une unité.
     *
     * @param libelle
     */
    protected static Unite ajouterUnite(@NotNull String libelle) {
        Unite unite = new Unite();
        unite.setLibelle(libelle);
        orm.persisterNUplet(unite);
        return unite;
    }

    /**
     * Ajouter un ingrédient.
     *
     * @param libelle
     * @param stock
     * @param idUnite
     * @return
     */
    protected static Ingredient ajouterIngredient(@NotNull String libelle, double stock, int idUnite) {
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle(libelle);
        ingredient.setStock(stock);
        ingredient.setIdUnite(idUnite);
        orm.persisterNUplet(ingredient);
        return ingredient;
    }

    /**
     * Ajouter une catégorie.
     *
     * @param libelle
     * @return
     */
    protected static Categorie ajouterCategorie(@NotNull String libelle) {
        Categorie categorie = new Categorie();
        categorie.setLibelle(libelle);
        orm.persisterNUplet(categorie);
        return categorie;
    }

    /**
     * Ajouter un plat.
     *
     * @param libelle
     * @param prix
     * @param carte
     * @param idCategorie
     * @return
     */
    protected static Plat ajouterPlat(@NotNull String libelle, double prix, int carte, int idCategorie) {
        Plat plat = new Plat();
        plat.setLibelle(libelle);
        plat.setPrix(prix);
        plat.setCarte(carte);
        plat.setIdCategorie(idCategorie);
        orm.persisterNUplet(plat);
        return plat;
    }

    /**
     * Ajouter une ligne de composition d'un plat.
     *
     * @param quantite
     * @param idPlat
     * @param idIngredient
     * @return
     */
    protected static PlatIngredients ajouterPlatIngredient(double quantite, int idPlat, int idIngredient) {
        PlatIngredients platIngredient = new PlatIngredients();
        platIngredient.setQuantite(quantite);
        platIngredient.setIdPlat(idPlat);
        platIngredient.setIdIngredient(idIngredient);
        orm.persisterNUplet(platIngredient);
        return platIngredient;
    }

    /**
     * Ajoute une table en précisant juste son état.
     *
     * @param etat
     * @return
     */
    protected static Place ajouterPlaceEtat(@NotNull String etat) {
        Place place = new Place();
        place.setEtat(etat);
        orm.persisterNUplet(place);
        return place;
    }

    /**
     * Ajoute une table en précisant juste son état.
     *
     * @param etat
     * @return
     */
    protected static Place ajouterPlaceEtatServeur(@NotNull String etat, int idCompteServeur) {
        Place place = new Place();
        place.setEtat(etat);
        place.setIdCompteServeur(idCompteServeur);
        orm.persisterNUplet(place);
        return place;
    }

    /**
     * Ajoute une table avec une réservation.
     *
     * @param etat
     * @param nom
     * @param prenom
     * @param date
     * @return
     */
    protected static Place ajouterPlaceReservee(@NotNull String etat, @NotNull String nom, @NotNull String prenom,
                                     @NotNull Date date) {
        Place place = new Place();
        place.setEtat(etat);
        place.setNomReservation(nom);
        place.setPrenomReservation(prenom);
        place.setDatetimeReservation(date);
        orm.persisterNUplet(place);
        return place;
    }

    /**
     * Ajouter une commande.
     *
     * @param etat
     * @param coutTotal
     * @param datetimeCreation
     * @param idPlace
     * @return
     */
    protected static Commande ajouterCommande(@NotNull String etat, double coutTotal, Date datetimeCreation, int idPlace) {
        Commande commande = new Commande();
        commande.setEtat(etat);
        commande.setCoutTotal(coutTotal);
        commande.setDatetimeCreation(datetimeCreation);
        commande.setIdPlace(idPlace);
        orm.persisterNUplet(commande);
        return commande;
    }

    /**
     * Ajouter une ligne de commande.
     *
     * @param etat
     * @param nbOccurences
     * @param idPlat
     * @param idCommande
     * @return
     */
    protected static LigneCommande ajouterLigneCommande(@NotNull String etat, int nbOccurences, int idPlat, int idCommande) {
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat(etat);
        ligneCommande.setNbOccurences(nbOccurences);
        ligneCommande.setIdPlat(idPlat);
        ligneCommande.setIdCommande(idCommande);
        orm.persisterNUplet(ligneCommande);
        return ligneCommande;
    }
}