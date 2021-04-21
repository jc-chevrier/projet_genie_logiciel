package fr.ul.miage.m1.projet_genie_logiciel.ui;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Scanner;

public class UI {
    //Délimiteur sur la console entre des contenus.
    public final static String DELIMITEUR = "-------------------------------------------------------------------------------------------------------";

    //Expression régulière des chaines de caractères.
    public final static String REGEX_CHAINE_DE_CARACTERES = ".{1,50}";
    //Expression régulière des entiers positifs.
    public final static String REGEX_ENTIER_POSITIF = "[1-9]{1}[0-9]{0,7}";
    //Expression régulière des nombres décimaux positifs.
    public final static String REGEX_DECIMAL_POSITIF = "[1-9]{1,5}|[1-9]{1,5}\\.{1}[0-9]{1,3}|[0-9]{1,5}\\.{1}[1-9]{1,3}";
    //Expression régulière des grands nombres décimaux positifs.
    public final static String REGEX_GRAND_DECIMAL_POSITIF = "[1-9]{1,13}|[1-9]{1,13}\\.{1}[0-9]{1,3}|[0-9]{1,13}\\.{1}[1-9]{1,3}";

    //Description des entiers positifs.
    public final static String DESCRIPTION_ENTIER_POSITIF = "entier positif";
    //Description des décimaux positifs.
    public final static String DESCRIPTION_DECIMAL_POSITIF  = "nombre décimal positif";

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
     * @param contenu
     */
    public void afficher(@NotNull String contenu) {
        System.out.println(contenu);
    }

    /**
     * Poser une question, en précisant :
     * - la question ;
     * - une expression régulière décrivant les réponses possibles autorisées ;
     * - si la question doit être précédée ou non du délimiteur.
     * Tant que la réponse donnée est incorrecte, la fonction boucle récursivement.
     * La réponse correcte donnée est retournée.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @param afficherDelimiteur
     * @return
     */
    public String poserQuestion(@NotNull String question, @NotNull String reponsesPossiblesRegex,
                                 boolean afficherDelimiteur) {
        String questionAffichee = null;
        if(afficherDelimiteur) {
            questionAffichee = "\n"+ DELIMITEUR + "\n" + question;
        } else {
            questionAffichee = question;
        }
        afficher(questionAffichee);
        String reponse = scanner.nextLine();
        //Cas trivial.
        if(reponse.matches("^" + reponsesPossiblesRegex + "$")) {
            return reponse;
        //Cas récursif.
        } else {
            afficher("La réponse donnée est incorrecte.");
            return poserQuestion(question, reponsesPossiblesRegex, afficherDelimiteur);
        }
    }

    /**
     * Poser une question dont la réponse est un entier
     * Cette fonction sert à factoriser la conversion de chaine de caractères vers entier.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @param afficherDelimiteur
     * @return
     */
    public int poserQuestionEntier(@NotNull String question, @NotNull String reponsesPossiblesRegex,
                                    boolean afficherDelimiteur) {
        return Integer.parseInt(poserQuestion(question, reponsesPossiblesRegex, afficherDelimiteur));
    }

    /**
     * Poser une question en proposant une liste d'options,
     * et obtenir l'indice de l'option sélectionnée.
     *
     * @param options
     * @return
     */
    public int poserQuestionListeOptions(List<String> options) {
        String question = "Sélectionner une option :";
        String reponsesPossiblesRegex = "";
        int nbOptions = options.size();
        for(int index = 0; index < nbOptions; index++) {
            String groupeFonctionnalite = options.get(index);
            question += "\n" + groupeFonctionnalite + " (saisir " + (index + 1) + ")";
            reponsesPossiblesRegex += (index + 1) + "{1}" + ((index < (nbOptions - 1)) ? "|" : "");
        }
        int index = poserQuestionEntier(question, reponsesPossiblesRegex, true) - 1;
        return index;
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
}
