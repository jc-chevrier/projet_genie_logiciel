package fr.ul.miage.m1.projet_genie_logiciel.ui;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour l'interface console.
 *
 * Cette classe propose différentes méthodes pour interagir avec
 * les utilisateurs via l'interface console.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class UI {
    //Délimiteur sur la console entre des contenus.
    public final static String DELIMITEUR = "--------------------------------------------------------------------------------------------------------------------------------------------";

    //Expression régulière des chaines de caractères.
    public final static String REGEX_CHAINE_DE_CARACTERES = ".{1,50}";
    //Expression régulière des nombres décimaux positifs.
    public final static String REGEX_DECIMAL_POSITIF = "0*[1-9]{1}[0-9]{0,4}(\\.{1}0*){0,1}|" +
                                                       "0*[1-9]{1}[0-9]{0,4}\\.{1}[0-9]{0,2}[1-9]{1}0*|" +
                                                       "0+\\.{1}[0-9]{0,2}[1-9]{1}0*";
    //Expression régulière des grands nombres décimaux positifs.
    public final static String REGEX_GRAND_DECIMAL_POSITIF = "0*[1-9]{1}[0-9]{0,12}(\\.{1}0*){0,1}|" +
                                                             "0*[1-9]{1}[0-9]{0,12}\\.{1}[0-9]{0,2}[1-9]{1}0*|" +
                                                             "0+\\.{1}[0-9]{0,2}[1-9]{1}0*";
    //Expression régulière des nombres décimaux positifs ou négatifs.
    public final static String REGEX_GRAND_DECIMAL_POSITIF_OU_NEGATIF = "-{0,1}(" + REGEX_GRAND_DECIMAL_POSITIF + ")";

    //Singleton.
    private static UI singletonUI;

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
        if(singletonUI == null) {
            singletonUI = new UI();
        }
        return singletonUI;
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
     * Afficher un contenu en ajoutant avant le délimiteur,
     * et l'utilisateur connecté.
     *
     * @param contenu
     */
    public void afficherAvecDelimiteurEtUtilisateur(@NotNull String contenu) {
        afficher("\n" + UI.DELIMITEUR + "\n" + utilisateurConnecte.toSimpleString() + " " + contenu + "\n");
    }

    /**
     * Afficher le message d'erreur précisé si le prédicat
     * précisé est vrai.
     *
     * La méthode retourne la valeur du prédicat / de la
     * condition.
     *
     * @param predicat
     * @param messageErreur
     * @return
     */
    public boolean afficherSiPredicatVrai(boolean predicat, @NotNull String messageErreur) {
        if(predicat) {
            afficher(messageErreur);
        }
        return predicat;
    }

    /**
     * Afficher le message d'erreur précisé si le nombre
     * précisé est vide.
     *
     * La méthode retourne true si le nombre est nul.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#afficherSiPredicatVrai(boolean, String)
     *
     * @param nombre
     * @param messageErreur
     * @return
     */
    public boolean afficherSiNombreNul(int nombre, @NotNull String messageErreur) {
        return afficherSiPredicatVrai(nombre == 0, messageErreur);
    }

    /**
     * Afficher le message d'erreur précisé si la liste de
     * n-uplets précisée est vide.
     *
     * La méthode retourne true si la liste est vide.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#afficherSiPredicatVrai(boolean, String) 
     * 
     * @param nUplets
     * @param messageErreur
     * @return
     */
    public boolean afficherSiListeNUpletsVide(@NotNull List<Entite> nUplets, @NotNull String messageErreur) {
        return afficherSiPredicatVrai(nUplets.isEmpty(), messageErreur);
    }

    /**
     * Poser une question, en précisant :
     * - la question ;
     * - une expression régulière décrivant les réponses possibles
     *   autorisées ;
     *
     * Tant que la réponse donnée est incorrecte, la fonction boucle
     * récursivement.
     * 
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
     *
     * Cette méthode sert à factoriser la conversion de la réponse
     * de chaine de caractères vers entier.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestion(String, String) 
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
     *
     * Cette méthode sert à factoriser la conversion de la réponse
     * de chaine de caractères vers nombre décimal.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestion(String, String)
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @return
     */
    public double poserQuestionDecimal(@NotNull String question, @NotNull String reponsesPossiblesRegex) {
        return Double.parseDouble(poserQuestion(question, reponsesPossiblesRegex));
    }

    /**
     * Poser une question fermée, c'est-à-dire une question qui ne
     * peut avoir en réponse que oui ou non.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestion(String, String)
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
     * Poser une question en proposant une liste d'options, et obtenir
     * l'indice de l'option sélectionnée.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestion(String, String)
     *
     * @param question
     * @param options
     * @return
     */
    public int poserQuestionListeOptions(@NotNull String question, @NotNull List<String> options) {
        //Si la liste de n-uplets est vide.
        if(options.isEmpty()) {
            throw new IllegalArgumentException("Erreur ! On ne peut passer poser une question avec une liste d'options vide !");
        }

        //Construction de la question, et de l'expression régulière des réponses
        //possibles.
        String reponsesPossiblesRegex = "";
        int nbOptions = options.size();
        for(int index = 0; index < nbOptions; index++) {
            String option = options.get(index);
            question += "\n" + option + " (saisir " + (index + 1) + ")";
            reponsesPossiblesRegex += (index + 1) + "{1}" + ((index < (nbOptions - 1)) ? "|" : "");
        }

        //Récupération de l'indice de l'option sélectionnée.
        int index = poserQuestionEntier(question, reponsesPossiblesRegex) - 1;

        return index;
    }

    /**
     * Poser une question en proposant une liste d'options, à partir d'une
     * liste de n-uplets, et obtenir le n-uplet sélectionné.
     *
     * On peut préciser un formateur pour formater les
     * n-uplets en chaines de caractère.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestion(String, String)
     *
     * @param question
     * @param nUplets
     * @param formateur
     * @return
     */
    public Entite poserQuestionListeNUplets(@NotNull String question, @NotNull List<Entite> nUplets,
                                            Function<Entite, String> formateur) {
        //Si la liste de n-uplets est vide.
        if(nUplets.isEmpty()) {
            throw new IllegalArgumentException("Erreur ! On ne peut passer poser une question avec une liste d'options vide !");
        }

        //Construction de la question, et de l'expression régulière des réponses
        //possibles.
        String reponsesPossiblesRegex = "";
        int nbNUplets = nUplets.size();
        for(int index = 0; index < nbNUplets; index++) {
            Entite nUplet = nUplets.get(index);
            question += "\n" + (formateur == null ? nUplet : formateur.apply(nUplet)) + " (saisir " + nUplet.getId() + ")";
            reponsesPossiblesRegex += nUplet.getId() + "{1}" + ((index < (nbNUplets - 1)) ? "|" : "");
        }

        //Saisie et attente de la réponsne de l'utilisateur.
        int id = poserQuestionEntier(question, reponsesPossiblesRegex);

        //Récupération du n-uplet sélectionné à partir de l'id.
        Entite nUplet = nUplets.stream()
                               .filter(nUplet_ -> nUplet_.getId().equals(id))
                               .findFirst()
                               .get();

        return nUplet;
    }

    /**
     * Poser une question en proposant une liste d'options, à partir d'une
     * liste de n-uplets, et obtenir le n-uplet sélectionné.
     *
     * On utilise toString comme formateur des n-uplets en
     * chaines de caractère.
     *
     * @see fr.ul.miage.m1.projet_genie_logiciel.ui.UI#poserQuestionListeNUplets(String, List, Function)
     *  
     * @param question
     * @param nUplets
     * @return
     */
    public Entite poserQuestionListeNUplets(@NotNull String question, @NotNull List<Entite> nUplets) {
        return poserQuestionListeNUplets(question, nUplets, null);
    }

    /**
     * Afficher une liste de strings, sans selection par la suite /
     * sans poser de question par la suite.
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
     * Afficher une liste de n-uplets, sans selection par la suite /
     * sans poser de question par la suite.
     *
     * On peut préciser un formateur pour formater les 
     * n-uplets en chaines de caractère.
     *
     * @param nUplets
     * @param formateur
     */
    public void listerNUplets(@NotNull List<Entite> nUplets, Function<Entite, String> formateur) {
        lister(nUplets.stream()
                      .map(nUplet -> formateur == null ? nUplet.toString() : formateur.apply(nUplet))
                      .collect(Collectors.toList()));
    }

    /**
     * Afficher une liste de n-uplets, sans selection par la suite /
     * sans poser de question par la suite.
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
     * (Ré-)initialiser le scanner avec le flux d'entrée du système.
     *
     * (Cette méthode sert aux tests.)
     */
    public void reinitialiserScanner() {
        scanner = new Scanner(System.in);
    }
}