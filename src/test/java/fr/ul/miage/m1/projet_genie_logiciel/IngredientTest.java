package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Ingrédient")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @DisplayName("Test - ajouter un ingrédient - cas 1 : n-uplet ingrédient bien ajouté")
    void testAjouterIngredientCasBienAjoute() {
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
    @DisplayName("Test - ajouter un ingrédient - cas 2 : n-uplet ingrédient bien ajouté avec le bon libellé")
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
    @DisplayName("Test - ajouter un ingrédient - cas 3 : n-uplet ingrédient non ajouté ")
    void testAjouterIngredientCasPasDUnite() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/ajouter_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();

    }

    @Test
    @DisplayName("Test : modifier un ingrédient - cas 1 : n-uplet ingrédient bien trouvé")
    void testModifierIngredientCasBienTrouve() {
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
    @DisplayName("Test : modifier un ingrédient - cas 2 : n-uplet ingrédient non trouvé")
    void testModifierIngredientCasNonTrouve() {
        //on ajoute une unité.
        Unite unite = new Unite();
        unite.setLibelle("kg");
        orm.persisterNUplet(unite);

        //On crée et on ajoute un ingrédient.
        Ingredient ingredientsAvant = new Ingredient();
        ingredientsAvant.setLibelle("libellé ingredient");
        ingredientsAvant.setStock(1.4);
        ingredientsAvant.setIdUnite(1);
        orm.persisterNUplet(ingredientsAvant);
        orm.supprimerNUplet(ingredientsAvant);

        //On simule les saisies de modification dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        IngredientControleur.modifier();

        //Un ingrédient a du être inséré.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = " + null, Ingredient.class);
        assertNull(ingredientApres);
    }

    @Test
    @DisplayName("Test - lister les ingrédients")
    void testListerIngredient() {
        //On simule les saisies de lister dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/lister.txt"));
        ui.reinitialiserScanner();

        List<Entite> ingredientsAvant = orm.chercherTousLesNUplets(Ingredient.class);

        //On simule le scénario de lister.
        IngredientControleur.lister();

        //Un ingrédient a du être inséré.
        List<Entite> ingredientsApres = orm.chercherTousLesNUplets(Ingredient.class);
        assertEquals(ingredientsAvant, ingredientsApres);
    }

    @Test
    @DisplayName("Test : supprimer un ingrédient - cas 1 : n-uplet ingrédient bien supprimé")
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
    @DisplayName("Test - supprimer un ingrédient - cas 2 : aucun n-uplet ingrédient trouvé")
    void testSupprimerIngredientCas2PasDeUnite() {
        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        IngredientControleur.supprimer();
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 1 : n-uplet ingrédient non trouvé")
    void testModifierStockIngredientCasNontrouve(){
        //on ajoute une unité.
        Unite unite = new Unite();
        unite.setLibelle("kg");
        orm.persisterNUplet(unite);

        //On crée et on ajoute un ingrédient.
        Ingredient ingredientsAvant = new Ingredient();
        ingredientsAvant.setLibelle("libellé ingredient de stock à modifier");
        ingredientsAvant.setStock(2.4);
        ingredientsAvant.setIdUnite(1);
        orm.persisterNUplet(ingredientsAvant);
        orm.supprimerNUplet(ingredientsAvant);

        //On simule les saisies de modification de stock d'ingrédient dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_stock_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'incrémentation.
        IngredientControleur.incrementerStock();
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 2 : n-uplet ingrédient trouvé")
    void testModifierStockIngredientCastrouve(){
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

        //On simule les saisies de modification de stock d'ingrédient dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/modifier_stock_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'incrémentation.
        IngredientControleur.incrementerStock();
    }
}
