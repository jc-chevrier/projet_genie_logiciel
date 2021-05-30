package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.CommandeControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commande")
public class CommandeTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables(){
        //On réinitialise la table stat chiffre d'affaire.
        orm.reinitialiserTable(StatChiffreAffaire.class);
        //On réinitialise la table ligne commande.
        orm.reinitialiserTable(LigneCommande.class);
        //On réinitialise la table plat_ingrédiants.
        orm.reinitialiserTable(PlatIngredients.class);
        //On réinitialise la table plat.
        orm.reinitialiserTable(Plat.class);
        //On réinitialise la table catégorie.
        orm.reinitialiserTable(Categorie.class);
        //On réinitialise la table ingrédient.
        orm.reinitialiserTable(Ingredient.class);
        //On réinitialise la table unité.
        orm.reinitialiserTable(Unite.class);
        //On réinitialise la table commande.
        orm.reinitialiserTable(Commande.class);
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
    @DisplayName("Test : Lister les commandes - cas 1 : commande trouvé")
    void testListerCas1rouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);

        //On simule le scénario du listing.
        CommandeControleur.lister();
    }

    @Test
    @DisplayName("Test : Lister toutes les commandes - cas 2 : aucune commande trouvée dans la base")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario du listing.
        CommandeControleur.lister();
    }

    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 1 : validation réussie")
    void testValiderPreparationCas1BienFait() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);
        ajouterLigneCommande("en attente", 1, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_preparation_cas_1.txt");

        int nbLignesCommandeEnAttenteAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'en attente'", LigneCommande.class);
        assertEquals(1, nbLignesCommandeEnAttenteAvant);

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();

        int nbLignesCommandeEnAttenteApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'en attente'", LigneCommande.class);
        assertEquals(0, nbLignesCommandeEnAttenteApres);

        int nbLignesCommandePretesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'prêt'", LigneCommande.class);
        assertEquals(1, nbLignesCommandePretesApres);
    }

    @Test
    @DisplayName("Test : Valider la préparation d'un plat d'une commande - cas 2 : validation correcte")
    void testValiderPreparationCas2Correcte() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);
        ajouterLigneCommande("en attente", 1, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_preparation_cas_2.txt");

        LigneCommande ligneCommandeAvant = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("en attente", ligneCommandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();

        LigneCommande ligneCommandeApres = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("prêt", ligneCommandeApres.getEtat());
    }

    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 3 : aucune commande en attente trouvée")
    void testValiderPreparationCas3PasEnAttenteTrouvees() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("payé", 1.5, new Date(), 1);

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : Valider préparation d'un plat d'une commande - cas 4 : aucune commande trouvée dans la base")
    void testValiderPreparationCas4PasTrouvees() {
        //On se connecte en tant que cuisiner.
        seConnecterEnCuisinier();

        //On simule le scénario de validation.
        CommandeControleur.validerPreparation();
    }

    @Test
    @DisplayName("Test : Valider le service d'une ligne de commande - cas 1 : validation réussie")
    void testValiderServiceCas1BienFait() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);
        ajouterLigneCommande("prêt", 1, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_service_cas_1.txt");

        int nbCommandesEnAttenteAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'en attente'", Commande.class);
        int nbLignesCommandePretesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'prêt'", LigneCommande.class);
        assertEquals(1, nbCommandesEnAttenteAvant);
        assertEquals(1, nbLignesCommandePretesAvant);

        //On simule le scénario de validation.
        CommandeControleur.validerService();

        int nbCommandesEnAttenteApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'en attente'", Commande.class);
        int nbLignesCommandePretesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'prêt'", LigneCommande.class);
        assertEquals(0, nbCommandesEnAttenteApres);
        assertEquals(0, nbLignesCommandePretesApres);
        int nbCommandesServiesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);
        int nbLignesCommandeServiesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'servi'", LigneCommande.class);
        assertEquals(1, nbCommandesServiesApres);
        assertEquals(1, nbLignesCommandeServiesApres);
    }

    @Test
    @DisplayName("Test : Valider le service d'une ligne de commande - cas 2 : validation correcte")
    void testValiderServiceCas2Correcte() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);
        ajouterLigneCommande("prêt", 1, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_service_cas_2.txt");

        Commande commandeAvant = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("en attente", commandeAvant.getEtat());

        LigneCommande ligneCommandeAvant = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("prêt", ligneCommandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerService();

        LigneCommande ligneCommandeApres = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("servi", ligneCommandeApres.getEtat());

        Commande commandeApres = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("servi", commandeApres.getEtat());
    }

    @Test
    @DisplayName("Test : Valider le service d'une ligne de commande - cas 3 : validation réussie et commande reste à en attente")
    void testValiderServiceCas3PlatPasPret() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterPlaceEtat("occupé");
        ajouterCategorie("catégorie");
        ajouterPlat("plat 1", 1.5, 1, 1);
        ajouterPlat("plat 2", 1.5, 1, 1);
        ajouterCommande("en attente", 1.5, new Date(), 1);
        ajouterLigneCommande("prêt", 1, 1, 1);
        ajouterLigneCommande("prêt", 1, 2, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_service_cas_3.txt");

        Commande commandeAvant = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("en attente", commandeAvant.getEtat());

        LigneCommande ligneCommandeAvant = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("prêt", ligneCommandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerService();

        Commande commandeApres = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1",Commande.class);
        assertEquals("en attente", commandeApres.getEtat());

        LigneCommande ligneCommandeApres = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", LigneCommande.class);
        assertEquals("servi", ligneCommandeApres.getEtat());
    }

    @Test
    @DisplayName("Test : Valider le service d'une ligne de commande - cas 4 : aucune commande en attente trouvée")
    void testValiderServiceCas4Pastrouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("payé", 1.5, new Date(), 1);

        //On simule le scénario de validation.
        CommandeControleur.validerService();
    }

    @Test
    @DisplayName("Test : Valider le service d'une ligne de commande - cas 5 : aucune commande trouvée")
    void testValiderServiceCas5PasTrouvees() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario de validation.
        CommandeControleur.validerService();
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 1 : validation réussie")
    void testValiderPaiementCas1BienFait() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("servi", 1.5, new Date(), 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_paiement_cas_1.txt");

        int nbCommandesServiesAvant = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);
        assertEquals(1, nbCommandesServiesAvant);

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();

        int nbCommandesServiesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);
        assertEquals(0, nbCommandesServiesApres);
        int nbCommandesPayeesApres = orm.compterNUpletsAvecPredicat("WHERE ETAT = 'payé'", Commande.class);
        assertEquals(1, nbCommandesPayeesApres);
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 2 : validation correcte")
    void testValiderPaiementCas2Correcte() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("servi", 1.5, new Date(), 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/commande_test/valider_paiement_cas_2.txt");

        Commande commandeAvant = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Commande.class);
        assertEquals("servi", commandeAvant.getEtat());

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();

        Commande commandeApres = (Commande) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Commande.class);
        assertEquals("payé", commandeApres.getEtat());
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 3 : aucune commande servie trouvée")
    void testValiderPaiementCas3PasServiesTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 1.5, 1, 1);
        ajouterPlaceEtat("occupé");
        ajouterCommande("en attente", 1.5, new Date(), 1);

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();
    }

    @Test
    @DisplayName("Test : Valider paiement d'une commande - cas 4 : aucune commande  trouvée dans la base")
    void testValiderPaiementCas4PasTrouvees() {
        //On se connecte en tant que maitre d'hotel.
        seConnecterEnMaitreHotel();

        //On simule le scénario de validation.
        CommandeControleur.validerPaiement();
    }
}