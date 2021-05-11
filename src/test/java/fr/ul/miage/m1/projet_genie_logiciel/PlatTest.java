package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlaceControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.controleurs.UniteControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
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

    }

    @Test
    @Order(1)
    @DisplayName("Test : lister les plats - cas 1 : plats trouvés")
    void testListerCas1Trouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat1 = new Plat();
        plat1.setLibelle("libellé");
        plat1.setCarte(0);
        plat1.setPrix(2.5);
        plat1.setIdCategorie(1);
        orm.persisterNUplet(plat1);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_1.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @Order(2)
    @DisplayName("Test : lister les plats - cas 2 : aucun plat trouvé")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de listing dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/lister_cas_2.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }
    @Test
    @Order(3)
    @DisplayName("Test : supprimer un plat - cas 1 : plat bien supprimé")
    void testSupprimerCas1BienSupprimee() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterTousLesNUplets(Plat.class);

        //On simule le scénario de suppression.
       PlatControleur.supprimer();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterTousLesNUplets(Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
    }

    @Test
    @Order(4)
    @DisplayName("Test : supprimer un plat - cas 2 : plat supprimé correct")
    void testSupprimerCas2Correcte() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à lister.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_2.txt"));
        ui.reinitialiserScanner();

        //Plat existant avant.
        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Plat.class);
        assertNotNull(platAvant);

        //On simule le scénario de suppression.
        PlatControleur.supprimer();

        //Plat suppirmé après.
        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 3", Plat.class);
        assertNull(platApres);
    }

    @Test
    @Order(5)
    @DisplayName("Test : supprimer un plat - cas 3 : aucun plat trouvé")
    void testSupprimerCas3PasTrouvees() {
        //On se connecte en tant que cuisinier.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 3", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de la suppression dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de suppression.
        PlatControleur.supprimer();
    }
    @Test
    @Order(6)
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 1 : plat bien ajouté")
    void testAjouterACarteCas1BienValide() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
        assertEquals(nbPlatsDansCarteAvant + 1, nbPlatsDansCarteApres);
    }

    @Test
    @Order(7)
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 2 : plat ajouté correct")
    void testAjouterACarteCas2Correct() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_2.txt"));
        ui.reinitialiserScanner();

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Plat.class);
        assertEquals(0, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 5", Plat.class);
        assertEquals(1, platApres.getCarte());
    }

    @Test
    @Order(8)
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 3 : aucun plat à ajouter trouvée")
    void testAjouterACarteCas3() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);
        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à la carte.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);


        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }

    @Test
    @Order(9)
    @DisplayName("Test : ajouter un plat à la carte du jour - cas 4 : aucun plat trouvée")
    void testAjouterACarteCas4PasTrouves() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/ajouter_carte_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }
    @Test
    @Order(10)
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 1 : plat bien supprimé")
    void testSupprimerDeCarteCas1BienValide() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_1.txt"));
        ui.reinitialiserScanner();

        int nbPlatsAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        int nbPlatsDansCarteAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        int nbPlatsDansCarteApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
        assertEquals(nbPlatsDansCarteAvant + 1, nbPlatsDansCarteApres);
    }

    @Test
    @Order(11)
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 2 : plat supprimé correct")
    void testSupprimerDeCarteCas2Correct() {
        //On se connecte en tant que directeur.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(1);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_2.txt"));
        ui.reinitialiserScanner();

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 8", Plat.class);
        assertEquals(1, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();

        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 8", Plat.class);
        assertEquals(0, platApres.getCarte());
    }

    @Test
    @Order(12)
    @DisplayName("Test : supprimer un plat à la carte du jour- cas 3 : aucun plat à supprimer trouvé")
    void testSupprimerDeCarteCas3() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);
        //On ajoute une catégorie pour pouvoir ajouter un plat.
        Categorie categorie = new Categorie();
        categorie.setLibelle("libellé1");
        orm.persisterNUplet(categorie);
        //On ajoute un plat à la carte.
        Plat plat = new Plat();
        plat.setLibelle("libellé");
        plat.setCarte(0);
        plat.setPrix(2.5);
        plat.setIdCategorie(1);
        orm.persisterNUplet(plat);


        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlaceTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_3.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }

    @Test
    @Order(13)
    @DisplayName("Test : supprimer un plat de la carte du jour - cas 4 : aucun plat trouvé")
    void testSupprimerDeCarteCas4PasTrouves() {
        //On se connecte en tant qu'assistant de service.
        ui.setUtilisateurConnecte((Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = 1", Compte.class));

        //On vide la table plat.
        orm.chercherTousLesNUplets(Plat.class).forEach(orm::supprimerNUplet);

        //On simule les saisies de validation dans ce fichier.
        System.setIn(PlatTest.class.getResourceAsStream("./saisies/plat_test/supprimer_carte_cas_4.txt"));
        ui.reinitialiserScanner();

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }

}
