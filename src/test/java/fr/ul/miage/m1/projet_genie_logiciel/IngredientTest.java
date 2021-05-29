package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ingrédient")
public class IngredientTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables(){
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
    @DisplayName("Test - lister les ingrédients")
    void testListerIngredient() {
        List<Entite> ingredientsAvant = orm.chercherTousLesNUplets(Ingredient.class);

        //On simule le scénario de lister.
        IngredientControleur.lister();

        //Un ingrédient a du être inséré.
        List<Entite> ingredientsApres = orm.chercherTousLesNUplets(Ingredient.class);
        assertEquals(ingredientsAvant, ingredientsApres);
    }

    @Test
    @DisplayName("Test - ajouter un ingrédient - cas 1 : ingrédient bien ajouté")
    void testAjouterIngredientCas1BienAjoute() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        //Ajout d'unité avant l'ajout d'ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("ajoutUnite");
        orm.persisterNUplet(unite);

        List<Entite> ingredientAvant = orm.chercherTousLesNUplets(Ingredient.class);

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();

        //Un ingrédient a due être inséré.
        List<Entite> ingredientsApres = orm.chercherTousLesNUplets(Ingredient.class);
        assertEquals(ingredientAvant.size() + 1, ingredientsApres.size());
    }

    @Test
    @DisplayName("Test - ajouter un ingrédient - cas 2 : ingrédient bien ajouté avec le bon libellé")
    void testAjouterIngredientCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //Ajout d'unité avant l'ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("ajoutUnitee");
        orm.persisterNUplet(unite);

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();

        //L'ingédient inséré doit avoir ce libellé : "libellé test 1 ajouter cas 2 bien libellé".
        Ingredient ingredientInsere = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals("libellé test 1 ajouter cas 2 bien libellé", ingredientInsere.getLibelle());
    }

    @Test
    @DisplayName("Test - ajouter un ingrédient - cas 3 : aucune unité trouvée")
    void testAjouterIngredientCas3PasDUnite() {
        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();
    }

    @Test
    @DisplayName("Test : modifier un ingrédient - cas 1 : ingrédient bien trouvé")
    void testModifierIngredientCas1BienTrouve() {
        //On ajoute une unité.
        Unite unite = new Unite();
        unite.setLibelle("kg");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient.
        Ingredient ingredientsAvant = new Ingredient();
        ingredientsAvant.setLibelle("libellé ingredient");
        ingredientsAvant.setStock(2.4);
        ingredientsAvant.setIdUnite(1);
        orm.persisterNUplet(ingredientsAvant);

        //On simule les saisies de modification dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        IngredientControleur.modifier();

        //Un ingrédient a du être inséré.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Ingredient.class);
        assertNotEquals(ingredientsAvant.getLibelle(), ingredientApres.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier un ingrédient - cas 2 : aucun ingrédient trouvé")
    void testModifierIngredientCas3PasTrouve() {
        //On simule le scénario de modification.
        IngredientControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer un ingrédient - cas 1 : ingrédient bien supprimé")
    void testSupprimerIngrédientCas1Supprime() {
        //Ajouter un unité pour pouvoir ajouter un ingrédient.
        Unite unite = new Unite();
        unite.setLibelle("unite");
        orm.persisterNUplet(unite);
        //Ajouter un ingrédient.
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle("libellé ingrédient 3");
        ingredient.setIdUnite(1);
        ingredient.setStock(0.0);
        orm.persisterNUplet(ingredient);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        //Ingrédient existant avant.
        Ingredient ingredientAvant = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertNotNull(ingredientAvant);

        //On simule le scénario de suppression.
        IngredientControleur.supprimer();

        //Ingrédient suppirmée après.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
    }

    @Test
    @DisplayName("Test - supprimer un ingrédient - cas 2 : aucun ingrédient trouvé")
    void testSupprimerIngredientCas2PasTrouve() {
        //On simule le scénario de suppression.
        IngredientControleur.supprimer();
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 1 : stock incrémenté")
    void testModifierStockIngredientCas1Incremente(){
        //on ajoute une unité.
        Unite unite = new Unite();
        unite.setLibelle("kg");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient.
        Ingredient ingredientsAvant = new Ingredient();
        ingredientsAvant.setLibelle("libellé ingredient de stock à modifier");
        ingredientsAvant.setStock(2.4);
        ingredientsAvant.setIdUnite(1);
        orm.persisterNUplet(ingredientsAvant);

        //On simule les saisies de modification de stock dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_stock_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification de stock.
        IngredientControleur.modifierStock();

        Ingredient ingredientsApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals(3.9, ingredientsApres.getStock());
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 1 : stock décrémenté")
    void testModifierStockIngredientCas2Decremente() {
        //on ajoute une unité.
        Unite unite = new Unite();
        unite.setLibelle("L");
        orm.persisterNUplet(unite);

        //On ajoute un ingrédient.
        Ingredient ingredientsAvant = new Ingredient();
        ingredientsAvant.setLibelle("libellé ingredient de stock à modifier");
        ingredientsAvant.setStock(5.4);
        ingredientsAvant.setIdUnite(1);
        orm.persisterNUplet(ingredientsAvant);

        //On simule les saisies de modification de stock dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_stock_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification de stock.
        IngredientControleur.modifierStock();

        Ingredient ingredientsApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals(2.4, ingredientsApres.getStock());
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 3 : aucun ingrédient trouvé")
    void testModifierStockIngredientCas3PasTrouve() {
        //On simule le scénario de modification du stock.
        IngredientControleur.modifierStock();
    }
}