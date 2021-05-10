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
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));
    }

    @Test
    @Order(1)
    @DisplayName("Test : lister les unités - cas 1 : unités trouvées")
    void testListerCas1Trouvees() {
        //On ajoute une unité à lister.
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
    @DisplayName("Test : lister les unités - cas 2 : aucune unité trouvée")
    void testListerCas2PasTrouvees() {
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
    @DisplayName("Test : ajouter une unité - cas 1 : unité bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On simule les saisies d'ajout dans ce fichier.
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
    @DisplayName("Test : ajouter une unité - cas 2 : unité bien ajoutée avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //L'unité insérée doit avoir ce libellé : "libellé test ajouter".
        Unite uniteInseree = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Unite.class);
        assertEquals("libellé test ajouter", uniteInseree.getLibelle());
    }

    @Test
    @Order(5)
    @DisplayName("Test : modifier une unité - cas 1 : unité bien modifié")
    void testModifierCas1BienModifiee() {
        //On ajoute une unité à modifier.
        Unite unite = new Unite();
        unite.setLibelle("libellé");
        orm.persisterNUplet(unite);

        //On simule les saisies de modification dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        UniteControleur.modifier();

        //L'unité modifiée doit avoir ce libellé : "libellé modifié".
        Unite uniteModifie = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Unite.class);
        assertEquals("libellé modifié", uniteModifie.getLibelle());
    }

    @Test
    @Order(6)
    @DisplayName("Test : modifier une unité - cas 2 : aucune unité trouvé")
    void testModifierCa2PasTrouvees() {
        //On vide la table unité.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de modification dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/modifier_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de modification.
        UniteControleur.modifier();
    }

    @Test
    @Order(7)
    @DisplayName("Test : supprimer une unité - cas 1 : unité bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        //On ajoute une unité à supprimer.
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
    @Order(8)
    @DisplayName("Test : supprimer une unité - cas 2 : unité supprimée correcte")
    void testSupprimerCas2Correcte() {
        //On ajoute une unité à supprimer.
        Unite unite = new Unite();
        unite.setLibelle("libellé");
        orm.persisterNUplet(unite);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Unité existant avant.
        Unite uniteAvant = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Unite.class);
        assertNotNull(uniteAvant);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Unité suppirmée après.
        Unite uniteApres = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Unite.class);
        assertNull(uniteApres);
    }

    @Test
    @Order(9)
    @DisplayName("Test : supprimer une unité - cas 3 : aucune unité trouvée")
    void testSupprimerCas3PasTrouvees() {
        //On vide la table unité.
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(UniteTest.class.getResourceAsStream("./saisies/unite_test/supprimer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        UniteControleur.supprimer();
    }
}