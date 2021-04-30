package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

/**
 * Contrôleur pour l'authentification.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class AuthControleur extends Controleur {
    /**
     * Se connecter.
     *
     * La fonction boucle récursivement tant que
     * l'authentification échoue.
     */
    public static void seConnecter() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Questions et saisies.
        ui.afficher("\n" + UI.DELIMITEUR + "\nMe connecter :");
        String nom = ui.poserQuestion("Saisir votre nom :", UI.REGEX_CHAINE_DE_CARACTERES);
        String prenom = ui.poserQuestion("Saisir votre prenom :", UI.REGEX_CHAINE_DE_CARACTERES);

        //Vérification.
        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE LOWER(nom) = LOWER('" + nom + "') " +
                                                                "AND LOWER(prenom) = LOWER('" + prenom + "')", Compte.class);
        //Cas récursif.
        if(compte == null) {
            ui.afficher("Erreur durant la tentative de connexion !");
            seConnecter();
        //Cas trivial.
        } else {
            ui.afficher("Connexion réussie !");
            //On retient l'utilisateur courant connecté.
            ui.setUtilisateurConnecte(compte);
        }
    }

    /**
     * Se déconnecter.
     */
    public static void seDeconnecter() {
        //UI.
        UI ui = getUI();

        //On retient la déconnexion.
        ui.setUtilisateurConnecte(null);
        ui.afficher("Déconnexion réussie !");

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}