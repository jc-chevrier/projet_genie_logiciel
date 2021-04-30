package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Ingredient;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ingrédient")
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
    @Order(1)
    @DisplayName("Test - ajouter un ingédient - cas 1 : n-uplet ingrédient bien ajouté")
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
    @Order(2)
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
        Ingredient ingredientInsere = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Ingredient.class);
        assertEquals("libellé test 1 ajouter cas 2 bien libellé", ingredientInsere.getLibelle());
    }

    @Test
    @Order(3)
    @DisplayName("Test - ajouter un ingrédient - cas 3 : n-uplet ingrédient non ajouté ")
    void testAjouterIngredientCasPasDUnite() {
        //On vide la table unités.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/ingredient_test/ajouter_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        IngredientControleur.ajouter();
    }
}
