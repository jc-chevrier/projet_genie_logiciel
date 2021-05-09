package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniteTest {
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
    @DisplayName("Test - lister les unités - cas 1 : n-uplets unité trouvés")
    void testListerCas1Trouve() {
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @Order(2)
    @DisplayName("Test - lister les unités - cas 2 : aucun n-uplet unité trouvé")
    void testListerCas2PasTrouve() {
        //On vide la table unité.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @Order(3)
    @DisplayName("Test - ajouter une unité - cas 1 : n-uplet unité bien ajouté")
    void testAjouterCas1BienAjoute() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

       int nbUnitesAvant = orm.compterTousLesNUplets(Unite.class);

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //Une unité a due être insérée.
        int nbUnitesApres = orm.compterTousLesNUplets(Unite.class);
        assertEquals(nbUnitesAvant + 1, nbUnitesApres);
    }

    @Test
    @Order(4)
    @DisplayName("Test - ajouter une unité - cas 2 : n-uplet unité bien ajouté avec bon libellé")
    void testAjouterUniteCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //L'unité insérée doit avoir ce libellé : "libellé test ajouter cas 2 bon libellé".
        Unite uniteInseree = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Unite.class);
        assertEquals("libellé test ajouter", uniteInseree.getLibelle());
    }

    @Test
    @Order(6)
    @DisplayName("Test - supprimer une unité - cas 1 : n-uplet unité bien supprimé")
    void testSupprimerCas1BienSupprime() {
        Unite unite = new Unite();
        unite.setLibelle("libellé");
        orm.persisterNUplet(unite);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbUnitesAvant = orm.compterTousLesNUplets(Unite.class);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Une unité a due être supprimée.
        int nbUnitesApres = orm.compterTousLesNUplets(Unite.class);
        assertEquals(nbUnitesAvant - 1, nbUnitesApres);
    }

    @Test
    @Order(7)
    @DisplayName("Test - supprimer une unité - cas 2 : n-uplet unité supprimé correct")
    void testSupprimerCas1Correct() {
        Unite unite = new Unite();
        unite.setLibelle("libellé");
        orm.persisterNUplet(unite);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Unité existant avant.
        Unite uniteAvant = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Unite.class);
        assertNotNull(uniteAvant);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Unité suppirmée après.
        Unite uniteApres = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Unite.class);
        assertNull(uniteApres);
    }

    @Test
    @Order(8)
    @DisplayName("Test - supprimer une unité - cas 3 : aucun n-uplet unité trouvé à supprimer")
    void testSupprimerCas2PasTrouve() {
        //On vide la table unité.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        UniteControleur.supprimer();
    }
}