package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.IngredientControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commande")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandeTest {
    private static ORM orm;
    private static UI ui;

    static void reinitialiserTables(){
        //On réinitialise la table commande.
        orm.reinitialiserTable(Commande.class);
        //On réinitialise la table unité.

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
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 1 : allocation bien faite")
    void testAllouerPourClientCas1BienFaite() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies d'allocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_client_cas_1.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table à allouer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        int nbPlacesOccupeesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);
        int nbPlacesLibresAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        int nbPlacesOccupeesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);
        int nbPlacesLibresApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);
        assertEquals(nbPlacesLibresAvant - 1, nbPlacesLibresApres);
        assertEquals(nbPlacesOccupeesAvant + 1, nbPlacesOccupeesApres);
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 2 : allocation faite correcte")
    void testAllouerPourClientCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies d'allocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_client_cas_2.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table à allouer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", placeAvant.getEtat());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("occupé", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 3 : allocation d'une table réservée correcte")
    void testAllouerPourClientCas3Reservee() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_client_cas_3.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table disponible à allouer.
        Place place = new Place();
        place.setEtat("réservé");
        orm.persisterNUplet(place);

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("occupé", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 4 : aucune table à allouer trouvée")
    void testAllouerPourClientCas4PasAAllouerTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On ajoute une table pas à allouer.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 5 : aucune table trouvée")
    void testAllouerPourClientCas5PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }


}