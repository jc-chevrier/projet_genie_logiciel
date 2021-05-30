package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Table")
public class PlaceTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
        //On réinitialise la table place.
        orm.reinitialiserTable(Place.class);
        orm.reinitialiserSequenceId(6, Compte.class);
    }

    static void reinitialiserCompteServeur4(){
        int compte4Existe = orm.compterNUpletsAvecPredicat("WHERE ID = 4", Compte.class);
        if(compte4Existe == 0) {
            orm.reinitialiserSequenceId(4, Compte.class);
            ajouterCompte("DUPONT", "Theo", 1, 4);
        }
    }

    @BeforeEach
    void faireAvantChaqueTest() {
        reinitialiserCompteServeur4();
        reinitialiserTables();
    }

    @AfterAll
    static void faireApresTousLesTests() {
        reinitialiserCompteServeur4();
        reinitialiserTables();
    }

    @Test
    @DisplayName("Test : lister toutes les tables - cas 1 : tables trouvées")
    void testListerCas1Trouvees() {
        ajouterPlaceEtat("libre");

        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de listing.
        PlaceControleur.lister();
    }

    @Test
    @DisplayName("Test : lister toutes les tables - cas 2 : aucune table trouvée")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de listing.
        PlaceControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter une table - cas 1 : table bien ajoutée")
    void testAjouterCas1BienAjoutee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        int nbPlacesAvant = orm.compterTousLesNUplets(Place.class);

        //On simule le scénario d'ajout.
        PlaceControleur.ajouter();

        //Une table a due être insérée.
        int nbPlacesApres = orm.compterTousLesNUplets(Place.class);
        assertEquals(nbPlacesAvant + 1, nbPlacesApres);
    }

    @Test
    @DisplayName("Test : ajouter une table - cas 1 : table bien ajoutée avec bons attributs")
    void testAjouterCas2BonsAttributs() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario d'ajout.
        PlaceControleur.ajouter();

        //Les attributs doivent avoir ces valeurs.
        Place place = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", place.getEtat());
        assertNull(place.getIdCompteServeur());
        assertNull(place.getDatetimeReservation());
        assertNull(place.getNomReservation());
        assertNull(place.getPrenomReservation());
    }

    @Test
    @DisplayName("Test : supprimer une table - cas 1 : table bien supprimée")
    void testSupprimerCas1BienSupprimee() {
        ajouterPlaceEtat("libre");

        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/place_test/supprimer_cas_1.txt");

        int nbPlacesAvant = orm.compterTousLesNUplets(Place.class);

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();

        //Une table a due être supprimée.
        int nbPlacesApres = orm.compterTousLesNUplets(Place.class);
        assertEquals(nbPlacesAvant - 1, nbPlacesApres);
    }

    @Test
    @DisplayName("Test : supprimer une table - cas 2 : table supprimée correcte")
    void testSupprimerCas2Correcte() {
        ajouterPlaceEtat("libre");

        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/place_test/supprimer_cas_2.txt");

        //Table existante avant.
        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNotNull(placeAvant);

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();

        //Table suppirmée après.
        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNull(placeApres);
    }

    @Test
    @DisplayName("Test : supprimer une table - cas 3 : aucune table trouvée")
    void testSupprimerCas3PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de suppression.
        PlaceControleur.supprimer();
    }

    @Test
    @DisplayName("Test : lister les tables à préparer - cas 1 : tables trouvées")
    void testListerAPreparerCas1Trouvees() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        ajouterPlaceEtat("sale");

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @DisplayName("Test : lister les tables à préparer - cas 2 : aucune table à préparer trouvée")
    void testListerAPreparerCas2PasAPreparerPasTrouve() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        ajouterPlaceEtat("libre");

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @DisplayName("Test : lister les tables à préparer - cas 3 : aucune table trouvée")
    void testListerAPreparerCas3PasTrouve() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        //On simule le scénario de listing.
        PlaceControleur.listerAPreparer();
    }

    @Test
    @DisplayName("Test : valider la préparation d'une table - cas 1 : table bien validée")
    void testValiderPreparationCas1BienValidee() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        ajouterPlaceEtat("sale");

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/place_test/valider_preparation_cas_1.txt");

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
    @DisplayName("Test : valider la préparation d'une table - cas 2 : table validée correcte")
    void testValiderPreparationCas2Correcte() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        ajouterPlaceEtat("sale");

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/place_test/valider_preparation_cas_2.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("sale", placeAvant.getEtat());

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : valider la préparation d'une table - cas 3 : aucune table à préparer trouvée")
    void testValiderPreparationCas3PasAPreparerTrouvees() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        ajouterPlaceEtat("libre");

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : valider la préparation d'une table - cas 4 : aucune table trouvée")
    void testValiderPreparationCas4PasTrouvees() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnAssistantService();

        //On simule le scénario de validation.
        PlaceControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : lister les tables disponibles - cas 1 : tables trouvées")
    void testListerDisponiblesCas1Trouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @DisplayName("Test : lister les tables disponibles - cas 2 : aucune table disponible trouvée")
    void testListerDisponiblesCas2PasDisponiblesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @DisplayName("Test : lister les tables disponibles - cas 3 : aucune table trouvée")
    void testListerDisponiblesCas3PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de listing.
        PlaceControleur.listerDisponibles();
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 1 : allocation bien faite")
    void testAllouerPourClientCas1BienFaite() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule les saisies d'allocation dans ce fichier.
        chargerSaisies("./saisies/place_test/allouer_pour_client_cas_1.txt");

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
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule les saisies d'allocation dans ce fichier.
        chargerSaisies("./saisies/place_test/allouer_pour_client_cas_2.txt");

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
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("réservé");

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/place_test/allouer_pour_client_cas_3.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("occupé", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 4 : aucune table disponible à allouer trouvée")
    void testAllouerPourClientCas4PasAAllouerTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("occupé");

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }

    @Test
    @DisplayName("Test : allouer une table à un client - cas 5 : aucune table à allouer trouvée")
    void testAllouerPourClientCas5PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourClient();
    }

    @Test
    @DisplayName("Test : désallouer une table à un client - cas 1 : désallocation bien faite")
    void testDesallouerPourClientCas1BienFaite() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("occupé");

        //On simule les saisies de désallocation dans ce fichier.
        chargerSaisies("./saisies/place_test/desallouer_pour_client_cas_1.txt");

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
    @DisplayName("Test : désallouer une table à un client - cas 2 : désallocation faite correcte")
    void testDesallouerPourClientCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("occupé");

        //On simule les saisies de désallocation dans ce fichier.
        chargerSaisies("./saisies/place_test/desallouer_pour_client_cas_2.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("occupé", placeAvant.getEtat());

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("sale", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : désallouer une table à un client - cas 3 : aucune table à désallouer trouvée")
    void testDesallouerPourClientCas3PasADesallouerTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("sale");

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();
    }

    @Test
    @DisplayName("Test : désallouer une table à un client - cas 4 : aucune table trouvée")
    void testDesallouerPourClientCas4PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourClient();
    }

    @Test
    @DisplayName("Test : lister les tables allouées à un serveur - cas 1 : tables trouvées")
    void testListerAlloueesPourServeurCas1Trouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCompte("NOM", "prenom", 1, 4);
        ajouterPlaceEtatServeur("libre", 6);

        //On simule le scénario de listing.
        PlaceControleur.listerAlloueesPourServeur();
    }

    @Test
    @DisplayName("Test : lister les tables allouées à un serveur - cas 2 : aucune table trouvée")
    void testListerAlloueesPourServeurCas2PasTrouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario de listing.
        PlaceControleur.listerAlloueesPourServeur();
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 1 : allocation bien faite")
    void testAllouerPourServeurCas1Trouves() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 1, 4);
        ajouterPlaceEtat("libre");

        //On simule les saisies d'allocation ans ce fichier.
        chargerSaisies("./saisies/place_test/allouer_pour_serveur_cas_1.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNull(placeAvant.getIdCompteServeur());

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNotNull(placeApres.getIdCompteServeur());
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 2 : aucun serveur actif trouvé")
    void testAllouerPourServeurCas2PasServeurActifTrouve() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 0, 4);
        ajouterPlaceEtatServeur("libre", 6);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 3 : aucune table non allouée trouvée")
    void testAllouerPourServeurCas3PPasPlaceNonAlloueeTrouvee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 1, 4);
        ajouterPlaceEtatServeur("libre", 6);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 4 : aucun serveur trouvé")
    void testAllouerPourServeurCas4PasPlacesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterPlaceEtat("libre");

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 5 : aucune table trouvée")
    void testAllouerPourServeurCas5PasServeursTrouves() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 1, 4);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }

    @Test
    @DisplayName("Test : allouer une table à un serveur - cas 6 : aucun serveur et aucune table trouvés")
    void testAllouerPourServeurCas6PasTrouves() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        //On simule le scénario d'allocation.
        PlaceControleur.allouerPourServeur();
    }


    @Test
    @DisplayName("Test : désallouer une table à un serveur - cas 1 : désallocation bien faite")
    void testDesallouerPourServeurCas1Trouves() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 1, 4);
        ajouterPlaceEtatServeur("libre", 6);

        //On simule les saisies d'allocation ans ce fichier.
        chargerSaisies("./saisies/place_test/desallouer_pour_serveur_cas_1.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNotNull(placeAvant.getIdCompteServeur());

        //On simule le scénario d'allocation.
        PlaceControleur.desallouerPourServeur();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertNull(placeApres.getIdCompteServeur());
    }

    @Test
    @DisplayName("Test : désallouer une table à un serveur - cas 2 : aucun serveur actif trouvé")
    void testDesallouerPourServeurCas2PasServeurActifTrouve() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        //On simule le scénario de désallocation.
        PlaceControleur.desallouerPourServeur();
    }

    @Test
    @DisplayName("Test : désallouer une table à un serveur - cas 3 : aucune table allouée trouvée")
    void testDesallouerPourServeurCas3PPasPlaceAlloueeTrouvee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On supprime les serveurs existants.
        orm.chercherNUpletsAvecPredicat("WHERE ID_ROLE = 4", Compte.class).forEach(orm::supprimerNUplet);

        ajouterCompte("NOM", "prenom", 1, 4);

        //On simule les saisies désallocation ans ce fichier.
        chargerSaisies("./saisies/place_test/desallouer_pour_serveur_cas_3.txt");

        //On simule le scénario désallocation.
        PlaceControleur.desallouerPourServeur();
    }

    @Test
    @DisplayName("Test : lister les tables réservées - cas 1 : tables trouvées")
    void testListerReserveesCas1Trouve() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("réservé");

        //On simule le scénario de listing des tables réservées.
        PlaceControleur.listerReservees();
    }

    @Test
    @DisplayName("Test : lister les tables réservées - cas 2 : aucune table trouvée")
    void testListerReserveesCas2PasTrouve() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de listing des tables réservées.
        PlaceControleur.listerReservees();
    }

    @Test
    @DisplayName("Test : réserver une table : cas 1 - table bien réservée")
    void testReserverCas1BienReservee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule les saisies de réservation dans ce fichier.
        chargerSaisies("./saisies/place_test/reserver_cas_1.txt");

        int nbPlacesAvant = orm.compterNUpletsAvecPredicat("WHERE etat = 'libre'", Place.class);
        assertEquals(1, nbPlacesAvant);

        //On simule le scénario de réservation.
        PlaceControleur.reserver();

        int nbPlacesApres = orm.compterNUpletsAvecPredicat("WHERE etat = 'libre'", Place.class);
        assertEquals(0, nbPlacesApres);
        nbPlacesApres = orm.compterNUpletsAvecPredicat("WHERE etat = 'réservé'", Place.class);
        assertEquals(1, nbPlacesApres);

    }

    @Test
    @DisplayName("Test : réserver une table : cas 2 - table réservée correcte")
    void testReserverCas2ReserveeCorrect() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceEtat("libre");

        //On simule les saisies de réservation dans ce fichier.
        chargerSaisies("./saisies/place_test/reserver_cas_2.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", placeAvant.getEtat());

        //On simule le scénario de réservation.
        PlaceControleur.reserver();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : réserver une table : cas 3 - aucune table trouvée")
    void testReserverCas3PasTrouvee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de réservation.
        PlaceControleur.reserver();
    }

    @Test
    @DisplayName("Test : annuler une réservation d'une table : cas 1 - bien annulée")
    void testAnnulerReservationCas1BienAnnulee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceReservee("réservé", "Torent", "Loïc", new Date());

        //On simule les saisies d'annulation de réseravtion dans ce fichier.
        chargerSaisies("./saisies/place_test/annuler_reservation_cas_1.txt");

        int nbPlacesAvant = orm.compterNUpletsAvecPredicat("WHERE etat = 'réservé'", Place.class);
        assertEquals(1, nbPlacesAvant);

        //On simule le scénario d'annulation de réservation.
        PlaceControleur.annulerReservation();

        int nbPlacesApres = orm.compterNUpletsAvecPredicat("WHERE etat = 'réservé'", Place.class);
        assertEquals(0, nbPlacesApres);
        nbPlacesApres = orm.compterNUpletsAvecPredicat("WHERE etat = 'libre'", Place.class);
        assertEquals(1, nbPlacesApres);
    }

    @Test
    @DisplayName("Test : annuler une réservation d'une table : cas 2 - annulée correcte")
    void testAnnulerReservationCas2Correct() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceReservee("réservé", "Rollui", "Evan", new Date());

        //On simule les saisies d'annulation de réseravtion dans ce fichier.
        chargerSaisies("./saisies/place_test/annuler_reservation_cas_2.txt");


        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'annulation de réservation.
        PlaceControleur.annulerReservation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : annuler une réservation d'une table : cas 3 - mauvais nom et / ou prenom")
    void testAnnulerReservationCas3Annulee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceReservee("réservé", "Nols", "Léa", new Date());

        //On simule les saisies d'annulation de réseravtion dans ce fichier.
        chargerSaisies("./saisies/place_test/annuler_reservation_cas_3.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'annulation de réservation.
        PlaceControleur.annulerReservation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : annuler une réservation d'une table : cas 4 - annulée avec casse")
    void testAnnulerReservationCas4AnnuleeAvecCasse() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterPlaceReservee("réservé", "Foldi", "Julien", new Date());

        //On simule les saisies d'annulation de réseravtion dans ce fichier.
        chargerSaisies("./saisies/place_test/annuler_reservation_cas_4.txt");

        Place placeAvant = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("réservé", placeAvant.getEtat());

        //On simule le scénario d'annulation de réservation.
        PlaceControleur.annulerReservation();

        Place placeApres = (Place) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Place.class);
        assertEquals("libre", placeApres.getEtat());
    }

    @Test
    @DisplayName("Test : annuler une réservation d'une table : cas 5 - aucune table trouvée")
    void testAnnulerReservationCas5PasTrouvee() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule les saisies d'annulation de réseravtion dans ce fichier.
        chargerSaisies("./saisies/place_test/annuler_reservation_cas_5.txt");

        //On simule le scénario d'annulation de réservation.
        PlaceControleur.annulerReservation();
    }
}