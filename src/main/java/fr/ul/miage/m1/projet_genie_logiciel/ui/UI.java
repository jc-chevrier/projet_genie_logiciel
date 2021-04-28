package fr.ul.miage.m1.projet_genie_logiciel.ui;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
     * Poser une question dont la réponse est un nombre décimal
     * Cette fonction sert à factoriser la conversion de chaine de caractères vers nombre décimal.
     *
     * @param question
     * @param reponsesPossiblesRegex
     * @param afficherDelimiteur
     * @return
     */
    public double poserQuestionDecimal(@NotNull String question, @NotNull String reponsesPossiblesRegex,
                                      boolean afficherDelimiteur) {
        return Double.parseDouble(poserQuestion(question, reponsesPossiblesRegex, afficherDelimiteur));
    }

    /**
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
     * Poser une question fermée (oui, non).
     *
     * @param question
     * @return
     */
    public boolean poserQuestionFermee(@NotNull String question, boolean afficherDelimiteur) {
        question += " (oui/non)";
        String reponsesPossiblesRegex = "oui|non";
        String reponse = poserQuestion(question, reponsesPossiblesRegex, afficherDelimiteur) ;
        return reponse.equals("oui");
    }

    /**
=======
>>>>>>> fd99e4c965e1ed812db89102c5c8ad3d79efe4f3
=======
>>>>>>> 2496e6ed7b551e1964eeed3d8e697b1c40905eb5
=======
>>>>>>> 3b29530ceedfde8db6fcf2c07969249d093c4cb0
     * Poser une question en proposant une liste d'options,
     * et obtenir l'indice de l'option sélectionnée.
     *
     * @param options
     * @return
     */
    public int poserQuestionListeOptions(@NotNull List<String> options) {
        String question = "Sélectionner :";
        String reponsesPossiblesRegex = "";
        int nbOptions = options.size();
        for(int index = 0; index < nbOptions; index++) {
            String option = options.get(index);
            question += "\n" + option + " (saisir " + (index + 1) + ")";
            reponsesPossiblesRegex += (index + 1) + "{1}" + ((index < (nbOptions - 1)) ? "|" : "");
        }
        int index = poserQuestionEntier(question, reponsesPossiblesRegex, true) - 1;
        return index;
    }

    /**
     * Poser une question en proposant une liste d'options
     * pour une liste de n-uplets, et obtenir l'id de
     * du n-uplet sélectionné.
     *
     * @return
     */
    public int poserQuestionListeNUplets(@NotNull List<Entite> nUplets) {
        String question = "Sélectionner :";
        String reponsesPossiblesRegex = "";
        int nbNUplets = nUplets.size();
        for(int index = 0; index < nbNUplets; index++) {
            Entite nUplet = nUplets.get(index);
            question += "\n" + nUplet + " (saisir " + (nUplet.getId()) + ")";
            reponsesPossiblesRegex += (nUplet.getId()) + "{1}" + ((index < (nbNUplets - 1)) ? "|" : "");
        }
        int id = poserQuestionEntier(question, reponsesPossiblesRegex, true);
        return id;
    }

    /**
     * Afficher une liste de strings,
     * sans selection par al suite.
     *
     * @param elements
     * @return
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
     * sans selection par al suite.
     *
     * @param nUplets
     * @return
     */
    public void listerNUplets(@NotNull List<Entite> nUplets) {
        lister(nUplets.stream().map(nUplet -> nUplet.toString()).collect(Collectors.toList()));
    }
    git
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
