package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CompteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Compte")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompteTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables() {
        //On réinitialise la table compte.
        orm.supprimerNUpletsAvecPredicat("WHERE ID > 5", Compte.class);
        orm.reinitialiserSequenceId(6, Compte.class);
    }

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
        ui = UI.getInstance();
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));
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
    @DisplayName("Test : ajouter un salarié - cas 1 : salarié bien ajouté")
    void testConnexionCas1BienAjoute() {
        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Compte.class);
        assertNotNull(compte);
    }

    @Test
    @DisplayName("Test : ajouter un salarié - cas 2 : salarié ajouté correct")
    void testConnexionCas2AjouteCorrect() {
        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/compte_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Compte.class);
        assertNull(compte);

        //On simule le scénario d'ajout d'un salarié.
        CompteControleur.ajouter();

        compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Compte.class);
        assertNotNull(compte);

        assertEquals("Suivrot", compte.getNom());
        assertEquals("Guillaume", compte.getPrenom());
        assertEquals(1, compte.getIdRole());
        assertEquals(1, compte.getActif());
    }
}