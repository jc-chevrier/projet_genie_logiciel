package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Compte;
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
     * La méthode boucle récursivement tant que
     * l'authentification échoue.
     */
    public static void seConnecter() {
        //Questions et saisies.
        ui.afficher("\n" + UI.DELIMITEUR + "\nMe connecter :\n");
        String nom = ui.poserQuestion("Saisir votre nom :", UI.REGEX_CHAINE_DE_CARACTERES);
        String prenom = ui.poserQuestion("Saisir votre prenom :", UI.REGEX_CHAINE_DE_CARACTERES);

        //Vérification.
        //(Seuls les utilisateurs actifs dans le restaurant peuvent se connecter)
        Compte compte = (Compte) orm.chercherNUpletAvecPredicat("WHERE LOWER(NOM) = LOWER('" + nom + "') " +
                                                                "AND LOWER(PRENOM) = LOWER('" + prenom + "') " +
                                                                "AND ACTIF = 1",
                                                                Compte.class);
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
        //On retient la déconnexion.
        ui.setUtilisateurConnecte(null);
        ui.afficher("Déconnexion réussie !");
    }
}