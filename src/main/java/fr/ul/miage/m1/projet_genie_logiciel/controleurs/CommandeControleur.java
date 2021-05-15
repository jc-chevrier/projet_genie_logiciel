package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Commande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.LigneCommande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Place;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.util.List;

/**
 * Controleur pour les commandes.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class CommandeControleur extends Controleur{
    /**
     * Lister tous les plats à préparer.
     */
    public static void listerPlatAPreparer() {
        //UI et ORM
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Lister les plats à préparer des commandes :");

        //Récupération des lignes de commandes.
        List<Entite> ligneCommande = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'en attente'", LigneCommande.class);

        //Si pas de plats en attente.
        if (ligneCommande.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucun plat n'est à préparer dans le restaurant !");
        //Sinon
        } else {
            //Listing.
            ui.listerNUplets(ligneCommande);
        }

        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
