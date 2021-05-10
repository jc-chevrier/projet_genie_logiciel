package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Plat")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlatTest {
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
    @DisplayName("Test - ajouter un plat - cas 1 : n-uplet plat bien ajoutée")
    void testAjouterPlatCasBienAjoutee() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        List<Entite> platsAvant = orm.chercherTousLesNUplets(Plat.class);

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();

        //Un plat a du être insérée.
        List<Entite> platsApres = orm.chercherTousLesNUplets(Plat.class);
        assertEquals(platsAvant.size() + 1, platsApres.size());
    }

    @Test
    @Order(2)
    @DisplayName("Test - ajouter une unité - cas 2 : n-uplet unité bien ajoutée avec bon libellé")
    void testAjouterPlatCas2BonLibelle() {
        //On simule les saisies de l'ajout dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/unite_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        UniteControleur.ajouter();

        //L'unité insérée doit avoir ce libellé : "libellé test ajouter cas 2 bon libellé".
        Unite uniteInseree = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Unite.class);
        assertEquals("libellé test ajouter cas 2 bon libellé", uniteInseree.getLibelle());
    }


}
