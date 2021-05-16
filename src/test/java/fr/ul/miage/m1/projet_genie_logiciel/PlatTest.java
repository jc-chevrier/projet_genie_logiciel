package fr.ul.miage.m1.projet_genie_logiciel;


import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Plat")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlatTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables(){
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
    @DisplayName("Test : lister les plats - cas 1 : plats trouvés")
    void testListerCas1Trouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat1 = new Plat();
        plat1.setLibelle("libellé");
        plat1.setCarte(0);
        plat1.setPrix(2.5);
        plat1.setIdCategorie(1);
        orm.persisterNUplet(plat1);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les plats - cas 2 : aucun plat trouvé")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 1 : plat bien supprimé")
    void testSupprimerCas1BienSupprimee() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterTousLesNUplets(Plat.class);

        //On simule le scénario de suppression.
       PlatControleur.supprimer();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterTousLesNUplets(Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 2 : plat supprimé correct")
    void testSupprimerCas2Correct() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Plat existant avant.
        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertNotNull(platAvant);

        //On simule le scénario de suppression.
        PlatControleur.supprimer();

        //Plat suppirmé après.
        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertNull(platApres);
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 3 : aucun plat trouvé")
    void testSupprimerCas3PasTrouves() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        PlatControleur.supprimer();
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 1 : plat bien ajouté")
    void testAjouterACarteCas1BienValide() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
        assertEquals(nbPlatsDansCarteAvant + 1, nbPlatsDansCarteApres);
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 2 : plat ajouté correct")
    void testAjouterACarteCas2Correct() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_2.txt"));
        ui.reinitialiserScanner();

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(0, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(1, platApres.getCarte());
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 3 : aucun plat à ajouter trouvée")
    void testAjouterACarteCas3() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);
        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à la carte.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);


        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour - cas 4 : aucun plat trouvée")
    void testAjouterACarteCas4PasTrouves() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }

    @Test
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 1 : plat bien supprimé")
    void testSupprimerDeCarteCas1BienValide() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        int nbPlatsDansCarteAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        int nbPlatsDansCarteApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
        assertEquals(nbPlatsDansCarteAvant + 1, nbPlatsDansCarteApres);
    }

    @Test
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 2 : plat supprimé correct")
    void testSupprimerDeCarteCas2Correct() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_2.txt"));
        ui.reinitialiserScanner();

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(1, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();

        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(0, platApres.getCarte());
    }

    @Test
    @DisplayName("Test : supprimer un plat à la carte du jour- cas 3 : aucun plat à supprimer trouvé")
    void testSupprimerDeCarteCas3() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à la carte.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }

    @Test
    @DisplayName("Test : supprimer un plat de la carte du jour - cas 4 : aucun plat trouvé")
    void testSupprimerDeCarteCas4PasTrouves() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }
    @Test
    @DisplayName("Test : ajouter un plat - cas 1 : plat bien ajouté")
    void testAjouterCas1BienAjoute() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute une unité pour pouvoir ajouter un ingredient.
        Unite unite = new Unite();
        unite.setLibelle("libellé1");
        orm.persisterNUplet(unite);
        //On ajoute un ingrédient pour pouvoir composer un plat.
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setLibelle("ingredient1");
        ingredient1.setIdUnite(1);
        ingredient1.setStock(5.0);
        orm.persisterNUplet(ingredient1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setLibelle("ingredient2");
        ingredient2.setStock(5.0);
        ingredient2.setIdUnite(1);
        orm.persisterNUplet(ingredient2);


        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterTousLesNUplets(Plat.class);

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();

        //Un plat a du être inséré.
        int nbPlatsApres = orm.compterTousLesNUplets(Plat.class);
        assertEquals(nbPlatsAvant + 1, nbPlatsApres);
    }
    @Test
    @DisplayName("Test : ajouter un plat - cas 2 : plat bien ajouté avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute une unité pour pouvoir ajouter un ingredient.
        Unite unite = new Unite();
        unite.setLibelle("libellé1");
        orm.persisterNUplet(unite);
        //On ajoute un ingrédient pour pouvoir composer un plat.
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setLibelle("ingredient1");
        ingredient1.setIdUnite(1);
        ingredient1.setStock(5.0);
        orm.persisterNUplet(ingredient1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setLibelle("ingredient2");
        ingredient2.setStock(5.0);
        ingredient2.setIdUnite(1);
        orm.persisterNUplet(ingredient2);

        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();

        //Le plat inséré doit avoir ce libellé : "libellé test ajouter".
        Plat platInsere = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals("libellé test ajouter", platInsere.getlibelle());
    }
    @Test
    @DisplayName("Test : ajouter un plat - cas 3 : pas d'ingrédient")
    void testAjouterCas3() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);

        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();
    }
    @Test
    @DisplayName("Test : ajouter un plat - cas 3 : pas de catégorie")
    void testAjouterCas4() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));
        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);
        //On vide la table Catégorie.
        orm.chercherTousLesNUplets(Categorie.class).forEach(orm::supprimerNUplet);

        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();
    }
    @Test
    @DisplayName("Test : modifier un plat - cas 1 : plat bien modifié")
    void testAjouterCas1() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute une unité pour pouvoir ajouter un ingredient.
        Unite unite = new Unite();
        unite.setLibelle("libellé1");
        orm.persisterNUplet(unite);
        //On ajoute un ingrédient pour pouvoir composer un plat.
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setLibelle("ingredient1");
        ingredient1.setIdUnite(1);
        ingredient1.setStock(5.0);
        orm.persisterNUplet(ingredient1);
        //On ajoute un plat
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setPrix(3.0);
        plat.setCarte(1);
        plat.setIdCategorie(categorie.getId());
        orm.persisterNUplet(plat);
        PlatIngredients platIngredients = new PlatIngredients();
        platIngredients.setIdIngredient(ingredient1.getId());
        platIngredients.setIdPlat(plat.getId());
        platIngredients.setQuantite(2.0);
        orm.persisterNUplet(platIngredients);

        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        PlatControleur.modifier();

        //Le plat modifié doit avoir ce libellé : "libellé modifié".
        Plat platModifie = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals("libellé modifié", platModifie.getlibelle());
    }
    @Test
    @DisplayName("Test : modifier un plat - cas 2 : pas de plat trouvé")
    void testAjouterCas2() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On simule les saisies de modification dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/modifier_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        PlatControleur.modifier();
    }

    @Test
    @DisplayName("Test : lister les plats de la carte - cas 1 : plats trouvés")
    void testListerCarteCasTrouve() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("cat 1");
        orm.persisterNUplet(categorie);

        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("plat jour");
        plat.setCarte(1);
        plat.setIdCategorie(categorie.getId());
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_carte_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de lister.
        PlatControleur.listerCarte();

    }

    @Test
    @DisplayName("Test : lister les plats de la carte - cas 1 : aucun plat dans la carte")
    void testListerCarteCas2AucunDansCarte() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("cat 2");
        orm.persisterNUplet(categorie);

        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("plat jour");
        plat.setCarte(0);
        plat.setIdCategorie(categorie.getId());
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_carte_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de lister.
        PlatControleur.listerCarte();

    }
    @Test
    @DisplayName("Test : lister les plats de la carte - cas 1 : aucun plat trouvé")
    void testListerCarteCas3AucunPlatTrouve() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_carte_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de lister.
        PlatControleur.listerCarte();

    }

}
