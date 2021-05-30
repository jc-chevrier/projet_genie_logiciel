package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.PlatControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Plat")
public class PlatTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
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
    @DisplayName("Test : lister les plats - cas 1 : plats trouvés")
    void testListerCas1Trouvees() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @DisplayName("Test : lister les plats - cas 2 : aucun plat trouvé")
    void testListerCas2PasTrouvees() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario de listing.
        PlatControleur.lister();
    }

    @Test
    @DisplayName("Test : ajouter un plat - cas 1 : plat bien ajouté")
    void testAjouterCas1BienAjoute() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient 1", 5.0, 1);
        ajouterIngredient("ingrédient 2", 5.0, 1);

        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/plat_test/ajouter_cas_1.txt");

        int nbPlatsAvant = orm.compterTousLesNUplets(Plat.class);

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();

        //Un plat a du être inséré.
        int nbPlatsApres = orm.compterTousLesNUplets(Plat.class);
        assertEquals(nbPlatsAvant + 1, nbPlatsApres);
    }

    @Test
    @DisplayName("Test : ajouter un plat - cas 2 : plat ajouté correct")
    void testAjouterCas2Correct() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie 1");
        ajouterCategorie("catégorie 2");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient 1", 5.0, 1);
        ajouterIngredient("ingrédient 2", 5.0, 1);

        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/plat_test/ajouter_cas_2.txt");

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();

        //Le plat inséré doit avoir certaines caractéristiques données.
        Plat platInsere = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals("ajouté", platInsere.getlibelle());
        assertEquals(5, platInsere.getPrix());
        assertEquals(0, platInsere.getCarte());
        assertEquals(2, platInsere.getIdCategorie());
    }

    @Test
    @DisplayName("Test : ajouter un plat - cas 3 : aucune catégorie trouvée")
    void testAjouterCas3() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();
    }

    @Test
    @DisplayName("Test : ajouter un plat - cas 4 : aucun ingrédient trouvé")
    void testAjouterCas4() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");

        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/plat_test/ajouter_cas_4.txt");

        //On simule le scénario d'ajout.
        PlatControleur.ajouter();
    }

    @Test
    @DisplayName("Test : modifier un plat - cas 1 : plat bien modifié")
    void testModifierCas1() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie 1");
        ajouterCategorie("catégorie 2");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 5.0, 1);
        ajouterPlat("plat", 3.0, 1, 2);

        //On simule les saisies d'ajout dans ce fichier.
        chargerSaisies("./saisies/plat_test/modifier_cas_1.txt");

        //On simule le scénario de modification.
        PlatControleur.modifier();

        //Le plat modifié doit avoir ce libellé : "libellé modifié".
        Plat platModifie = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals("modifié", platModifie.getlibelle());
        assertEquals(7, platModifie.getPrix());
        assertEquals(0, platModifie.getCarte());
        assertEquals(1, platModifie.getIdCategorie());
    }

    @Test
    @DisplayName("Test : modifier un plat - cas 2 : aucun plat trouvé")
    void testModifierCas2() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario de modification.
        PlatControleur.modifier();
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 1 : plat bien supprimé")
    void testSupprimerCas1BienSupprimee() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/plat_test/supprimer_cas_1.txt");

        int nbPlatsAvant = orm.compterTousLesNUplets(Plat.class);
        assertEquals(1, nbPlatsAvant);

        //On simule le scénario de suppression.
        PlatControleur.supprimer();

        //Un plat a du être supprimé.
        int nbPlatsApres = orm.compterTousLesNUplets(Plat.class);
        assertEquals(0, nbPlatsApres);
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 2 : plat supprimé correct")
    void testSupprimerCas2Correct() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule les saisies de la suppression dans ce fichier.
        chargerSaisies("./saisies/plat_test/supprimer_cas_2.txt");

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertNotNull(platAvant);

        //On simule le scénario de suppression.
        PlatControleur.supprimer();

        //Un plat a du être supprimé.
        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertNull(platApres);
    }

    @Test
    @DisplayName("Test : supprimer un plat - cas 3 : aucun plat trouvé")
    void testSupprimerCas3PasTrouve() {
        //On se connecte en tant que cuisinier.
        seConnecterEnCuisinier();

        //On simule le scénario de suppression.
        PlatControleur.supprimer();
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 1 : plat bien ajouté")
    void testAjouterACarteCas1BienAjoute() {
        //On se connecte en tant que directeur.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/plat_test/ajouter_carte_cas_1.txt");

        int nbPlatsAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteAvant = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        //Un plat a du être ajouté à la carte.
        int nbPlatsApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 0", Plat.class);
        int nbPlatsDansCarteApres = orm.compterNUpletsAvecPredicat("WHERE CARTE = 1", Plat.class);
        assertEquals(nbPlatsAvant - 1, nbPlatsApres);
        assertEquals(nbPlatsDansCarteAvant + 1, nbPlatsDansCarteApres);
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 2 : plat ajouté correct")
    void testAjouterACarteCas2Correct() {
        //On se connecte en tant que directeur.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/plat_test/ajouter_carte_cas_2.txt");

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(0, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();

        //Un plat a du être ajouté à la carte.
        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(1, platApres.getCarte());
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour- cas 3 : aucun plat trouvé - tous les plats déjà dans carte")
    void testAjouterACarteCas3PasTrouveDejaDansCarteCarte() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 1, 1);

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }

    @Test
    @DisplayName("Test : ajouter un plat à la carte du jour - cas 4 : aucun plat trouvé - table plat vide")
    void testAjouterACarteCas4PasTrouveTableVide() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnDirecteur();

        //On simule le scénario de validation.
        PlatControleur.ajouterACarte();
    }

    @Test
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 1 : plat bien supprimé")
    void testSupprimerDeCarteCas1BienSupprime() {
        //On se connecte en tant que directeur.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/plat_test/supprimer_carte_cas_1.txt");

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
    @DisplayName("Test : supprimer un plat de la carte du jour- cas 2 : plat supprimé correct")
    void testSupprimerDeCarteCas2Correct() {
        //On se connecte en tant que directeur.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 1, 1);

        //On simule les saisies de validation dans ce fichier.
        chargerSaisies("./saisies/plat_test/supprimer_carte_cas_2.txt");

        Plat platAvant = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(1, platAvant.getCarte());

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();

        Plat platApres = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Plat.class);
        assertEquals(0, platApres.getCarte());
    }

    @Test
    @DisplayName("Test : supprimer un plat à la carte du jour- cas 3 : aucun plat trouvé - carte vide")
    void testSupprimerDeCarteCas3PasTrouveCarteVide() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnDirecteur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 2.5, 0, 1);

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }

    @Test
    @DisplayName("Test : supprimer un plat de la carte du jour - cas 4 : aucun plat trouvé - table plat vide")
    void testSupprimerDeCarteCas4PasTrouveTableVide() {
        //On se connecte en tant qu'assistant de service.
        seConnecterEnDirecteur();

        //On simule le scénario de validation.
        PlatControleur.supprimerDeCarte();
    }

    @Test
    @DisplayName("Test : lister les plats de la carte - cas 1 : plats trouvés")
    void testListerCarteCas1Trouves() {
        //On se connecte en tant que cuisinier.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 3.0, 1, 1);

        //On simule le scénario de lister.
        PlatControleur.listerCarte();

    }

    @Test
    @DisplayName("Test : lister les plats de la carte - cas 1 : aucun plat dans la carte - carte vide")
    void testListerCarteCas2PasTrouveCarteVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterPlat("plat", 3.0, 0, 1);

        //On simule le scénario de lister.
        PlatControleur.listerCarte();
    }

    @Test
    @DisplayName("Test : lister les plats de la carte - cas 2 : aucun plat trouvé - table plat vide")
    void testListerCarteCas3PasTrouveTableVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario de lister.
        PlatControleur.listerCarte();
    }

    @Test
    @DisplayName("Test : lister les plats disponibles de la carte - cas 1 : plats trouvés")
    void testListerDisponiblesCarteCasTrouve() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 10.0, 1);
        ajouterPlat("plat", 3.0, 1, 1);
        ajouterPlatIngredient(2.0, 1, 1);

        //On simule le scénario de lister.
        PlatControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les plats disponibles de la carte - cas 1 : aucun plat trouvé - carte vide")
    void testListerrDisponiblesCarteCas1PasTrouveCarteVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 10.0, 1);
        ajouterPlat("plat", 3.0, 0, 1);
        ajouterPlatIngredient(2.0, 1, 1);

        //On simule le scénario de lister.
        PlatControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les plats disponibles de la carte - cas 2 : aucun plat trouvé - stock insuffisant")
    void testListerrDisponiblesCarteCas2PasTrouveStockInsuffisant() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("catégorie");
        ajouterUnite("unité");
        ajouterIngredient("ingrédient", 10.0, 1);
        ajouterIngredient("ingrédient", 0.0, 1);
        ajouterPlat("plat", 3.0, 1, 1);
        ajouterPlatIngredient(2.0, 1, 1);
        ajouterPlatIngredient(2.0, 1, 2);

        //On simule le scénario de lister.
        //Dans ce cas aucun plat est lister car un des ingrédients composant le plat n'est pas disponible.
        PlatControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : lister les plats disponible de la carte - cas 3 : aucun plat trouvé - table vide")
    void testListerrDisponiblesCarteCas3PasTrouveTableVide() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule le scénario de lister les plats disponibles de la carte.
        PlatControleur.listerDisponiblesCarte();
    }

    @Test
    @DisplayName("Test : chercher un plat avec son libellé - cas 1 : un plat trouvé avec libellé partiel")
    void testChercherAvecLibelleCas1UniqueTrouve() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("Cuisine française");
        ajouterPlat("Nouille", 5.5, 1, 1);
        ajouterPlat("Ratatouille", 5.5, 1, 1);

        //On simule les saisies de recherche dans ce fichier.
        chargerSaisies("./saisies/plat_test/chercher_cas_1.txt");

        //On simule le scénario de recherche.
        PlatControleur.chercherAvecLibelle();
    }

    @Test
    @DisplayName("Test : chercher un plat avec son libellé - cas 2 : plusieurs plats trouvés avec libellé partiel")
    void testChercherAvecLibelleCas2PlusieursTrouves() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("Cuisine française");
        ajouterCategorie("Cuisine algérienne");
        ajouterPlat("Cassoulet", 5.5, 1, 1);
        ajouterPlat("Taboulé", 5.5, 1, 2);

        //On simule les saisies de recherche dans ce fichier.
        chargerSaisies("./saisies/plat_test/chercher_cas_2.txt");

        //On simule le scénario de recherche.
        PlatControleur.chercherAvecLibelle();
    }

    @Test
    @DisplayName("Test : chercher un plat avec son libellé - cas 3 : un plat trouvé avec la casse")
    void testChercherAvecLibelleCas3TrouveCasse() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("Cuisine française");
        ajouterPlat("pâte", 5.5, 1, 1);

        //On simule les saisies de recherche dans ce fichier.
        chargerSaisies("./saisies/plat_test/chercher_cas_3.txt");

        //On simule le scénario de recherche.
        PlatControleur.chercherAvecLibelle();
    }

    @Test
    @DisplayName("Test : chercher un plat avec son libellé - cas 4 : un plat trouvé avec libellé complet")
    void testChercherAvecLibelleCas4UniqueTrouveLiblelComplet() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        ajouterCategorie("Cuisine française");
        ajouterPlat("Pâtes à la bolognaise", 5.5, 1, 1);

        //On simule les saisies de recherche dans ce fichier.
        chargerSaisies("./saisies/plat_test/chercher_cas_4.txt");

        //On simule le scénario de recherche.
        PlatControleur.chercherAvecLibelle();
    }

    @Test
    @DisplayName("Test : chercher un plat avec son libellé - cas 5 : aucun plat trouvé")
    void testChercherAvecLibelleCas5PasTrouve() {
        //On se connecte en tant que serveur.
        seConnecterEnServeur();

        //On simule les saisies de recherche dans ce fichier.
        chargerSaisies("./saisies/plat_test/chercher_cas_5.txt");

        //On simule le scénario de recherche.
        PlatControleur.chercherAvecLibelle();
    }
}