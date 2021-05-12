package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Commande;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
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
     * Valider le paiement d'une commande.
     */
    public static void validerPaiement() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Valider le paiement d'une commande :");

        //Récupération des commandes servies.
        List<Entite> commandes = orm.chercherNUpletsAvecPredicat("WHERE ETAT = 'servi'", Commande.class);
        //Si pas de commande servie.
        if(commandes.isEmpty()) {
            //Message d'erreur.
            ui.afficher("Aucune commande servie trouvée !");
            //Sinon.
        } else {
            //Question et saisies.
            int idCommande = ui.poserQuestionListeNUplets("Sélectionner une commande :", commandes);
            Commande commande = (Commande) filtrerListeNUpletsAvecId(commandes, idCommande);

            //Sauvegarde : modification du plat.
            commande.setEtat("payé");
            orm.persisterNUplet(commande);

            //Message de résultat.
            ui.afficher("Commande payée !");
            ui.afficher(commande.toString());
        }

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }

}