package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @Order(1)
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

}
