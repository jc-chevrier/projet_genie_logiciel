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

    /**
     * Ajouter un rôle.
     *
     * @param libelle
     */
    static void ajouterRole(@NotNull String libelle) {
        Role role = new Role();
        role.setLibelle(libelle);
        orm.persisterNUplet(role);
    }

    /**
     * Ajouter un compte.
     *
     * @param nom
     * @param prenom
     * @param actif
     * @param idRole
     */
    static void ajouterCompte(@NotNull String nom, @NotNull String prenom, int actif, int idRole) {
        Compte compte = new Compte();
        compte.setNom(nom);
        compte.setPrenom(prenom);
        compte.setActif(actif);
        compte.setIdRole(idRole);
        orm.persisterNUplet(compte);
    }

    static void reinitialiserTables() {
        //On réinitialise la table compte.
        orm.supprimerTousLesNUplets(Compte.class);
        //On réinitialise la table role.
        orm.reinitialiserTable(Role.class);
        ajouterRole("Directeur");
        ajouterRole("Maitre d''hôtel");
        ajouterRole("Cuisinier");
        ajouterRole("Serveur");
        ajouterRole("Assistant de service");
        //On réinitialise la table compte.
        orm.reinitialiserSequenceIdA1(Compte.class);
        ajouterCompte("LAURENT", "Victoria", 1, 1);
        ajouterCompte("DURAND", "Oliviver", 1, 2);
        ajouterCompte("CARON", "Jules", 1, 3);
        ajouterCompte("DUPONT", "Theo", 1, 4);
        ajouterCompte("ANDERSON", "Christa", 1, 5);
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
    @DisplayName("Test : lister les salariés - cas 1 : salariés trouvés")
    void testListerCas1Trouve() {
        //On crée un salarié.
        Compte compte = new Compte();
        compte.setPrenom("Joe");
        compte.setNom("Loan");
        compte.setActif(1);
        compte.setIdRole(1);
        orm.persisterNUplet(compte);

        //On simule le scénario de listing des salariés.
        CompteControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter un salarié - cas 1 : salarié bien ajouté")
    void testAjouterCas1BienAjoute() {
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
    void testAjouterCas2AjouteCorrect() {
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