package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CategorieControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Catégorie")
public class CategorieTest {
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
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));
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
    @DisplayName("Test : lister les categories - cas 1 : categories trouvées")
    void testListerCas1Trouvees() {
        //On ajoute une categorie à lister.
        Categorie categorie1 = new Categorie();
        categorie1.setLibelle("libellé");
        orm.persisterNUplet(categorie1);

        //On simule le scénario de listing.
        CategorieControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les catégories - cas 2 : aucune catégorie trouvée")
    void testListerCas2PasTrouvees() {
        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter une catégorie - cas 1 : catégorie bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/categorie_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbCategoriesAvant = orm.compterTousLesNUplets(Categorie.class);

        //On simule le scénario d'ajout.
        CategorieControleur.ajouter();

        //Une unité a due être insérée.
        int nbCategoriesApres = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(nbCategoriesAvant + 1, nbCategoriesApres);
    }

    @Test
    @DisplayName("Test : ajouter une catégorie - cas 2 : catégorie bien ajoutée avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        CategorieControleur.ajouter();

        //La catégorie insérée doit avoir ce libellé : "libellé test ajouter".
        Categorie categorieInseree = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertEquals("libellé test ajouter", categorieInseree.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une catégorie - cas 1 : catégorie bien modifiée")
    void testModifierCas1BienModifiee() {
        //On ajoute une catégorie à modifier.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé");
        orm.persisterNUplet(categorie);

        //On simule les saisies de modification dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        CategorieControleur.modifier();

        //La catégorie modifiée doit avoir ce libellé : "libellé modifié".
        Categorie categorieModifie = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertEquals("libellé modifié", categorieModifie.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une catégorie - cas 2 : aucune catégorie trouvée")
    void testModifierCa2PasTrouvees() {
        //On simule les saisies de modification dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        CategorieControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer une catégorie - cas 1 : catégorie bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        //On ajoute une catégorie à supprimer.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé");
        orm.persisterNUplet(categorie);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbCategoriesAvant = orm.compterTousLesNUplets(Categorie.class);

        //On simule le scénario de suppression.
        CategorieControleur.supprimer();

        //Une catégorie a due être supprimée.
        int nbCategoriesApres = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(nbCategoriesAvant - 1, nbCategoriesApres);
    }

    @Test
    @DisplayName("Test : supprimer une catégorie - cas 2 : catégorie supprimée correcte")
    void testSupprimerCas2Correcte() {
        //On ajoute une catégorie à supprimer.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé");
        orm.persisterNUplet(categorie);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Catégorie existante avant.
        Categorie categorieAvant = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertNotNull(categorieAvant);

        //On simule le scénario de suppression.
        CategorieControleur.supprimer();

        //Catégorie suppirmée après.
        Categorie categorieApres = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertNull(categorieApres);
    }

    @Test
    @DisplayName("Test : supprimer une catégorie - cas 3 : aucune catégorie trouvée")
    void testSupprimerCas3PasTrouvees() {
        //On simule le scénario de suppression.
        CategorieControleur.supprimer();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponibles dans la carte du jour - cas 1 : catégories trouvés")
    void testListerDisponiblesCasTrouve() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On ajoute une catégorie.
        Categorie categorie1 = new Categorie();
        categorie1.setLibelle("catégorie1");
        orm.persisterNUplet(categorie1);

        //On ajoute une catégorie.
        Categorie categorie2 = new Categorie();
        categorie2.setLibelle("catégorie2");
        orm.persisterNUplet(categorie2);

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
        plat1.setLibelle("plat1");
        plat1.setCarte(1);
        plat1.setIdCategorie(categorie1.getId());
        plat1.setPrix(1.5);
        orm.persisterNUplet(plat1);
        //On ajoute un plat qui n'est pas dans la carte du jour.
        Plat plat2 = new Plat();
        plat2.setLibelle("plat");
        plat2.setCarte(0);
        plat2.setIdCategorie(categorie2.getId());
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

        //On simule le scénario de listing.
        CategorieControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponible dans la carte du jour - cas 1 : aucune catégorie trouvée pour la carte du jour")
    void testListerCas1AucuneDansCarte() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

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

        //On ajoute un plat disponible mais n'est pas dans la carte du jour.
        Plat plat = new Plat();
        plat.setLibelle("plat jour");
        plat.setCarte(0);
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

        //On vérifie que la catégorie a bien été ajouté.
        Categorie catégorieAjoutée =(Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Categorie.class);

        assertNotNull(catégorieAjoutée);
        //On simule le scénario de listing.
        CategorieControleur.listerDisponiblesCarte();
    }


    @Test
    @DisplayName("Test : lister les catégories des plats disponibles dans la carte - cas 2 : aucune catégorie trouvée dans la base")
    void testListerCas2AucuneTrouvee() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On simule le scénario de listing des catégories des plats disponibles dans la carte.
        CategorieControleur.listerDisponiblesCarte();
    }
}