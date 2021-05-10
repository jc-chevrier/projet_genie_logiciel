package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    @Order(5)
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
    @Order(6)
    @DisplayName("Test : modifier un ingrédient - cas 1 : n-uplet ingrédient non trouvé")
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

}
