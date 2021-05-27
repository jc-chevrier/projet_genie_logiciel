package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Unite;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;
import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Contrôleur pour la gestion des unités.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class UniteControleur extends Controleur {
    //Messages courants.
    private final static String MESSAGE_AUCUNE_TROUVEE = "Aucune unité trouvée dans le cataloque !";
    private final static String MESSAGE_SELECTIONNER = "Sélectionner une unité :";

    /**
     * Lister les unités.
     */
    public static void lister() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Listing des unités :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si des unités ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(unites, MESSAGE_AUCUNE_TROUVEE)) {
            //Listing.
            ui.listerNUplets(unites);
        }
    }

    /**
     * Méthode permettant d'éditer, et de sauvegarder une unité,
     * qu'elle soit déjà existante en base de données ou non.
     *
     * @param unite
     */
    private static void editerEtPersister(@NotNull Unite unite) {
        //Questions et entrées.
        //Saisie du libellé de l'unité.
        String libelle = ui.poserQuestion("Saisir un libellé :", UI.REGEX_CHAINE_DE_CARACTERES);

        //Sauvegarde.
        //Insertion / mise à jour de l'unité.
        unite.setLibelle(libelle);
        orm.persisterNUplet(unite);
    }

    /**
     * Ajouter une unité.
     */
    public static void ajouter() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Ajout d'une unité :");

        //Saisie et sauvegarde.
        Unite unite = new Unite();
        editerEtPersister(unite);

        //Message de résultat.
        ui.afficher("Unité ajoutée !\n" + unite);
    }

    /**
     * Modifier une unité.
     */
    public static void modifier() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Modification d'une unité :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si des unités ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(unites, MESSAGE_AUCUNE_TROUVEE)) {
            //Questions et saisies.
            //Choix de l'unité.
            Unite unite = (Unite) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, unites);

            //Saisie et sauvegarde.
            editerEtPersister(unite);

            //Message de résultat.
            ui.afficher("Unité modifiée !\n" + unite);
        }
    }

    /**
     * Supprimer une unité.
     */
    public static void supprimer() {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Suppression d'une unité :");

        //Récupération des unités existantes.
        List<Entite> unites = orm.chercherTousLesNUplets(Unite.class);

        //Si des unités ont été trouvées dans le catalogue.
        if(!ui.afficherSiListeNUpletsVide(unites, MESSAGE_AUCUNE_TROUVEE)) {
            //Questions et saisies.
            //Choix de l'unité.
            Unite unite = (Unite) ui.poserQuestionListeNUplets(MESSAGE_SELECTIONNER, unites);

            //Si l'unité n'est pas utilisée par des ingrédients.
            String messageErreur = "Cette unité est utilisée par des ingrédients, elle ne peut pas être supprimée !";
            if(!ui.afficherSiPredicatVrai(unite.estUtiliseeParIngredient(), messageErreur)) {
                //Sauvegarde : suppression de l'unité.
                orm.supprimerNUplet(unite);

                //Message de résultat.
                ui.afficher("Unité supprimée !");
            }
        }
    }
}