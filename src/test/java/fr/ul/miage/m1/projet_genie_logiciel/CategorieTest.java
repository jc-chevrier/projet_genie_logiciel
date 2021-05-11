package fr.ul.miage.m1.projet_genie_logiciel;


import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CategorieControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Categorie")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategorieTest {
    private static ORM orm;
    private static UI ui;

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();

        ui = UI.getInstance();
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));
    }
    @Test
    @Order(1)
    @DisplayName("Test : lister les categories - cas 1 : categories trouvées")
    void testListerCas1Trouvees() {
        //On ajoute une categorie à lister.
        Categorie categorie1 = new Categorie();
        categorie1.setLibelle("libellé");
        orm.persisterNUplet(categorie1);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/categorie_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        CategorieControleur.lister();
    }
    @Test
    @Order(2)
    @DisplayName("Test : lister les catégories - cas 2 : aucune catégorie trouvée")
    void testListerCas2PasTrouvees() {
        //On vide la table catégorie.
        orm.chercherTousLesNUplets(Categorie.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        UniteControleur.lister();
    }
    @Test
    @Order(3)
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
    @Order(4)
    @DisplayName("Test : ajouter une catégorie - cas 2 : catégorie bien ajoutée avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(CategorieTest.class.getResourceAsStream("./saisies/categorie_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        CategorieControleur.ajouter();

        //La catégorie insérée doit avoir ce libellé : "libellé test ajouter".
        Categorie categorieInseree = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Categorie.class);
        assertEquals("libellé test ajouter", categorieInseree.getLibelle());
    }
}