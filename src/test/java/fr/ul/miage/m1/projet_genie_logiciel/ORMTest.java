package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ORM")
public class ORMTest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables(){
        //On réinitialise la table place.
        orm.reinitialiserTable(Place.class);
        //On réinitialise la table unité.
        orm.reinitialiserTable(Unite.class);
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
    @DisplayName("Test : chercher tous les n-uplets - cas n-uplets trouvés")
    void testChercherTousLesNUpletsTrouves() {
        ajouterUnite("unité");

        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);

        //Une unité doit avoir été trouvée.
        assertFalse(liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher tous les n-uplets - cas n-uplets non trouvés")
    void testChercherTousLesNUpletsNonTrouves() {
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);

        //Aucune unité doit avoir été trouvée.
        assertTrue(liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher le n-uplet avec un predicat - cas n-uplet trouvé")
    void testChercherNUpletAvecPredicatCasTrouve(){
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");

        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);

        //Une unité doit avoir été trouvée.
        assertNotNull(nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas n-uplet non trouvé")
    void testChercherNUpletAvecPredicatCasNonTrouve(){
        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);

        //Aucune unité doit avoir été trouvée.
        assertNull(nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas  n-uplet trouvé ")
    void testChercherNUpletAvecPredicatCasCorrect(){
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");

        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID = 2", Unite.class);

        //Une unité doit avoir été trouvée.
        assertEquals(2, nUplet.getId());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets trouvés")
    void testChercherNUpletsAvecPredicatCasTrouve() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");
        ajouterUnite("unité 4");
        ajouterUnite("unité 5");
        ajouterUnite("unité 6");

        List<Entite> liste = orm.chercherNUpletsAvecPredicat("WHERE ID IN (1,3)", Unite.class);

        //2 unités doivent avoir été trouvée.
        assertEquals(2, liste.size());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets non trouvés")
    void testChercherNUpletsAvecPredicatCasNonTrouve() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");
        ajouterUnite("unité 4");
        ajouterUnite("unité 5");
        ajouterUnite("unité 6");

        List<Entite> liste = orm.chercherNUpletsAvecPredicat("WHERE ID IN (9,11,13)", Unite.class);

        //Aucune unité doit avoir été trouvée.
        assertEquals(true, liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets corrects")
    void testChercherNUpletsAvecPredicatCasCorrects() {
        ajouterUnite("unité 1");
        ajouterUnite("unité 2");
        ajouterUnite("unité 3");
        ajouterUnite("unité 4");

        List<Entite> list = orm.chercherNUpletsAvecPredicat("WHERE ID IN (1,4)", Role.class);

        //Deux unités doivent avoir été trouvées.
        assertEquals(1,list.get(0).getId());
        assertEquals(4,list.get(1).getId());
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasInsertion() {
        Unite unite = new Unite();
        unite.setLibelle("à insérer");
        orm.persisterNUplet(unite);

        //Le libellé de l'unité doit avoir été insérée.
        Unite nUpletInsere = (Unite) orm.chercherNUpletAvecPredicat("WHERE LIBELLE = 'à insérer'", Unite.class);
        assertNotNull(nUpletInsere);
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasMiseAJour() {
        ajouterUnite("à modifier");

        Unite nUpletAMettreAJour = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        nUpletAMettreAJour.setLibelle("libellé modifié");
        orm.persisterNUplet(nUpletAMettreAJour);

        //Le libellé de l'unité doit avoir été modifié.
        Unite nUpletMisAJour = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertEquals("libellé modifié", nUpletMisAJour.getLibelle());
    }


    @Test
    @DisplayName("Test : supprimer un n-uplet avec un prédicat")
    void testSupprimerNUpletAvecPredicat() {
        ajouterUnite("à supprimer");
        ajouterUnite("à supprimer");
        ajouterUnite("à supprimer");
        ajouterUnite("pas à supprimer");

        List<Entite> nUplets = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(4, nUplets.size());

        orm.supprimerNUpletsAvecPredicat("WHERE LIBELLE = 'à supprimer'", Unite.class);

        //Il ne doit rester qu'une unité.
        nUplets = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(1, nUplets.size());
        assertEquals("pas à supprimer", ((Unite) nUplets.get(0)).getLibelle());
    }

    @Test
    @DisplayName("Test : supprimer un n-uplet")
    void testSupprimerNUplet() {
        ajouterUnite("à supprimer");

        Unite nUpletASupprimer = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNotNull(nUpletASupprimer);

        orm.supprimerNUplet(nUpletASupprimer);

        //L"unité doit avoir été supprimée.
        Unite nUpletSupprime = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNull(nUpletSupprime);
    }

    @Test
    @DisplayName("Test : supprimer tous les n-uplets")
    void testSupprimerTousLesNUplets() {
        ajouterUnite("à supprimer");
        ajouterUnite("à supprimer");

        List<Entite> nUpletsASupprimer = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(2, nUpletsASupprimer.size());

        orm.supprimerTousLesNUplets(Unite.class);

        //La table des unités doit être vide.
        List<Entite> nUpletsSupprimes = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(0, nUpletsSupprimes.size());
    }

    @Test
    @DisplayName("Test : réinitialiser la séquence des ids à n")
    void testReinitialiserSequenceIdAN() {
        orm.supprimerTousLesNUplets(Unite.class);
        orm.reinitialiserSequenceId(300, Unite.class);

        Unite unite = ajouterUnite("unité avec id 300");

        //L"unité doit avoir un id 300.
        assertEquals(300, unite.getId());
    }

    @Test
    @DisplayName("Test : réinitialiser la séquence des ids à 1")
    void testReinitialiserSequenceIdA1() {
        ajouterUnite("à supprimer");
        ajouterUnite("à supprimer");

        orm.supprimerTousLesNUplets(Unite.class);
        orm.reinitialiserSequenceIdA1(Unite.class);

        Unite unite = ajouterUnite("unité avec id 1");

        //L"unité doit avoir un id 1.
        assertEquals(1, unite.getId());
    }
}