package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Contrôleur pour la gestion des salariés.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CompteControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUN_TROUVE =  "Aucun salarié trouvé !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner un salarié :";

    /**
     * Lister tous les salarié.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing de tous les salariés :");

        //Récupération des salariés existants.
        List<Entite> comptes = orm.chercherTousLesNUplets(Compte.class);

        //Si des salariés ont été trouvés.
        if (!ui.afficherSiListeNUpletsVide(comptes, MESSAGE_AUCUN_TROUVE)) {
            //Listing.
            ui.listerNUplets(comptes);
        }
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder un salarié,
     * qu'il existe déjà en base de données ou non.
     *
     * @param compte
     */
    private static void editerEtPersister(@NotNull Compte compte) {
        //Récupération des rôles.
        List<Entite> roles = orm.chercherTousLesNUplets(Role.class);

        //Compte.
        //Questions et saisies.
        //Choix du role.
        int idRole = ui.poserQuestionListeNUplets("Sélectionner un role :", roles).getId();
        //Caractéristiques du compte.
        String nom = ui.poserQuestion("Saisir un nom :", UI.REGEX_CHAINE_DE_CARACTERES);
        String prenom = ui.poserQuestion("Saisir un prénom : ", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde : insertion du compte.
        compte.setNom(nom);
        compte.setPrenom(prenom);
        compte.setIdRole(idRole);
        compte.setActif(1);
        orm.persisterNUplet(compte);
    }

    /**
     * Ajouter un salarié.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un salarié :");

        //Saisies et sauvegarde.
        Compte compte = new Compte();
        editerEtPersister(compte);

        //Message de résultat.
        ui.afficher("Salarié ajouté !\n" + compte);
    }

    /**
     * Modifier un salarié.
     */
    public static void modifier() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification des informations d'un salarié :");

        //Récupération des salariés existants.
        List<Entite> comptes = orm.chercherTousLesNUplets(Compte.class);

        //Si des salariés ont été trouvés.
        if (!ui.afficherSiListeNUpletsVide(comptes, MESSAGE_AUCUN_TROUVE)) {
            //Questions et saisies.
            Compte compte = (Compte) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, comptes);

            //Saisies et sauvegarde.
            editerEtPersister(compte);

            //Message de résultat.
            ui.afficher("Informations du salarié modifiées !\n" + compte);
        }
    }

    /**
     * Suuprimer un salarié.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un salarié :");

        //Récupération des salariés actifs existants.
        List<Entite> comptesActifs = orm.chercherNUpletsAvecPredicat("WHERE ACTIF = 1", Compte.class);

        //Si des salariés actifs ont été trouvés.
        String messageErreur = "Aucun salarié actif trouvé !";
        if (!ui.afficherSiListeNUpletsVide(comptesActifs, messageErreur)) {
            //Question et saisies.
            Compte compte = (Compte) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, comptesActifs);

            //Sauvegarde : modification du salarié.
            //Le salarié passe à inactif.
            compte.setActif(0);
            orm.persisterNUplet(compte);

            //Message de résultat.
            ui.afficher("Salarié supprimé !");
        }
    }
}