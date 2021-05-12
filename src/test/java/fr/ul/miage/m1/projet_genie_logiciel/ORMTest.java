package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import org.junit.jupiter.api.*;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Role;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ORM")
public class ORMTest {
    private static ORM orm;

    @BeforeAll
    static void setUp() {
        ORM.CONFIGURATION_FILENAME = "./configuration/configuration_bdd_test.properties";
        orm = ORM.getInstance();
    }

    @Test
    @DisplayName("Test : chercher tous les n-uplets - cas n-uplets trouvés")
    void testChercherTousLesNUpletsTrouves() {
        List<Entite> list = orm.chercherTousLesNUplets(Role.class);
        assertEquals(false, list.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher tous les n-uplets - cas n-uplets non trouvés")
    void testChercherTousLesNUpletsNonTrouves() {
        List<Entite> list = orm.chercherTousLesNUplets(Unite.class);
        assertEquals(true, list.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher le n-uplet avec un predicat - cas n-uplet trouvé")
    void testChercherNUpletAvecPredicatCasTrouve(){
        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=2",Role.class);
        assertNotNull(nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas n-uplet non trouvé")
    void testChercherNUpletAvecPredicatCasNonTrouve(){
        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=-7",Role.class);
        assertEquals(null, nUplet);
    }

    @Test
    @DisplayName("Test : chercher un n-uplet avec un predicat - cas  n-uplet trouvé ")
    void testChercherNUpletAvecPredicatCasCorrect(){
        Entite nUplet = orm.chercherNUpletAvecPredicat("WHERE ID=2",Role.class);
        assertEquals(2, nUplet.getId());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets trouvés")
    void testChercherNUpletsAvecPredicatCasTrouve() {
        List<Entite> list = orm.chercherNUpletsAvecPredicat("WHERE ID IN (2,3)", Role.class);
        assertEquals(2,list.size());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets non trouvés")
    void testChercherNUpletsAvecPredicatCasNonTrouve() {
        List<Entite> list = orm.chercherNUpletsAvecPredicat("WHERE ID IN (-2,-3)", Role.class);
        assertEquals(true,list.isEmpty());
    }

    @Test
    @DisplayName("Test : chercher des n-uplets avec un predicat - cas n-uplets corrects")
    void testChercherNUpletsAvecPredicatCasCorrects() {
        List<Entite> list = orm.chercherNUpletsAvecPredicat("WHERE ID IN (2,3) ORDER BY ID", Role.class);
        assertEquals(2,list.get(0).getId());
        assertEquals(3,list.get(1).getId());
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasInsertion() {
        Role nUpletAInserer = new Role();
        nUpletAInserer.setLibelle("Test");
        orm.persisterNUplet(nUpletAInserer);
        Role nUpletInsere = (Role) orm.chercherNUpletAvecPredicat("WHERE LIBELLE = 'Test'", Role.class);
        assertEquals(true, nUpletInsere != null);
    }

    @Test
    @DisplayName("Test : faire persister un n-uplet - cas insérer un n-uplet")
    void testPersisterNUpletCasMiseAJour() {
        Role nUpletAMettreAJour = (Role) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Role.class);
        nUpletAMettreAJour.setLibelle("Maitre Hotel MAJ");
        orm.persisterNUplet(nUpletAMettreAJour);
        Role nUpletMisAJour = (Role) orm.chercherNUpletAvecPredicat("WHERE ID = 4", Role.class);
        assertEquals("Maitre Hotel MAJ", nUpletMisAJour.getLibelle());
    }


    @Test
    @DisplayName("Test : supprimer un n-uplet avec un prédicat")
    void testSupprimerNUpletAvecPredicat() {
        orm.chercherTousLesNUplets(Unite.class).forEach(orm::supprimerNUplet);

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
        Role role = new Role();
        role.setLibelle("libellé");
        orm.persisterNUplet(role);

        Role nUpletASupprimer = (Role) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Role.class);
        assertNotNull(nUpletASupprimer);

        orm.supprimerNUplet(nUpletASupprimer);

        Role nUpletSupprime = (Role) orm.chercherNUpletAvecPredicat("WHERE ID = 6", Role.class);
        assertNull(nUpletSupprime);
    }

    @Test
    @DisplayName("Test : supprimer tous les n-uplets")
    void testSupprimerTousLesNUplets() {
        orm.chercherTousLesNUplets(Place.class).forEach(orm::supprimerNUplet);

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