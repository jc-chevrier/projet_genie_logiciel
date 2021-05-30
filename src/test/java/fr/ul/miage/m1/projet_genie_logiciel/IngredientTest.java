package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ingrédient")
public class IngredientTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables(){
        //On réinitialise la table ingrédient.
        orm.reinitialiserTable(Ingredient.class);
        //On réinitialise la table unité.
        orm.reinitialiserTable(Unite.class);
    }

    @BeforeAll
    static void faireAvantTousLesTests() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();
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
    @DisplayName("Test - lister les ingrédients : cas 1 - ingrédients trouvés")
    void testListerIngredientTrouve() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");
        ajouterIngredient("ingrédient 1", 77, 3);
        ajouterIngredient("ingrédient 2", 99, 2);

        //On simule le scénario de listing.
        IngredientControleur.lister();
    }

    @Test
    @DisplayName("Test - lister les ingrédients : cas 2 - aucun ingrédient trouvé")
    void testListerIngredientCas2PasTrouve() {
        //On simule le scénario de listing.
        IngredientControleur.lister();
    }

    @Test
    @DisplayName("Test - ajouter un ingrédient - cas 1 : ingrédient bien ajouté")
    void testAjouterIngredientCas1BienAjoute() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");

        //On simule les saisies de l'ajout dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/ajouter_cas_1.txt");

        List<Entite> ingredientAvant = orm.chercherTousLesNUplets(Ingredient.class);
        assertEquals(0, ingredientAvant.size());

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();

        //Un ingrédient a due être inséré.
        List<Entite> ingredientsApres = orm.chercherTousLesNUplets(Ingredient.class);
        assertEquals(1, ingredientsApres.size());
    }

    @Test
    @DisplayName("Test - ajouter un ingrédient - cas 2 : ingrédient bien ajouté avec le bon libellé")
    void testAjouterIngredientCas2BonLibelle() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");

        //On simule les saisies de l'ajout dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/ajouter_cas_2.txt");

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();

        //Un ingrédient a du être inséré.
        Ingredient ingredientInsere = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals("ajouté", ingredientInsere.getLibelle());
        assertEquals(2, ingredientInsere.getIdUnite());
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
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");
        ajouterIngredient("à modifier", 0.0, 1);

        //On simule les saisies de modification dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/modifier_cas_1.txt");

        //On simule le scénario de modification.
        IngredientControleur.modifier();

        //Un ingrédient a du être modifié.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals("libellé modifié", ingredientApres.getLibelle());
        assertEquals(2, ingredientApres.getIdUnite());
    }

    @Test
    @DisplayName("Test : modifier un ingrédient - cas 2 : aucun ingrédient trouvé")
    void testModifierIngredientCas3PasTrouve() {
        //On simule le scénario de modification.
        IngredientControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer un ingrédient - cas 1 : ingrédient bien supprimé")
    void testSupprimerIngredientCas1Supprime() {
        ajouterUnite("unité");
        ajouterIngredient("à supprimer", 0.0, 1);

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/supprimer_cas_1.txt");

        Ingredient ingredientAvant = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertNotNull(ingredientAvant);

        //On simule le scénario de suppression.
        IngredientControleur.supprimer();

        //Un ingrédient a du être supprimé.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertNull(ingredientApres);
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
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 2.4, 1);

        //On simule les saisies de modification de stock dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/modifier_stock_cas_1.txt");

        //On simule le scénario de modification de stock.
        IngredientControleur.modifierStock();

        //Le stock de l'ingrédient a du être incrémenté.
        Ingredient ingredientsApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Ingredient.class);
        assertEquals(3.9, ingredientsApres.getStock());
    }

    @Test
    @DisplayName("Test : modifier stock d'un ingrédient - cas 1 : stock décrémenté")
    void testModifierStockIngredientCas2Decremente() {
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 5.4, 1);

        //On simule les saisies de modification de stock dans ce fichier.
        chargerSaisies("./saisies/ingredient_test/modifier_stock_cas_2.txt");

        //On simule le scénario de modification de stock.
        IngredientControleur.modifierStock();

        //Le stock de l'ingrédient a du être décrémenté.
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