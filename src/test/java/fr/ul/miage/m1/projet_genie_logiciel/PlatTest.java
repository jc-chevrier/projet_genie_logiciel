package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

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
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));
    }

    @Test
    @Order(2)
    @DisplayName("Test : lister les plats - cas 2 : plats non trouvés")
    void testListerPlatCasNonTrouve() {
        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @Order(1)
    @DisplayName("Test : lister les plats - cas 1 : plats trouvés")
    void testListerPlatCasTrouve() {
        //On vide la table place.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On ajoute une catégorie.
        Categorie categorie = new Categorie();
        categorie.setLibelle("cat 1");
        orm.persisterNUplet(categorie);

        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("plat jour");
        plat.setCarte(1);
        plat.setIdCategorie(1);
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de lister.
        PlatControleur.lister();

    }

}
