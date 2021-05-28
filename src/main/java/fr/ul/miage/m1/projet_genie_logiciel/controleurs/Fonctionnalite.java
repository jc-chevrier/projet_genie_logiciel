package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * Classe pour la définition des fonctionnalités
 * de l'application.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Fonctionnalite {
    //Libellé de la fonctionnalité.
    private String libelle;
    //Fonctionnalité.
    private Runnable runnable;

    public Fonctionnalite(@NotNull String libelle, @NotNull Runnable runnable) {
        this.libelle = libelle;
        this.runnable = runnable;
    }

    /**
     * Exécuter une fonctionnalité.
     */
    public void executer() {
        runnable.run();
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}