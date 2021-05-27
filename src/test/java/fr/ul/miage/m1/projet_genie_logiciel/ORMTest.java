package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ORM")
public class ORMTest {
    private static ORM orm;

    static void reinitialiserTables(){
        //On réinitialise la table place.
        orm.reinitialiserTable(Place.class);
        //On réinitialise la table unité.
        orm.reinitialiserTable(Unite.class);
    }

    @BeforeAll
    static void setUp() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
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
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);

        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(false, liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher tous les n-uplets - cas n-uplets non trouvés")
    void testChercherTousLesNUpletsNonTrouves() {
        List<Entite> liste = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(true, liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher le n-uplet avec un predicat - cas n-uplet trouvé")
    void testChercherNUpletAvecPredicatCasTrouve(){
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);

        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=1",Unite.class);
        assertNotNull(nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas n-uplet non trouvé")
    void testChercherNUpletAvecPredicatCasNonTrouve(){
        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=-7",Unite.class);
        assertEquals(null, nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas  n-uplet trouvé ")
    void testChercherNUpletAvecPredicatCasCorrect(){
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);

        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=1",Unite.class);
        assertEquals(1, nUplet.getId());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets trouvés")
    void testChercherNUpletsAvecPredicatCasTrouve() {
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);
        //On ajoute une deuxième unité à lister.
        Unite unite2 = new Unite();
        unite2.setLibelle("libellé");
        orm.persisterNUplet(unite2);
        List<Entite> liste = orm.chercherNUpletsAvecPredicat("WHERE ID IN (1,2)", Unite.class);
        assertEquals(2,liste.size());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets non trouvés")
    void testChercherNUpletsAvecPredicatCasNonTrouve() {
        List<Entite> liste = orm.chercherNUpletsAvecPredicat("WHERE ID IN (-2,-3)", Unite.class);
        assertEquals(true,liste.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets corrects")
    void testChercherNUpletsAvecPredicatCasCorrects() {
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);
        //On ajoute une deuxième unité à lister.
        Unite unite2 = new Unite();
        unite2.setLibelle("libellé");
        orm.persisterNUplet(unite2);

        List<Entite> list = orm.chercherNUpletsAvecPredicat("WHERE ID IN (1,2)", Role.class);
        assertEquals(1,list.get(0).getId());
        assertEquals(2,list.get(1).getId());
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasInsertion() {
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);
        Unite nUpletInsere = (Unite) orm.chercherNUpletAvecPredicat("WHERE LIBELLE = 'libellé'", Unite.class);
        assertEquals(true, nUpletInsere != null);
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasMiseAJour() {
        //On ajoute une unité à lister.
        Unite unite1 = new Unite();
        unite1.setLibelle("libellé");
        orm.persisterNUplet(unite1);

        Unite nUpletAMettreAJour = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        nUpletAMettreAJour.setLibelle("libellé modifié");
        orm.persisterNUplet(nUpletAMettreAJour);

       Unite nUpletMisAJour = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertEquals("libellé modifié", nUpletMisAJour.getLibelle());
    }


    @Test
    @DisplayName("Test : supprimer un n-uplet avec un prédicat")
    void testSupprimerNUpletAvecPredicat() {
        Unite unite = new Unite();
        unite.setLibelle("libellé unité à suppprimer");
        orm.persisterNUplet(unite);
        unite = new Unite();
        unite.setLibelle("libellé unité à suppprimer");
        orm.persisterNUplet(unite);
        unite = new Unite();
        unite.setLibelle("libellé unité pas à suppprimer");
        orm.persisterNUplet(unite);
        unite = new Unite();
        unite.setLibelle("libellé unité à suppprimer");
        orm.persisterNUplet(unite);

        List<Entite> nUplets = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(4, nUplets.size());

        orm.supprimerNUpletsAvecPredicat("WHERE LIBELLE = 'libellé unité à suppprimer'", Unite.class);

        nUplets = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(1, nUplets.size());
        assertEquals("libellé unité pas à suppprimer", ((Unite) nUplets.get(0)).getLibelle());
    }

    @Test
    @DisplayName("Test : supprimer un n-uplet")
    void testSupprimerNUplet() {
        //On ajoute une unité à lister.
        Unite unite = new Unite();
        unite.setLibelle("libellé");
        orm.persisterNUplet(unite);

        Unite nUpletASupprimer = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNotNull(nUpletASupprimer);

        orm.supprimerNUplet(nUpletASupprimer);

        Unite nUpletSupprime = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = 1", Unite.class);
        assertNull(nUpletSupprime);
    }

    @Test
    @DisplayName("Test : supprimer tous les n-uplets")
    void testSupprimerTousLesNUplets() {
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);
        place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        List<Entite> nUpletsASupprimer = orm.chercherTousLesNUplets(Place.class);
        assertEquals(2, nUpletsASupprimer.size());

        orm.supprimerTousLesNUplets(Place.class);

        List<Entite> nUpletsSupprimes = orm.chercherTousLesNUplets(Place.class);
        assertEquals(0, nUpletsSupprimes.size());
    }

    @Test
    @DisplayName("Test : réinitialiser la séquence des ids à n")
    void testReinitialiserSequenceIdAN() {
        orm.supprimerTousLesNUplets(Place.class);
        orm.reinitialiserSequenceId(300, Place.class);

        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        assertEquals(300, place.getId());
    }

    @Test
    @DisplayName("Test : réinitialiser la séquence des ids à 1")
    void testReinitialiserSequenceIdA1() {
        Place place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);
        place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        orm.supprimerTousLesNUplets(Place.class);
        orm.reinitialiserSequenceIdA1(Place.class);

        place = new Place();
        place.setEtat("libre");
        orm.persisterNUplet(place);

        assertEquals(1, place.getId());
    }
}