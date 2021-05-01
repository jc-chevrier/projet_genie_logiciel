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
    @DisplayName("Test - ajouter une unité - cas 1 : n-uplet unité bien ajoutée")
    void testAjouterUniteCasBienAjoutee() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        List<Entite> unitesAvant = orm.chercherTousLesNUplets(Unite.class);

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //Une unité a due être insérée.
        List<Entite> unitesApres = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(unitesAvant.size() + 1, unitesApres.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test - ajouter une unité - cas 2 : n-uplet unité bien ajoutée avec bon libellé")
    void testAjouterUniteCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //L'unité insérée doit avoir ce libellé : "libellé test ajouter cas 2 bon libellé".
        Unite uniteInseree = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Unite.class);
        assertEquals("libellé test ajouter cas 2 bon libellé", uniteInseree.getLibelle());
    }

    @Test
    @Order(3)
    @DisplayName("Test - supprimer une unité - cas 1 : n-uplet unité bien supprimée")
    void testSupprimerUniteCas1Supprimee() {
        Unite unite3 = new Unite();
        unite3.setLibelle("libellé unité 3");
        orm.persisterNUplet(unite3);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        //Unité existant avant.
        Unite uniteAvant = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Unite.class);
        assertNotNull(uniteAvant);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Unité suppirmée après.
        Unite uniteApres = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Unite.class);
        assertNull(uniteApres);
    }

    @Test
    @Order(4)
    @DisplayName("Test - supprimer une unité - cas 2 : aucun n-uplet unité trouvé")
    void testSupprimerUniteCas2PasDeUnite() {
        //On vide la table unité.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        UniteControleur.supprimer();
    }
}
