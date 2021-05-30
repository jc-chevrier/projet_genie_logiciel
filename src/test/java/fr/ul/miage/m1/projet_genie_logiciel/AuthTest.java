package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.AuthControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentification")
public class AuthTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
        //On réinitialise la table compte.
        //(On ne supprime pas les lignes référentielles).
        orm.supprimerNUpletsAvecPredicat("WHERE ID > 5", Compte.class);
        orm.reinitialiserSequenceId(6, Compte.class);
    }

    static void deconnecter() {
        ui.setUtilisateurConnecte(null);
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
        ajouterCompte("Orient", "Jules", 1, 1);

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);

        //On simule les saisies de connexion dans ce fichier.
        chargerSaisies("./saisies/auth_test/se_connecter_cas_1.txt");

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();

        //Un salarié doit désormais être connecté.
        utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNotNull(utilisateurConnecte);
    }

    @Test
    @DisplayName("Test : se connecter - cas 2 : connexion réussie correcte")
    void testConnexionCas2ReussieCorrecte() {
        ajouterCompte("Roque", "Théo", 1, 1);
        ajouterCompte("Orient", "Jules", 1, 3);
        ajouterCompte("Rolvas", "Lisa", 1, 5);

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);

        //On simule les saisies de connexion dans ce fichier.
        chargerSaisies("./saisies/auth_test/se_connecter_cas_2.txt");

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();

        //Un salarié doit désormais être connecté.
        utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNotNull(utilisateurConnecte);
        assertEquals(7, utilisateurConnecte.getId());
        assertEquals("Jules", utilisateurConnecte.getPrenom());
        assertEquals("Orient", utilisateurConnecte.getNom());
    }


    @Test
    @DisplayName("Test : se connecter - cas 3 : connexion réussie avec casse")
    void testConnexionCas3ReussieCasse() {
        ajouterCompte("Volovent", "Julie", 1, 1);

        //On simule les saisies de connexion dans ce fichier.
        chargerSaisies("./saisies/auth_test/se_connecter_cas_3.txt");

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se connecter - cas 4 : connexion échouée puis réussie")
    void testConnexionCas4Echec() {
        ajouterCompte("Lovuis", "Francis", 1, 4);

        //On simule les saisies de connexion dans ce fichier.
        chargerSaisies("./saisies/auth_test/se_connecter_cas_4.txt");

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se connecter - cas 5 : connexion echouée 2 fois puis réussie")
    void testConnexionCas5Echec2fois() {
        ajouterCompte("Lovuis", "Francois", 1, 4);

        //On simule les saisies de connexion dans ce fichier.
        chargerSaisies("./saisies/auth_test/se_connecter_cas_5.txt");

        //On simule le scénario de connexion.
        AuthControleur.seConnecter();
    }

    @Test
    @DisplayName("Test : se déconnecter - cas 1 : déconnexion réussie")
    void testDeconnexionCas1ReussieConnecteAvant() {
        ajouterCompte("Ross", "Richard", 1, 4);

        //On simule le scénario de connexion.
        AuthControleur.seDeconnecter();

        Compte utilisateurConnecte = ui.getUtilisateurConnecte();
        assertNull(utilisateurConnecte);
    }
}