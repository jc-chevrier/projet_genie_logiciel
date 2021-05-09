package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Categorie;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
    @Order(1)
    @DisplayName("Test - lister les plats - cas 1 plat non trouvé")
    void testListerPlatCasNonTrouve() {
        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister.txt"));
        ui.reinitialiserScanner();

        List<Entite> platsAvant = orm.chercherTousLesNUplets(Plat.class);

        //On simule le scénario de lister.
        PlatControleur.lister();

        //Un ingrédient a du être inséré.
        List<Entite> platssApres = orm.chercherTousLesNUplets(Plat.class);
        assertEquals(platsAvant, platssApres);
    }

    @Test
    @Order(2)
    @DisplayName("Test - lister les plats - cas 2 plat non trouvé")
    void testListerPlatCasTrouve() {
        Categorie categorie = new Categorie();
        categorie.setLibelle("cat 1");
        orm.persisterNUplet(categorie);

        Plat plat = new Plat();
        plat.setLibelle("plat jour");
        plat.setCarte(1);
        plat.setIdCategorie(1);
        plat.setPrix(1.5);
        orm.persisterNUplet(plat);

        //On simule les saisies de lister dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister.txt"));
        ui.reinitialiserScanner();

        List<Entite> platsAvant = orm.chercherTousLesNUplets(Plat.class);

        //On simule le scénario de lister.
        PlatControleur.lister();

        //Un ingrédient a du être inséré.
        List<Entite> platssApres = orm.chercherTousLesNUplets(Plat.class);
        assertEquals(platsAvant, platssApres);
    }

}
