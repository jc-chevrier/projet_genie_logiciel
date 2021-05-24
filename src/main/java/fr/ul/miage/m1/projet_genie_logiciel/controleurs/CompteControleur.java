package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompteControleur extends Controleur{
    /**
     * Méthode permettant d'éditer, et de sauvegarder un salarié,
     * qu'il soit déjà ajouté ou non.
     *
     * @param compte
     */
    private static void editerEtPersister(@NotNull Compte compte) {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Récupération des roles.
        List<Entite> roles = orm.chercherTousLesNUplets(Role.class);

        //Compte.
        //Questions et saisies.
        //Choix du role.
        int idRole = ui.poserQuestionListeNUplets("Sélectionner un role :", roles);
        //Caractéristiques du compte.
        String nom = ui.poserQuestion("Saisir un nom :", UI.REGEX_CHAINE_DE_CARACTERES);
        String prenom = ui.poserQuestion("Saisir un prénom : ", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde : insertion du role.
        //Compte.
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
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'un salarié :");

        //Saisie et sauvegarde.
        Compte compte = new Compte();
        editerEtPersister(compte);
        //Message de résultat.
        ui.afficher("Salarié ajouté !");
        ui.afficher(compte.toString());

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Modifier un salarié.
     */
    public static void modifier() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'un salarié :");

        //Récupération des salariés existants.
        List<Entite> salaries = orm.chercherTousLesNUplets(Compte.class);

        //Si pas de salarié trouvés.
        if (salaries.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun salarié trouvé !");
        //Sinon.
        } else {
            //Saisie du salarié à modofier.
            int idSalarie = ui.poserQuestionListeNUplets("Sélectionner un salarié :", salaries);
            Compte compte = (Compte) filtrerListeNUpletsAvecId(salaries, idSalarie);

            //Saisie et sauvegarde.
            editerEtPersister(compte);

            //Message de résultat.
            ui.afficher("Informations salarié modifié !");
            ui.afficher(compte.toString());
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

    /**
     * Suuprimer un salarié.
     */
    public static void supprimer() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'un salarié :");

        //Récupération des salariés exsitants.
        List<Entite> salaries = orm.chercherTousLesNUplets(Compte.class);

        //Si pas de plats dans le cataloque.
        if (salaries.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun salarié trouvé !");
        //Sinon.
        } else {
            //Question et saisies.
            int idSalarie = ui.poserQuestionListeNUplets("Sélectionner un salarié :", salaries);
            Compte compte = (Compte) filtrerListeNUpletsAvecId(salaries, idSalarie);

            //Sauvegarde : suppression du salarié.
            List<Entite> salarie = orm.chercherNUpletsAvecPredicat("WHERE ID = " + idSalarie,
                    PlatIngredients.class);
            salarie.forEach(orm::supprimerNUplet);
            orm.supprimerNUplet(compte);

            //Message de résultat.
            ui.afficher("Salarié supprimé !");
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }

}

