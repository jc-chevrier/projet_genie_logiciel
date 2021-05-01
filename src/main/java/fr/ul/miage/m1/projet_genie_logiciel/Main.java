package fr.ul.miage.m1.projet_genie_logiciel;

import fr.ul.miage.m1.projet_genie_logiciel.controleurs.AccueilControleur;
import fr.ul.miage.m1.projet_genie_logiciel.entites.Plat;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;

/**
 * Classe principale.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Main {
    public static void main(String[] args) {
        //AccueilControleur.consulter();
        ORM.getInstance().chercherTousLesNUplets(Plat.class).forEach((c)->System.out.println(((Plat)c).getIdCategorie()));
    }
}