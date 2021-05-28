package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CommandeControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commande")
public class CommandeTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables(){
        //On réinitialise la table stat chiffre d'affaire.
        orm.reinitialiserTable(StatChiffreAffaire.class);
        //On réinitialise la table ligne commande.
        orm.reinitialiserTable(LigneCommande.class);
        //On réinitialise la table plat_ingrédiants.
        orm.reinitialiserTable(PlatIngredients.class);
        //On réinitialise la table plat.
        orm.reinitialiserTable(Plat.class);
        //On réinitialise la table catégorie.
        orm.reinitialiserTable(Categorie.class);
        //On réinitialise la table ingrédient.
        orm.reinitialiserTable(Ingredient.class);
        //On réinitialise la table unité.
        orm.reinitialiserTable(Unite.class);
        //On réinitialise la table commande.
        orm.reinitialiserTable(Commande.class);

    }

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
        ui = UI.getInstance();
    }

    @BeforeEach
    void faireAvantChaqueTest() {
        reinitialiserTables();
    }

    @AfterAll
    static void faireApresTousLesTests() {
        reinitialiserTables();
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 1 : validation réussite")
    void testValiderPaiementCas1BienFait() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_paiement_cas_1.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table occupé pour pouvoir passer une commande.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("catégorie");
        orm.persisterNUplet(categorie);

        //On ajoute une unité pour pouvoir ajouter un ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("unité");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient pour pouvoir l'ajouter à la composition du plat.
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle("ingredient");
        ingredient.setStock(20.0);
        ingredient.setIdUnite(1);
        orm.persisterNUplet(ingredient);

        //On ajoute un plat à la carte du jour.
        Plat plat = new Plat();
        plat.setLibelle("plat");
        plat.setCarte(1);
        plat.setIdCategorie(categorie.getId());
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On ajoute un platIngredients car un plat est disponible uniquement
        // si la quantité utilisé par ce plat est inferieure ou égale aux stocks des ingrédients qui le compose.
        PlatIngredients platIngredient = new PlatIngredients();
        platIngredient.setQuantite(2.0);
        platIngredient.setIdPlat(1);
        platIngredient.setIdIngredient(1);
        orm.persisterNUplet(platIngredient);

        //On ajoute une commande
        Commande commande = new Commande();
        commande.setEtat("servi");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat("servi");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();

    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 2 : validation correcte")
    void testValiderPaiementCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_paiement_cas_2.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table occupé pour pouvoir passer une commande.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("catégorie");
        orm.persisterNUplet(categorie);

        //On ajoute une unité pour pouvoir ajouter un ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("unité");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient pour pouvoir l'ajouter à la composition du plat.
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle("ingredient");
        ingredient.setStock(20.0);
        ingredient.setIdUnite(1);
        orm.persisterNUplet(ingredient);

        //On ajoute un plat à la carte du jour.
        Plat plat = new Plat();
        plat.setLibelle("plat");
        plat.setCarte(1);
        plat.setIdCategorie(categorie.getId());
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On ajoute un platIngredients car un plat est disponible uniquement
        // si la quantité utilisé par ce plat est inferieure ou égale aux stocks des ingrédients qui le compose.
        PlatIngredients platIngredient = new PlatIngredients();
        platIngredient.setQuantite(2.0);
        platIngredient.setIdPlat(1);
        platIngredient.setIdIngredient(1);
        orm.persisterNUplet(platIngredient);

        //On ajoute une commande
        Commande commande = new Commande();
        commande.setEtat("servi");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat("servi");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);

        Commande commandeAvant = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("servi", commandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();

        Commande commandeApres = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("payé", commandeApres.getEtat());
    }


    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 3 : aucune commande servie trouvée dans la base")
    void testValiderPaiementCas3PasServiesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On ajoute une table occupé pour pouvoir passer une commande.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("catégorie");
        orm.persisterNUplet(categorie);

        //On ajoute une unité pour pouvoir ajouter un ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("unité");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient pour pouvoir l'ajouter à la composition du plat.
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle("ingredient");
        ingredient.setStock(20.0);
        ingredient.setIdUnite(1);
        orm.persisterNUplet(ingredient);

        //On ajoute un plat à la carte du jour.
        Plat plat = new Plat();
        plat.setLibelle("plat");
        plat.setCarte(1);
        plat.setIdCategorie(categorie.getId());
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On ajoute un platIngredients car un plat est disponible uniquement
        // si la quantité utilisé par ce plat est inferieure ou égale aux stocks des ingrédients qui le compose.
        PlatIngredients platIngredient = new PlatIngredients();
        platIngredient.setQuantite(2.0);
        platIngredient.setIdPlat(1);
        platIngredient.setIdIngredient(1);
        orm.persisterNUplet(platIngredient);

        //On ajoute une commande
        Commande commande = new Commande();
        commande.setEtat("payé");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat("prêt");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);


        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 4 : aucune commande  trouvée dans la base")
    void testValiderPaiementCas4PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();
    }


}