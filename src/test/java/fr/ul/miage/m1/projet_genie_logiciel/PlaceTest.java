package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Table")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaceTest {
    private static ORM orm;
    private static UI ui;

    @BeforeAll
    static void faireAvantTousLesTests() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();

        ui = UI.getInstance();
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));
    }

    @Test
    @Order(1)
    @DisplayName("Test : lister toutes les tables - cas 1 : tables trouvées")
    void testListerCas1Trouvees() {
        //On ajoute une table à lister.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.lister();
    }

    @Test
    @Order(2)
    @DisplayName("Test : lister toutes les tables - cas 2 : aucune table trouvée")
    void testListerCas2PasTrouvees() {
        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.lister();
    }

    @Test
    @Order(3)
    @DisplayName("Test : ajouter une table - cas 1 : table bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/ajouter_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlacesAvant = orm.compterTousLesNUplets(Place.class);

        //On simule le scénario d'ajout.
        PlaceControleur.ajouter();

        //Une table a due être insérée.
        int nbPlacesApres = orm.compterTousLesNUplets(Place.class);
        assertEquals(nbPlacesAvant + 1, nbPlacesApres);
    }

    @Test
    @Order(4)
    @DisplayName("Test : ajouter une table - cas 1 : table bien ajoutée avec bon attributs")
    void testAjouterCas2BonsAttributs() {
        //On simule les saisies d'ajout dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/ajouter_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'ajout.
        PlaceControleur.ajouter();

        //Les attributs doivent avoir ces valeurs.
        Place place = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Place.class);
        assertEquals("libre", place.getEtat());
        assertNull(place.getIdCompteServeur());
        assertNull(place.getDatetimeReservation());
        assertNull(place.getNomReservation());
        assertNull(place.getPrenomReservation());
    }
}