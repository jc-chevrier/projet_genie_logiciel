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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
    @DisplayName("Test : lister les tables à préparer - cas 1 : tables trouvées")
    void testListerAPreparerCas1Trouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

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
    @Order(9)
    @DisplayName("Test : lister les tables à préparer - cas 2 : aucune table à préparer trouvée")
    void testListerAPreparerCas2PasAPreparerTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

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
    @Order(10)
    @DisplayName("Test : lister les tables à préparer - cas 3 : aucune table trouvée")
    void testListerAPreparerCas3PasTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_a_preparer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @Order(11)
    @DisplayName("Test : valider la préparation d'une table - cas 1 : table bien validée")
    void testValiderPreparationCas1BienValidee() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

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
    @Order(12)
    @DisplayName("Test : valider la préparation d'une table - cas 2 : table validée correcte")
    void testValiderPreparationCas2Correcte() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On ajoute une table à préparer.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_2.txt"));
        ui.reinitialiserScanner();

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 9", Place.class);
        assertEquals("sale", placeAvant.getEtat());

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 9", Place.class);
        assertEquals("libre", placeApres.getEtat());
    }

    @Test
    @Order(13)
    @DisplayName("Test : valider la préparation d'une table - cas 3 : aucune table à préparer trouvée")
    void testValiderPreparationCas3PasAPreparerTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

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
    @Order(14)
    @DisplayName("Test : valider la préparation d'une table - cas 4 : aucune table trouvée")
    void testValiderPreparationCas4PasTrouvees() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/valider_preparation_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();
    }

    @Test
    @Order(15)
    @DisplayName("Test : lister les tables disponibles - cas 1 : tables trouvées")
    void testListerDisponiblesCas1Trouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
    @Order(16)
    @DisplayName("Test : lister les tables disponibles - cas 2 : aucune table disponible trouvée")
    void testListerDisponiblesCas2PasDisponiblesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

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
    @Order(17)
    @DisplayName("Test : lister les tables disponibles - cas 3 : aucune table trouvée")
    void testListerDisponiblesCas3PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_disponibles_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @Order(18)
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
    @Order(19)
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

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 14", Place.class);
        assertEquals("libre", placeAvant.getEtat());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 14", Place.class);
        assertEquals("occupé", placeApres.getEtat());
    }

    @Test
    @Order(20)
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

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 15", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 15", Place.class);
        assertEquals("occupé", placeApres.getEtat());
    }

    @Test
    @Order(21)
    @DisplayName("Test : allouer une table à un client - cas 4 : aucune table à allouer trouvée")
    void testAllouerPourClientCas4PasAAllouerTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à allouer.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_client_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }

    @Test
    @Order(22)
    @DisplayName("Test : allouer une table à un client - cas 5 : aucune table trouvée")
    void testAllouerPourClientCas5PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies d'allocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_client_cas_5.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }

    @Test
    @Order(23)
    @DisplayName("Test : désallouer une table à un client - cas 1 : désallocation bien faite")
    void testDesallouerPourClientCas1BienFaite() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies de désallocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/desallouer_pour_client_cas_1.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table à désallouer.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        int nbPlacesOccupeesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);
        int nbPlacesSalesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();

        int nbPlacesOccupeesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'occupé'", Place.class);
        int nbPlacesSalesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'sale'", Place.class);
        assertEquals(nbPlacesOccupeesAvant - 1, nbPlacesOccupeesApres);
        assertEquals(nbPlacesSalesAvant + 1, nbPlacesSalesApres);
    }

    @Test
    @Order(24)
    @DisplayName("Test : désallouer une table à un client - cas 2 : désallocation faite correcte")
    void testDesallouerPourClientCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On simule les saisies de désallocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/desallouer_pour_client_cas_2.txt"));
        ui.reinitialiserScanner();

        //On ajoute une table à désallouer.
        Place place = new Place();
        place.setEtat("occupé");
        orm.persisterNUplet(place);

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 18", Place.class);
        assertEquals("occupé", placeAvant.getEtat());

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 18", Place.class);
        assertEquals("sale", placeApres.getEtat());
    }

    @Test
    @Order(25)
    @DisplayName("Test : désallouer une table à un client - cas 3 : aucune table à désallouer trouvée")
    void testDesallouerPourClientCas3PasADesallouerTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à désallouer.
        Place place = new Place();
        place.setEtat("sale");
        orm.persisterNUplet(place);

        //On simule les saisies de désallocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/desallouer_pour_client_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();
    }

    @Test
    @Order(26)
    @DisplayName("Test : désallouer une table à un client - cas 4 : aucune table trouvée")
    void testDesallouerPourClientCas4PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de désallocation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/desallouer_pour_client_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();
    }

    @Test
    @Order(27)
    @DisplayName("Test : lister les tables allouées à un serveur - cas 1 : tables trouvées")
    void testListerAlloueesPourServeurCas1Trouvees() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        Place place = new Place();
        place.setEtat("libre");
        place.setIdCompteServeur(4);
        orm.persisterNUplet(place);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_allouees_pour_serveur_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerAlloueesPourServeur();
    }

    @Test
    @Order(28)
    @DisplayName("Test : lister les tables allouées à un serveur - cas 2 : aucune table trouvée")
    void testListerAlloueesPourServeurCas2PasTrouvees() {
        //On se connecte en tant que serveur.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/lister_allouees_pour_serveur_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlaceControleur.listerAlloueesPourServeur();
    }

    @Test
    @Order(29)
    @DisplayName("Test : allouer une table à un serveur - cas 1 : allocation bien faite")
    void testAllouerPourServeurCas1Trouves() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à allouer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);
        //On ajoute un serveur.
        Compte compte = new Compte();
        compte.setNom("NOM");
        compte.setPrenom("prenom");
        compte.setActif(1);
        compte.setIdRole(4);
        orm.persisterNUplet(compte);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_1.txt"));
        ui.reinitialiserScanner();

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 21", Place.class);
        assertNull(placeAvant.getIdCompteServeur());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 21", Place.class);
        assertNotNull(placeApres.getIdCompteServeur());
    }

    @Test
    @Order(30)
    @DisplayName("Test : allouer une table à un serveur - cas 2 : aucune table non allouée trouvée")
    void testAllouerPourServeurCas2PPasPlaceNonAlloueeTrouvee() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);
        //On ajoute un serveur.
        Compte compte = new Compte();
        compte.setNom("NOM");
        compte.setPrenom("prenom");
        compte.setActif(1);
        compte.setIdRole(4);
        orm.persisterNUplet(compte);
        //On ajoute une table allouée.
        Place place = new Place();
        place.setEtat("libre");
        place.setIdCompteServeur(7);
        orm.persisterNUplet(place);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @Order(31)
    @DisplayName("Test : allouer une table à un serveur - cas 3 : aucun serveur actif trouvé")
    void testAllouerPourServeurCas3PasServeurActifTrouve() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à allouer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);
        //On ajoute un serveur.
        Compte compte = new Compte();
        compte.setNom("NOM");
        compte.setPrenom("prenom");
        compte.setActif(0);
        compte.setIdRole(4);
        orm.persisterNUplet(compte);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @Order(32)
    @DisplayName("Test : allouer une table à un serveur - cas 4 : aucune table trouvée")
    void testAllouerPourServeurCas4PasServeursTrouves() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);
        //On ajoute un serveur.
        Compte compte = new Compte();
        compte.setNom("NOM");
        compte.setPrenom("prenom");
        compte.setActif(1);
        compte.setIdRole(4);
        orm.persisterNUplet(compte);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @Order(33)
    @DisplayName("Test : allouer une table à un serveur - cas 5 : aucun serveur trouvé")
    void testAllouerPourServeurCas5PasPlacesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);
        //On ajoute une table pas à allouer.
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_5.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @Order(34)
    @DisplayName("Test : allouer une table à un serveur - cas 6 : aucun serveur et aucune table trouvés")
    void testAllouerPourServeurCas6PasTrouves() {
        //On se connecte en tant que maitre d'hotel.
        ui.setUtilisateurConnecte((Compte) orm.chercherNUpletAvecPredicat("WHERE ID = 2", Compte.class));

        //On vide la table place.
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);
        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        //On simule les saisies d'allocation ans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/place_test/allouer_pour_serveur_cas_6.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }
}