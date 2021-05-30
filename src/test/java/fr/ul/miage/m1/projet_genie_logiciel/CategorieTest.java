package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CategorieControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Catégorie")
public class CategorieTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
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
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("Cuisine française");
        ajouterCategorie("Cuisine indienne");

        //On simule le scénario de listing.
        CategorieControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les catégories - cas 2 : aucune catégorie trouvée")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter une catégorie - cas 1 : catégorie bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/categorie_test/ajouter_cas_1.txt");

        int nbCategoriesAvant = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(0, nbCategoriesAvant);

        //On simule le scénario d'ajout.
        CategorieControleur.ajouter();

        //Une unité a due être insérée.
        int nbCategoriesApres = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(1, nbCategoriesApres);
    }

    @Test
    @DisplayName("Test : ajouter une catégorie - cas 2 : catégorie bien ajoutée avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule les saisies de l'ajout dans ce fichier.
        chargerSaisies("./saisies/categorie_test/ajouter_cas_2.txt");

        //On simule le scénario d'ajout.
        CategorieControleur.ajouter();

        //La catégorie insérée doit avoir ce libellé : "libellé test ajouter".
        Categorie categorieInseree = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertEquals("libellé test ajouter", categorieInseree.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une catégorie - cas 1 : catégorie bien modifiée")
    void testModifierCas1BienModifiee() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("à modifier");

        //On simule les saisies de modification dans ce fichier.
        chargerSaisies("./saisies/categorie_test/modifier_cas_1.txt");

        Categorie categorieAvant = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertEquals("à modifier", categorieAvant.getLibelle());

        //On simule le scénario de modification.
        CategorieControleur.modifier();

        //La catégorie modifiée doit avoir ce libellé : "libellé modifié".
        Categorie categorieApres = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Categorie.class);
        assertEquals("libellé modifié", categorieApres.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une catégorie - cas 2 : aucune catégorie trouvée")
    void testModifierCa2PasTrouvees() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule les saisies de modification dans ce fichier.
        chargerSaisies("./saisies/categorie_test/modifier_cas_1.txt");

        //On simule le scénario de modification.
        CategorieControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer une catégorie - cas 1 : catégorie bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("à supprimer");

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/categorie_test/supprimer_cas_1.txt");

        int nbCategoriesAvant = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(1, nbCategoriesAvant);

        //On simule le scénario de suppression.
        CategorieControleur.supprimer();

        //Une catégorie a due être supprimée.
        int nbCategoriesApres = orm.compterTousLesNUplets(Categorie.class);
        assertEquals(0, nbCategoriesApres);
    }

    @Test
    @DisplayName("Test : supprimer une catégorie - cas 2 : catégorie supprimée correcte")
    void testSupprimerCas2Correcte() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("à supprimer - autre");

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/categorie_test/supprimer_cas_2.txt");

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
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario de suppression.
        CategorieControleur.supprimer();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponibles dans la carte du jour - cas 1 : catégories trouvées")
    void testListerDisponiblesCas1Trouve() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie 1");
        ajouterCategorie("catégorie 2");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 20.0, 1);
        ajouterPlat("plat 1", 1.5, 1, 1);
        ajouterPlatIngredient(1.0, 1, 1);
        ajouterPlat("plat 2", 1.5, 0, 2);
        ajouterPlatIngredient(3.5, 2, 1);

        //On simule le scénario de listing.
        CategorieControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponibles dans la carte du jour - cas 2 : catégories pas trouvées - stocks insuffisants")
    void testListerDisponiblesCas2PasTrouveStockInsuffisant() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 2, 1);
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlatIngredient(5, 1, 1);

        //On simule le scénario de listing.
        CategorieControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponible dans la carte du jour - cas 3 : aucune catégorie trouvée - carte du jour vide")
    void testListerCas3PasTrouveCarteVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 20.0, 1);
        ajouterPlat("plat", 1.5, 0, 1);
        ajouterPlatIngredient(1.0, 1, 1);

        //On simule le scénario de listing.
        CategorieControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les catégories des plats disponibles dans la carte - cas 4 : aucune catégorie trouvée - categorie vide ")
    void testListerCas4PasTrouveCategorieVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario de listing des catégories des plats disponibles dans la carte.
        CategorieControleur.listerDisponiblesCarte();
    }
}