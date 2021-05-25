package fr.ul.miage.m1.projet_genie_logiciel.ui;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Programme utilitaire pour l'interface console.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class UI {
    //Délimiteur sur la console entre des contenus.
    public final static String DELIMITEUR = "-------------------------------------------------------------------------------------------------------";

    //Expression régulière des chaines de caractères.
    public final static String REGEX_CHAINE_DE_CARACTERES = ".{1,50}";
    //Expression régulière des nombres décimaux positifs.
    public final static String REGEX_DECIMAL_POSITIF = "[0-9]{1,5}|[0-9]{1,5}\\.{1}[0-9]{1,3}";
    //Expression régulière des grands nombres décimaux positifs.
    public final static String REGEX_GRAND_DECIMAL_POSITIF = "[0-9]{1,13}|[0-9]{1,13}\\.{1}[0-9]{1,3}";
    //Expression régulière des nombres décimaux positifs ou négatifs.
    public final static String REGEX_GRAND_DECIMAL_POSITIF_OU_NEGATIF = "-{0,1}(" + REGEX_DECIMAL_POSITIF + ")";

    //Singleton.
    private static UI UISingleton;

    //Lecteur des saisies de l'utilisateur.
    private Scanner scanner;

    //Utilisateur connecté.
    private Compte utilisateurConnecte;

    private UI() {
        scanner = new Scanner(System.in);
        utilisateurConnecte = null;
    }

    /**
     * Obtenir le singleton UI.
     *
     * @return
     */
    public static UI getInstance() {
        if(UISingleton == null) {
            UISingleton = new UI();
        }
        return UISingleton;
    }

    /**
     * Alias de System.out.println(...).
     *
     * @param contenu
     */
    public void afficher(@NotNull String contenu) {
        System.out.println(contenu);
    }

    /**
     * Afficher un contenu en ajoutant avant un saut avec le délimiteur,
     * et l'utilisateur connecté.
     *
     * @param contenu
     */
    public void afficherAvecDelimiteurEtUtilisateur(@NotNull String contenu) {
        afficher("\n" + UI.DELIMITEUR + "\n" + utilisateurConnecte.toSimpleString() + " " + contenu + "\n");
    }

    /**
     * Poser une question, en précisant :
     * - la question ;
     * - une expression régulière décrivant les réponses possibles autorisées ;
     * Tant que la réponse donnée est incorrecte, la fonction boucle récursivement.
     * La réponse correcte donnée est retournée.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @return
     */
    public String poserQuestion(@NotNull String question, @NotNull String reponsesPossiblesRegex) {
        afficher(question);
        String reponse = scanner.nextLine();
        //Cas trivial.
        if(reponse.matches("^" + reponsesPossiblesRegex + "$")) {
            return reponse;
        //Cas récursif.
        } else {
            afficher("La réponse donnée est incorrecte.\n");
            return poserQuestion(question, reponsesPossiblesRegex);
        }
    }

    /**
     * Poser une question dont la réponse est un entier
     * Cette fonction sert à factoriser la conversion de chaine de caractères vers entier.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @return
     */
    public int poserQuestionEntier(@NotNull String question, @NotNull String reponsesPossiblesRegex) {
        return Integer.parseInt(poserQuestion(question, reponsesPossiblesRegex));
    }

    /**
     * Poser une question dont la réponse est un nombre décimal
     * Cette fonction sert à factoriser la conversion de chaine de caractères vers nombre décimal.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @return
     */
    public double poserQuestionDecimal(@NotNull String question, @NotNull String reponsesPossiblesRegex) {
        return Double.parseDouble(poserQuestion(question, reponsesPossiblesRegex));
    }

    /**
     * Poser une question fermée (oui, non).
     *
     * @param question
     * @return
     */
    public boolean poserQuestionFermee(@NotNull String question) {
        question += " (oui/non)";
        String reponsesPossiblesRegex = "oui|non";
        String reponse = poserQuestion(question, reponsesPossiblesRegex) ;
        return reponse.equals("oui");
    }

    /**
     * Poser une question en proposant une liste d'options,
     * et obtenir l'indice de l'option sélectionnée.
     *
     * @param question
     * @param options
     * @return
     */
    public int poserQuestionListeOptions(@NotNull String question, @NotNull List<String> options) {
        String reponsesPossiblesRegex = "";
        int nbOptions = options.size();
        for(int index = 0; index < nbOptions; index++) {
            String option = options.get(index);
            question += "\n" + option + " (saisir " + (index + 1) + ")";
            reponsesPossiblesRegex += (index + 1) + "{1}" + ((index < (nbOptions - 1)) ? "|" : "");
        }
        int index = poserQuestionEntier(question, reponsesPossiblesRegex) - 1;
        return index;
    }

    /**
     * Poser une question en proposant une liste d'options
     * pour une liste de n-uplets, et obtenir l'id de
     * du n-uplet sélectionné.
     *
     * On peut préciser un formateur pour formater les
     * n-uplets en chaines de caractère.
     *
     * @param question
     * @param nUplets
     * @param formateur
     * @return
     */
    public int poserQuestionListeNUplets(@NotNull String question, @NotNull List<Entite> nUplets,
                                         Function<Entite, String> formateur) {
        String reponsesPossiblesRegex = "";
        int nbNUplets = nUplets.size();
        for(int index = 0; index < nbNUplets; index++) {
            Entite nUplet = nUplets.get(index);
            question += "\n" + (formateur == null ? nUplet : formateur.apply(nUplet)) +
                        " (saisir " + nUplet.getId() + ")";
            reponsesPossiblesRegex += nUplet.getId() + "{1}" + ((index < (nbNUplets - 1)) ? "|" : "");
        }
        int id = poserQuestionEntier(question, reponsesPossiblesRegex);
        return id;
    }

    /**
     * Poser une question en proposant une liste d'options
     * pour une liste de n-uplets, et obtenir l'id de
     * du n-uplet sélectionné.
     *
     * On utilise toString comme formateur des n-uplets en
     * chaines de caractère.
     *
     * @param question
     * @param nUplets
     * @return
     */
    public int poserQuestionListeNUplets(@NotNull String question, @NotNull List<Entite> nUplets) {
        return poserQuestionListeNUplets(question, nUplets, null);
    }

    /**
     * Afficher une liste de strings,
     * sans selection par la suite.
     *
     * @param elements
     */
    public void lister(@NotNull List<String> elements) {
        String contenu = "";
        int nbElements = elements.size();
        for(int index = 0; index < nbElements; index++) {
            String element = elements.get(index);
            contenu += element + ((index < (nbElements - 1)) ? "\n" : "");;
        }
        afficher(contenu);
    }

    /**
     * Afficher une liste de n-uplets,
     * sans selection par la suite.
     *
     * On peut préciser un formateur pour formater les 
     * n-uplets en chaines de caractère.
     *
     * @param nUplets
     * @param formateur
     */
    public void listerNUplets(@NotNull List<Entite> nUplets, Function<Entite, String> formateur) {
        lister(nUplets
              .stream()
              .map(nUplet -> formateur == null ? nUplet.toString() : formateur.apply(nUplet))
              .collect(Collectors.toList()));
    }

    /**
     * Afficher une liste de n-uplets,
     * sans selection par la suite.
     *
     * On utilise toString comme formateur des n-uplets en
     * chaines de caractère.
     *
     * @param nUplets
     * @return
     */
    public void listerNUplets(@NotNull List<Entite> nUplets) {
        listerNUplets(nUplets, null);
    }

    /**
     * Obtenir l'utilisateur connecté.
     *
     * @return
     */
    public Compte getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * Modifier l'utilisateur connecté.
     *
     * @param utilisateurConnecte
     */
    public void setUtilisateurConnecte(Compte utilisateurConnecte) {
        this.utilisateurConnecte = utilisateurConnecte;
    }

    /**
     * (Ré-)initialiser le scanner
     * avec le flux d'entrée du système.
     *
     * (Cette méthode sert aux tests.)
     */
    public void reinitialiserScanner() {
        scanner = new Scanner(System.in);
    }
}