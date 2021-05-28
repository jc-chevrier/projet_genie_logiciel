package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.AuthControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentification")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables() {
        //On réinitialise la table compte.
        orm.supprimerNUpletsAvecPredicat("WHERE ID > 5", Compte.class);
        orm.reinitialiserSequenceId(6, Compte.class);
    }

    static void deconnecter() {
        ui.setUtilisateurConnecte(null);
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
        deconnecter();
    }

    @AfterAll
    static void faireApresTousLesTests() {
        reinitialiserTables();
    }

    @Test
    @DisplayName("Test : se connecter - cas 1 : connexion réussie")
    void testConnexionCas1Reussie() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Jules");
        compte.setNom("Orient");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);

        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/auth_test/se_connecter_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();

        utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNotNull(utilisateurConnecte);
    }

    @Test
    @DisplayName("Test : se connecter - cas 2 : connexion réussie correcte")
    void testConnexionCas2ReussieCorrecte() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Théo");
        compte.setNom("Roque");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On crée un salarié.
        compte = new Compte();
        compte.setPrenom("Jules");
        compte.setNom("Orient");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On crée un salarié.
        compte = new Compte();
        compte.setPrenom("Lisa");
        compte.setNom("Bolvas");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);

        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/auth_test/se_connecter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();

        utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNotNull(utilisateurConnecte);

        utilisateurConnecte = ui.getUtilisateurConnecte();
        assertEquals(7, utilisateurConnecte.getId());
        assertEquals("Jules", utilisateurConnecte.getPrenom());
        assertEquals("Orient", utilisateurConnecte.getNom());
    }


    @Test
    @DisplayName("Test : se connecter - cas 3 : connexion réussie avec casse")
    void testConnexionCas3ReussieCasse() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Julie");
        compte.setNom("Volovent");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/auth_test/se_connecter_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se connecter - cas 4 : connexion échouée puis réussie")
    void testConnexionCas4Echec() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Francis");
        compte.setNom("Lovuis");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/auth_test/se_connecter_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se connecter - cas 5 : connexion echouée 2 fois puis réussie")
    void testConnexionCas5Echec2fois() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Francois");
        compte.setNom("Lovuis");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On simule les saisies de connexion dans ce fichier.
        System.setIn(IngredientTest.class.getResourceAsStream("./saisies/auth_test/se_connecter_cas_5.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se déconnecter - cas 1 : déconnexion réussie")
    void testDeconnexionCas1ReussieConnecteAvant() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Richard");
        compte.setNom("Ross");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);
        ui.setUtilisateurConnecte(compte);

        //On simule le scénario de connexion.
        AuthControleur.seDeconnecter();

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);
    }
}