package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CompteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Compte")
public class CompteTest {
    private static ORM orm;
    private static UI ui;

    static void ajouterCompte(@NotNull String nom, @NotNull String prenom, int actif, int idRole) {
        Compte compte = new Compte();
        compte.setNom(nom);
        compte.setPrenom(prenom);
        compte.setActif(actif);
        compte.setIdRole(idRole);
        orm.persisterNUplet(compte);
    }

    static void reinitialiserComptesReferenciels() {
        ajouterCompte("LAURENT", "Victoria", 1, 1);
        ajouterCompte("DURAND", "Oliviver", 1, 2);
        ajouterCompte("CARON", "Jules", 1, 3);
        ajouterCompte("DUPONT", "Theo", 1, 4);
        ajouterCompte("ANDERSON", "Christa", 1, 5);
    }

    static void reinitialiserTables() {
        //On réinitialise la table compte.
        orm.reinitialiserTable(Compte.class);
    }

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
        ui = UI.getInstance();
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
        //On crée un salarié.
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
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNotNull(compte);
    }

    @Test
    @DisplayName("Test : ajouter un salarié - cas 2 : salarié ajouté correct")
    void testAjouterCas2AjouteCorrect() {
        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class);
        assertNotNull(compte);

        assertEquals("Suivrot", compte.getNom());
        assertEquals("Guillaume", compte.getPrenom());
        assertEquals(1, compte.getIdRole());
        assertEquals(1, compte.getActif());
    }

    @Test
    @DisplayName("Test : supprimer un salarié - cas 1 : salarié bien supprimé")
    void testSupprimerCas1BienSupprime() {
        //On simule les saisies de suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        //On ajoute un salarié.
        ajouterCompte("Fromtz", "Thomas", 1, 1);

        int nbComptesAvant = orm.compterNUpletsAvecPredicat("WHERE actif = 1", Compte.class);
        assertEquals(1, nbComptesAvant);

        //On simule le scénario de suppression d'un salarié.
        CompteControleur.supprimer();

        int nbComptesApres =  orm.compterNUpletsAvecPredicat("WHERE actif = 1", Compte.class);
        assertEquals(0, nbComptesApres);
        nbComptesApres =  orm.compterNUpletsAvecPredicat("WHERE actif = 0", Compte.class);
        assertEquals(1, nbComptesApres);
    }


    @Test
    @DisplayName("Test : supprimer un salarié - cas 2 : salarié supprimé correct")
    void testSupprimerCas2Correct() {
        //On simule les saisies de suppression dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //On ajoute un salarié.
        ajouterCompte("Ross", "Eric", 1, 4);

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