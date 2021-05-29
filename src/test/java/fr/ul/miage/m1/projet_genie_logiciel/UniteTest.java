package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unité")
public class UniteTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
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
    @DisplayName("Test : lister les unités - cas 1 : unités trouvées")
    void testListerCas1Trouvees() {
        ajouterUnite("kg");

        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les unités - cas 2 : aucune unité trouvée")
    void testListerCas2PasTrouvees() {
        //On simule le scénario de listing.
        UniteControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter une unité - cas 1 : unité bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/unite_test/ajouter_cas_1.txt");

        int nbUnitesAvant = orm.compterTousLesNUplets(Unite.class);

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //Une unité a due être insérée.
        int nbUnitesApres = orm.compterTousLesNUplets(Unite.class);
        assertEquals(nbUnitesAvant + 1, nbUnitesApres);
    }

    @Test
    @DisplayName("Test : ajouter une unité - cas 2 : unité bien ajoutée avec bon libellé")
    void testAjouterCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        chargerSaisies("./saisies/unite_test/ajouter_cas_2.txt");

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //L'unité insérée doit avoir ce libellé : "libellé test ajouter".
        Unite uniteInseree = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertEquals("libellé test ajouter", uniteInseree.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une unité - cas 1 : unité bien modifié")
    void testModifierCas1BienModifiee() {
        ajouterUnite("à modifier");

        //On simule les saisies de modification dans ce fichier.
        chargerSaisies("./saisies/unite_test/modifier_cas_1.txt");

        //On simule le scénario de modification.
        UniteControleur.modifier();

        //L'unité modifiée doit avoir ce libellé : "libellé modifié".
        Unite uniteModifie = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertEquals("libellé modifié", uniteModifie.getLibelle());
    }

    @Test
    @DisplayName("Test : modifier une unité - cas 2 : aucune unité trouvé")
    void testModifierCa2PasTrouvees() {
        //On simule le scénario de modification.
        UniteControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer une unité - cas 1 : unité bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        ajouterUnite("à supprimer");

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/unite_test/supprimer_cas_1.txt");

        int nbUnitesAvant = orm.compterTousLesNUplets(Unite.class);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Une unité a due être supprimée.
        int nbUnitesApres = orm.compterTousLesNUplets(Unite.class);
        assertEquals(nbUnitesAvant - 1, nbUnitesApres);
    }

    @Test
    @DisplayName("Test : supprimer une unité - cas 2 : unité supprimée correcte")
    void testSupprimerCas2Correcte() {
        ajouterUnite("à supprimer - autre");

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/unite_test/supprimer_cas_2.txt");

        //Unité existant avant.
        Unite uniteAvant = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNotNull(uniteAvant);

        //On simule le scénario de suppression.
        UniteControleur.supprimer();

        //Unité suppirmée après.
        Unite uniteApres = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNull(uniteApres);
    }

    @Test
    @DisplayName("Test : supprimer une unité - cas 3 : aucune unité trouvée")
    void testSupprimerCas3PasTrouvees() {
        //On simule le scénario de suppression.
        UniteControleur.supprimer();
    }
}