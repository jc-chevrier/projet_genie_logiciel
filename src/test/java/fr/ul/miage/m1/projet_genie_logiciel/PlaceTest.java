package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
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
    }

    @Test
    @Order(1)
    @DisplayName("Test : lister toutes les tables - cas 1 : tables trouvées")
    void testListerCas1Trouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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

    @Test
    @Order(5)
    @DisplayName("Test : supprimer une table - cas 1 : table bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On ajoute une table à supprimer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlacesAvant = orm.compterTousLesNUplets(Place.class);

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();

        //Une table a due être supprimée.
        int nbPlacesApres = orm.compterTousLesNUplets(Place.class);
        assertEquals(nbPlacesAvant - 1, nbPlacesApres);
    }

    @Test
    @Order(6)
    @DisplayName("Test : supprimer une table - cas 2 : table supprimée correcte")
    void testSupprimerCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On ajoute une table à supprimer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Table existante avant.
        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Place.class);
        assertNotNull(placeAvant);

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();

        //Table suppirmée après.
        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Place.class);
        assertNull(placeApres);
    }

    @Test
    @Order(7)
    @DisplayName("Test : supprimer une table - cas 3 : aucune table trouvée")
    void testSupprimerCas3PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/supprimer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();
    }

    @Test
    @Order(8)
    @DisplayName("Test : lister les tables disponibles - cas 1 : tables trouvées")
    void testListerDisponiblesCas1Trouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table disponible à lister.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_disponibles_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @Order(9)
    @DisplayName("Test : lister les tables disponibles - cas 2 : aucune table disponible trouvée")
    void testListerDisponiblesCas2PasDisponiblesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table non disponible.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_disponibles_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @Order(10)
    @DisplayName("Test : lister les tables disponibles - cas 3 : aucune table trouvée")
    void testListerDisponiblesCas3PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_disponibles_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @Order(11)
    @DisplayName("Test : lister les tables à préparer - cas 1 : tables trouvées")
    void testListerAPreparerCas1Trouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table à préparer à lister.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_a_preparer_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @Order(12)
    @DisplayName("Test : lister les tables à préparer - cas 2 : aucune table à préparer trouvée")
    void testListerAPreparerCas2PasAPreparerTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à préparer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_a_preparer_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @Order(13)
    @DisplayName("Test : lister les tables à préparer - cas 3 : aucune table trouvée")
    void testListerAPreparerCas3PasTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_a_preparer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.validerPreparation();
    }

    @Test
    @Order(14)
    @DisplayName("Test : valider la préparation d'une table - cas 1 : table bien validée")
    void testValiderPreparationCas1BienValidee() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On ajoute une table à préparer.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlacesSalesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);
        int nbPlacesLibresAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();

        //Une table a due être supprimée.
        int nbPlacesSalesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);
        int nbPlacesLibresApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'libre'", Place.class);
        assertEquals(nbPlacesSalesAvant - 1, nbPlacesSalesApres);
        assertEquals(nbPlacesLibresAvant + 1, nbPlacesLibresApres);
    }

    @Test
    @Order(15)
    @DisplayName("Test : valider la préparation d'une table - cas 2 : table validée correcte")
    void testValiderPreparationCas2Correcte() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On ajoute une table à préparer.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_2.txt"));
        ui.reinitialiserScanner();

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 11", Place.class);
        assertEquals("sale", placeAvant.getEtat());

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 11", Place.class);
        assertEquals("libre", placeApres.getEtat());
    }

    @Test
    @Order(16)
    @DisplayName("Test : valider la préparation d'une table - cas 3 : aucune table à préparer trouvée")
    void testValiderPreparationCas3PasAPreparerTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à préparer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();
    }

    @Test
    @Order(17)
    @DisplayName("Test : valider la préparation d'une table - cas 4 : aucune table trouvée")
    void testValiderPreparationCas4PasTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();
    }

}