package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import org.jetbrains.annotations.NotNull;

/**
 * Classe pour les fonctionnalités internes, qui font un
 * retour vers l'accueil une fois exécutée.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class FonctionnaliteInterne extends Fonctionnalite {
    public FonctionnaliteInterne(@NotNull String libelle, @NotNull Runnable runnable) {
        super(libelle, runnable);
    }

    @Override
    public void executer() {
        //Exécution de la fonctionnalité.
        super.executer();

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }
}