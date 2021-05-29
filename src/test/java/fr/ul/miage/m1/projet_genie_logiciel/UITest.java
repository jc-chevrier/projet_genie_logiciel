package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UI")
public class UITest extends GlobalTest {
    /**
     * Réinitialiser les tables utilisées dans les tests
     * de cette classe.
     */
    static void reinitialiserTables() {
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
    @DisplayName("Test : afficher si prédicat vrai - cas 1 : vrai")
    void testAfficherSiPredicatVraiCas1Vrai() {
        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiPredicatVrai(true, "message - vrai");
        assertTrue(predicat);
    }

    @Test
    @DisplayName("Test : afficher si prédicat vrai - cas 2 : faux")
    void testAfficherSiPredicatVraiCas2Faux() {
        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiPredicatVrai(false, "message");
        assertFalse(predicat);
    }

    @Test
    @DisplayName("Test : afficher si nombre nul - cas 1 : nul")
    void testAfficherSiNombreNulCas1Nul() {
        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiNombreNul(0, "message - nul");
        assertTrue(predicat);
    }

    @Test
    @DisplayName("Test : afficher si nombre nul - cas 2 : non nul")
    void testAfficherSiNombreNulCas2NonNul() {
        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiNombreNul(6, "message");
        assertFalse(predicat);
    }

    @Test
    @DisplayName("Test : afficher si liste n-uplets vide - cas 1 : vide")
    void testAfficherSiListeNUpletsVideCas1Vide() {
        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiListeNUpletsVide(new ArrayList<Entite>(), "message - vide");
        assertTrue(predicat);
    }

    @Test
    @DisplayName("Test : afficher si liste n-uplets vide - cas 2 : non vide")
    void testAfficherSiListeNUpletsVideCas2Vide() {
        //Ajout d'unités.
        Unite unite1 = ajouterUnite("unité 1");
        Unite unite2 = ajouterUnite("unité 2");
        Unite unite3 = ajouterUnite("unité 3");

        //On simule le scénario d'affichage.
        boolean predicat = ui.afficherSiListeNUpletsVide(Arrays.asList(unite1, unite2, unite3), "message");
        assertFalse(predicat);
    }

    @Test
    @DisplayName("Test : poser question - chaîne de caractères : cas 1 - simple")
    void testPoserQuestionChaineCas1Simple() {
        //On simule les saisies de chaine de caractères dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_chaine_cas_1.txt");

        //On simule le scénario de saisie d'une chaîne de caractères.
        String chaine = ui.poserQuestion("Saisie une chaine :",  UI.REGEX_CHAINE_DE_CARACTERES);
        assertEquals("chaîne simple", chaine);
    }

    @Test
    @DisplayName("Test : poser question - chaîne de caractères : cas 2 - tout pile, égale à 50 caractères")
    void testPoserQuestionChaineCas2ToutPileEgale50() {
        //On simule les saisies de chaine de caractères dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_chaine_cas_2.txt");

        //On simule le scénario de saisie d'une chaîne de caractères.
        String chaine = ui.poserQuestion("Saisie une chaine :",  UI.REGEX_CHAINE_DE_CARACTERES);
        assertEquals("01234567890123456789012345678901234567890123456789", chaine);
    }

    @Test
    @DisplayName("Test : poser question - chaîne de caractères : cas 3 - trop longue, supérieure à 50 caractères")
    void testPoserQuestionChaineCas3TropLongueSup50() {
        //On simule les saisies de chaine de caractères dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_chaine_cas_3.txt");

        //On simule le scénario de saisie d'une chaîne de caractères.
        String chaine = ui.poserQuestion("Saisie une chaine :",  UI.REGEX_CHAINE_DE_CARACTERES);
        assertEquals("chaîne", chaine);
    }

    @Test
    @DisplayName("Test : poser question - chaîne de caractères : cas 4 - vide")
    void testPoserQuestionChaineCas4Vide() {
        //On simule les saisies de chaine de caractères dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_chaine_cas_4.txt");

        //On simule le scénario de saisie d'une chaîne de caractères.
        String chaine = ui.poserQuestion("Saisie une chaine :",  UI.REGEX_CHAINE_DE_CARACTERES);
        assertEquals("chaîne", chaine);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 1 - entier")
    void testPoserQuestionDecimalCas1Entier() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_1.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(5, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 2 - une seule décimale")
    void testPoserQuestionDecimalCas2UniqueDecimale() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_2.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(4.2, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 3 - plusieurs décimales")
    void testPoserQuestionDecimalCas3PlusieursDecimales() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_3.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(8.95, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 4 - tout pile 3 décimales")
    void testPoserQuestionDecimalCas4ToutPile3Decimales() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_4.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(3.156, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 4 - plus de 3 décimales")
    void testPoserQuestionDecimalCas5Sup3Decimales() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_5.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(2, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 6 - tout pile 5 entiers")
    void testPoserQuestionDecimalCas6ToutPile5Entiers() {
        //On simule les saisies de nombre décimal dans ce fichier.
       chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_6.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(38887, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 7 - plus de 5 entiers")
    void testPoserQuestionDecimalCas7Sup5Entiers() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_7.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(1, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 8 - vide")
    void testPoserQuestionDecimalCas8Vide() {
        //On simule les saisies de nombre décimal dans ce fichier.c
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_8.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(6, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 9 - chaîne de caractères")
    void testPoserQuestionDecimalCas9Chaine() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_9.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(5, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - nombre décimal : cas 10 - négatif")
    void testPoserQuestionDecimalCas10Negatif() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_decimal_cas_10.txt");

        //On simule le scénario de saisie d'un nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un nombre décimal :",  UI.REGEX_DECIMAL_POSITIF);
        assertEquals(9, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 1 - une seule décimale")
    void testPoserQuestionGrandDecimalCas1UniqueDecimale() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_1.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF);
        assertEquals(6.1, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 2 - plusieurs décimales")
    void testPoserQuestionGrandDecimalCas2PlusieursDecimales() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_2.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF);
        assertEquals(7.36, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 3 - tout pile 13 entiers")
    void testPoserQuestionGrandDecimalCas3ToutPile13Entiers() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_3.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF);
        assertEquals(0123456789012D, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 4 - plus de 13 entiers")
    void testPoserQuestionGrandDecimalCas4Sup13Entiers() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_4.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF);
        assertEquals(8, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 5 - négatif impossible")
    void testPoserQuestionGrandDecimalCas5() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_5.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF);
        assertEquals(4, nombreDecimal);
    }

    @Test
    @DisplayName("Test : poser question - grand nombre décimal : cas 6 - négatif possible")
    void testPoserQuestionGrandDecimalCas6() {
        //On simule les saisies de nombre décimal dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_grand_decimal_cas_6.txt");

        //On simule le scénario de saisie d'un grand nombre décimal.
        double nombreDecimal = ui.poserQuestionDecimal("Saisie un grand nombre décimal :",  UI.REGEX_GRAND_DECIMAL_POSITIF_OU_NEGATIF);
        assertEquals(-9, nombreDecimal);
    }

    @Test()
    @DisplayName("Test : poser question - liste options : cas 2 : vide")
    void testPoserQuestionOptionsCas2Vide() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            //On simule le scénario du choix d'une option.
            ui.poserQuestionListeOptions("Sélectionner une option :", new ArrayList<String>());
        });
    }

    @Test
    @DisplayName("Test : poser question - liste options n-uplets : cas 1 : réussi")
    void testPoserQuestionOptionsNUpletsCas1Reussi() {
        //Ajout d'unités.
        Unite unite1 = ajouterUnite("unité 1");
        Unite unite2 = ajouterUnite("unité 2");
        Unite unite3 = ajouterUnite("unité 3");

        //On simule les saisies de choix d'un n-uplet dans ce fichier.
        chargerSaisies("./saisies/ui_test/poser_question_options_nuplets_cas_1.txt");

        //On simule le scénario du choix d'un n-uplet.
        Entite uniteChoisie = ui.poserQuestionListeNUplets("Sélectionner un n-uplet :", Arrays.asList(unite1, unite2, unite3));
        assertEquals(3, uniteChoisie.getId());
    }

    @Test()
    @DisplayName("Test : poser question - liste options n-uplets : cas 2 : vide")
    void testPoserQuestionOptionsNupletsCas2Vide() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            //On simule le scénario du choix d'un n-uplet.
            ui.poserQuestionListeNUplets("Sélectionner un n-uplet :", new ArrayList<Entite>());
        });
    }
}