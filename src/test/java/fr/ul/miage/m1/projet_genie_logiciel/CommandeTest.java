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

    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 1 : validation réussite")
    void testValiderPreparationCas1BienFait() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_preparation_cas_1.txt"));
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
        commande.setEtat("en attente");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat("en attente");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);


        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();

    }

    @Test
    @DisplayName("Test : Valider la préparation d'un plat d'une commande - cas 2 : validation correcte")
    void testValiderPreparationCas2Correcte() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_preparation_cas_2.txt"));
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
        commande.setEtat("en attente");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setEtat("en attente");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);

        LigneCommande lignecommandeAvant = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",LigneCommande.class);
        assertEquals("en attente", lignecommandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();

        LigneCommande lignecommandeApres = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",LigneCommande.class);
        assertEquals("prêt", lignecommandeApres.getEtat());
    }


    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 3 : aucune commande en attente trouvée dans la base")
    void testValiderPreparationCas3PasEnAttenteTrouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

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
        CommandeControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 4 : aucune commande trouvée dans la base")
    void testValiderPreparationCas4PasTrouvees() {
        //On se connecte en tant que cuisiner.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : Valider le service d'une commande - cas 1 : validation réussite")
    void testValiderServiceCas1BienFait() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_service_cas_1.txt"));
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
        commande.setEtat("en attente");
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
        CommandeControleur.validerService();

    }

    @Test
    @DisplayName("Test : Valider le service d'une commande - cas 2 : validation correcte")
    void testValiderServiceCas2Correcte() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_service_cas_2.txt"));
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
        commande.setEtat("en attente");
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

        Commande commandeAvant = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("en attente", commandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerService();

        Commande commandeApres = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("servi", commandeApres.getEtat());
    }


    @Test
    @DisplayName("Test : Valider le service d'une commande - cas 3 : aucune commande en attente trouvée dans la base")
    void testValiderServiceCas3Pastrouvees() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

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
        ligneCommande.setEtat("servi");
        ligneCommande.setNbOccurences(1);
        ligneCommande.setIdCommande(commande.getId());
        ligneCommande.setIdPlat(plat.getId());
        orm.persisterNUplet(ligneCommande);


        //On simule le scénario de validation.
        CommandeControleur.validerService();
    }

    @Test
    @DisplayName("Test : Valider le service d'une commande - cas 4 : aucune commande trouvée dans la base")
    void testValiderServiceCas4PasTrouvees() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule le scénario de validation.
        CommandeControleur.validerService();
    }

    @Test
    @DisplayName("Test : Valider le service d'une commande - cas 5 : plat pas servi")
    void testValiderServiceCas5PlatPasPret() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(CommandeTest.class.getResourceAsStream("./saisies/commande_test/valider_service_cas_3.txt"));
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
        Plat plat1 = new Plat();
        plat1.setLibelle("plat");
        plat1.setCarte(1);
        plat1.setIdCategorie(categorie.getId());
        plat1.setPrix(1.5);
        orm.persisterNUplet(plat1);
        //On ajoute un plat à la carte du jour.
        Plat plat2 = new Plat();
        plat2.setLibelle("plat");
        plat2.setCarte(1);
        plat2.setIdCategorie(categorie.getId());
        plat2.setPrix(1.5);
        orm.persisterNUplet(plat2);

        //On ajoute un platIngredients car un plat est disponible uniquement
        // si la quantité utilisé par ce plat est inferieure ou égale aux stocks des ingrédients qui le compose.
        PlatIngredients platIngredient1 = new PlatIngredients();
        platIngredient1.setQuantite(2.0);
        platIngredient1.setIdPlat(1);
        platIngredient1.setIdIngredient(1);
        orm.persisterNUplet(platIngredient1);
        PlatIngredients platIngredient2 = new PlatIngredients();
        platIngredient2.setQuantite(2.0);
        platIngredient2.setIdPlat(2);
        platIngredient2.setIdIngredient(1);
        orm.persisterNUplet(platIngredient2);

        //On ajoute une commande
        Commande commande = new Commande();
        commande.setEtat("en attente");
        commande.setCoutTotal(1.5);
        commande.setDatetimeCreation(new Date());
        commande.setIdPlace(place.getId());
        orm.persisterNUplet(commande);

        //On ajoute une ligne commande
        LigneCommande ligneCommande1 = new LigneCommande();
        ligneCommande1.setEtat("servi");
        ligneCommande1.setNbOccurences(1);
        ligneCommande1.setIdCommande(commande.getId());
        ligneCommande1.setIdPlat(plat1.getId());
        orm.persisterNUplet(ligneCommande1);
        //On ajoute une ligne commande
        LigneCommande ligneCommande2 = new LigneCommande();
        ligneCommande2.setEtat("prêt");
        ligneCommande2.setNbOccurences(1);
        ligneCommande2.setIdCommande(commande.getId());
        ligneCommande2.setIdPlat(plat2.getId());
        orm.persisterNUplet(ligneCommande2);

        //On simule le scénario de validation.
        CommandeControleur.validerService();

    }
}