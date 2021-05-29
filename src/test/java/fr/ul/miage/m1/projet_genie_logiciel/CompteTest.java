package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CompteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Compte")
public class CompteTest extends GlobalTest {
    /**
     * Réinitialiser les comptes référentiels du jeu
     * de données.
     */
    static void reinitialiserComptesReferenciels() {
        ajouterCompte("LAURENT", "Victoria", 1, 1);
        ajouterCompte("DURAND", "Oliviver", 1, 2);
        ajouterCompte("CARON", "Jules", 1, 3);
        ajouterCompte("DUPONT", "Theo", 1, 4);
        ajouterCompte("ANDERSON", "Christa", 1, 5);
    }

    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
        //On réinitialise la table compte.
        orm.reinitialiserTable(Compte.class);
    }

    @BeforeAll
    static void faireAvantTousLesTests() {
        seConnecterEnDirecteur();
    }

    @BeforeEach
    void faireAvantChaqueTest() {
        reinitialiserTables();
    }

    @AfterAll
    static void faireApresTousLesTests() {
        reinitialiserTables();
        reinitialiserComptesReferenciels();
    }

    @Test
    @DisplayName("Test : lister les salariés - cas 1 : salariés trouvés")
    void testListerCas1Trouve() {
        ajouterCompte("Loan", "Joe", 1, 4);

        //On simule le scénario de listing des salariés.
        CompteControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les salariés - cas 2 : aucun salarié trouvé")
    void testListerCas2PasTrouve() {
        //On simule le scénario de listing des salariés.
        CompteControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter un salarié - cas 1 : salarié bien ajouté")
    void testAjouterCas1BienAjoute() {
        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/compte_test/ajouter_cas_1.txt");

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        //Un salarie a du être ajouté.
        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNotNull(compte);
    }

    @Test
    @DisplayName("Test : ajouter un salarié - cas 2 : salarié ajouté correct")
    void testAjouterCas2AjouteCorrect() {
        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/compte_test/ajouter_cas_2.txt");

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNotNull(compte);

        //Un salarie a du être ajouté.
        assertEquals("Porot", compte.getNom());
        assertEquals("Olivier", compte.getPrenom());
        assertEquals(1, compte.getIdRole());
        assertEquals(1, compte.getActif());
    }

    @Test
    @DisplayName("Test : modifier les informations d'un salarié - cas 1 : salarié modifié")
    void testModifierCas1Modifie() {
        ajouterCompte("Clondz", "Lisa", 1, 2);

        //On simule les saisies de modification dans ce fichier.
        chargerSaisies("./saisies/compte_test/modifier_cas_1.txt");

        //On simule le scénario de modification d'un salarié.
        CompteControleur.modifier();

        //Le salarié a du être modifié.
        Compte compteApres = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertEquals("Clond", compteApres.getNom());
        assertEquals("Lise", compteApres.getPrenom());
        assertEquals(3, compteApres.getIdRole());
        assertEquals(1, compteApres.getActif());
    }

    @Test
    @DisplayName("Test : modifier les informations d'un salarié - cas 2 : aucun salarié trouvé")
    void testModifierCas2pasTrouve() {
        //On simule le scénario de modification d'un salarié.
        CompteControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer un salarié - cas 1 : salarié bien supprimé")
    void testSupprimerCas1BienSupprime() {
        ajouterCompte("Fromtz", "Thomas", 1, 1);

        //On simule les saisies de suppression dans ce fichier.
        chargerSaisies("./saisies/compte_test/supprimer_cas_1.txt");

        int nbComptesAvant = orm.compterNUpletsAvecPredicat("WHERE actif = 1", Compte.class);
        assertEquals(1, nbComptesAvant);

        //On simule le scénario de suppression d'un salarié.
        CompteControleur.supprimer();

        //Le salarié a du être passé à inactif.
        int nbComptesApres =  orm.compterNUpletsAvecPredicat("WHERE actif = 1", Compte.class);
        assertEquals(0, nbComptesApres);
        nbComptesApres =  orm.compterNUpletsAvecPredicat("WHERE actif = 0", Compte.class);
        assertEquals(1, nbComptesApres);
    }


    @Test
    @DisplayName("Test : supprimer un salarié - cas 2 : salarié supprimé correct")
    void testSupprimerCas2Correct() {
        ajouterCompte("Ross", "Eric", 1, 4);

        //On simule les saisies de suppression dans ce fichier.
        chargerSaisies("./saisies/compte_test/supprimer_cas_2.txt");

        Compte compteAvant = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertEquals(1, compteAvant.getActif());

        //On simule le scénario de suppression d'un salarié.
        CompteControleur.supprimer();
        Compte compteApres = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertEquals(0, compteApres.getActif());
    }

    @Test
    @DisplayName("Test : supprimer un salarié - cas 3 : aucun salarié trouvé")
    void testSupprimerCas3PasTrouve() {
        //On simule le scénario de suppression d'un salarié.
        CompteControleur.supprimer();
    }
}