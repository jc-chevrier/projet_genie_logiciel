package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientTest {
    private static ORM orm;
    private static UI ui;

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();

        ui = UI.getInstance();
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));
    }

    @Test
    @Order(4)
    @DisplayName("Test - supprimer un ingrédient - cas 1 : n-uplet ingrédient bien supprimé")
    void testSupprimerIngrédientCas1Supprime() {
        Unite unite = new Unite();
        unite.setLibelle("unite");
        orm.persisterNUplet(unite);
        Ingredient ingredient = new Ingredient();
        ingredient.setLibelle("libellé ingrédient 3");
        ingredient.setIdUnite(1);
        ingredient.setStock(0.0);
        orm.persisterNUplet(ingredient);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        //Unité existant avant.
        Ingredient ingredientAvant = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Ingredient.class);
        assertNotNull(ingredientAvant);

        //On simule le scénario de suppression.
        IngredientControleur.supprimer();

        //Ingrédient suppirmée après.
        Ingredient ingredientApres = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Ingredient.class);
        assertNull(ingredientApres);
    }

    @Test
    @Order(5)
    @DisplayName("Test - supprimer un ingrédient - cas 2 : aucun n-uplet ingrédient trouvé")
    void testSupprimerIngredientCas2PasDeUnite() {
        //On vide la table ingrédient.
        orm.chercherTousLesNUplets(Ingredient.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        IngredientControleur.supprimer();
    }
}
