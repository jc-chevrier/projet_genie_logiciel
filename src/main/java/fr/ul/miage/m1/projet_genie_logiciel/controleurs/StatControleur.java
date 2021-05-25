package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.StatChiffreAffaire;
import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import fr.ul.miage.m1.projet_genie_logiciel.ui.UI;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Controleur pour les statistiques.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class StatControleur extends Controleur {
    /**
     * Constulter les statistiques sur le chiffre d'affaire.
     */
    public static void consulterChiffreAffaire()  {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Consultation des statistiques sur le chiffre d'affaire :");

        //Obtenir les statistiques des 7 derniers jours.
        String requete = "SELECT STA.DATE_JOUR, SUM(STA.CHIFFRE_AFFAIRE) AS CA,"+
                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DINER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DINER END ) AS CADI,"+
                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DEJEUNER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DEJEUNER END) AS CADE"+
                " FROM STAT_CHIFFRE_AFFAIRE AS STA "+
                "WHERE STA.DATE_JOUR >= (NOW()::date - 6)"+
                " GROUP BY STA.DATE_JOUR";

        List<Map<String, Object>> chiffreAffaire7DernierJours = orm.chercherNUplets(requete);

        SimpleDateFormat dateFormateur = new SimpleDateFormat("dd/MM/yyyy");
        Function<Map<String, Object>, String> formateur = nUplet-> {
            String statFormatee = dateFormateur.format(nUplet.get("DATE_JOUR")) +
                    " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                    " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                    " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                    " ]" ;
            return statFormatee;
        };
        ui.afficher("Statistiques sur le chiffre d'affaire pour les 7 derniers jours :");
        chiffreAffaire7DernierJours.forEach(nUplet -> ui.afficher(formateur.apply(nUplet)));

        //Statistiques de la semaine.
        ui.afficher("\nStatistiques sur le chiffre d'affaire pour les 4 dernières semaines :");
        String requeteSemaine = "SELECT " +
                            "STA.DATE_DEBUT_SEMAINE, " +
                            "STA.DATE_FIN_SEMAINE, " +
                            "SUM(STA.CHIFFRE_AFFAIRE ) AS CA, "+
                            "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DINER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DINER END) AS CADI, "+
                            "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DEJEUNER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DEJEUNER END) AS CADE "+
                            "FROM (" +
                                "SELECT " +
                                "STA.DATE_JOUR - CAST(EXTRACT(ISODOW FROM STA.DATE_JOUR - 1) AS INTEGER) " +
                                "AS DATE_DEBUT_SEMAINE, " +
                                "STA.DATE_JOUR + CAST((-EXTRACT(ISODOW FROM STA.DATE_JOUR - 1) + 6) AS INTEGER) " +
                                "AS DATE_FIN_SEMAINE, " +
                                "STA.CHIFFRE_AFFAIRE, " +
                                "STA.CHIFFRE_AFFAIRE_DINER, " +
                                "STA.CHIFFRE_AFFAIRE_DEJEUNER " +
                                "FROM STAT_CHIFFRE_AFFAIRE AS STA " +
                            ") STA " +
                            "GROUP BY STA.DATE_DEBUT_SEMAINE, STA.DATE_FIN_SEMAINE " +
                            "ORDER BY STA.DATE_DEBUT_SEMAINE DESC " +
                            "LIMIT 4 " +
                            "OFFSET 0; ";

        List<Map<String, Object>> chiffreAffaireSemaine = orm.chercherNUplets(requeteSemaine);

        Function<Map<String, Object>, String> formateurSemaine = nUplet-> {
            String statFormatee = dateFormateur.format(nUplet.get("DATE_DEBUT_SEMAINE")) + " - " +
                    dateFormateur.format(nUplet.get("DATE_FIN_SEMAINE")) +
                    " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                    " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                    " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                    " ]" ;
            return statFormatee;
        };
        chiffreAffaireSemaine.forEach(nUplet -> ui.afficher(formateurSemaine.apply(nUplet)));
        //Statistiques pour les deux mois
        ui.afficher("\nStatistiques sur le chiffre d'affaire pour les deux derniers mois :");
        String requeteMois = "SELECT " +
                "STA.MOIS_2, " +
                "STA.MOIS, " +
                "SUM(STA.CHIFFRE_AFFAIRE ) AS CA, "+
                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DINER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DINER END) AS CADI, "+
                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DEJEUNER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DEJEUNER END) AS CADE "+
                "FROM (" +
                "SELECT " +
                "TO_CHAR(STA.DATE_JOUR, 'TMMONTH') AS MOIS, " +
                "CAST(EXTRACT(MONTH FROM STA.DATE_JOUR) AS INTEGER) AS MOIS_2, " +
                "STA.CHIFFRE_AFFAIRE, " +
                "STA.CHIFFRE_AFFAIRE_DINER, " +
                "STA.CHIFFRE_AFFAIRE_DEJEUNER " +
                "FROM STAT_CHIFFRE_AFFAIRE AS STA" +
                ") STA " +
                "GROUP BY STA.MOIS_2, STA.MOIS " +
                "ORDER BY STA.MOIS_2 DESC " +
                "LIMIT 2 " +
                "OFFSET 0;";
        List<Map<String, Object>> statsCAMois = orm.chercherNUplets(requeteMois);
        Function<Map<String, Object>, String> formateurMois = nUplet -> {
            String statFormatee =
                    nUplet.get("MOIS") +
                            " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                            " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                            " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                            " ]" ;
            return statFormatee;
        };
        statsCAMois.forEach(nUplet -> ui.afficher(formateurMois.apply(nUplet)));
        //Statistiques de l'année.
        ui.afficher("\nStatistiques sur le chiffre d'affaire pour l'année  :");
        String requeteAnnee = "SELECT " +
                            "STA.ANNEE, " +
                            "SUM(STA.CHIFFRE_AFFAIRE ) AS CA, "+
                            "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DINER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DINER END) AS CADI, "+
                            "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DEJEUNER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DEJEUNER END) AS CADE "+
                            "FROM (" +
                                "SELECT " +
                                "CAST(EXTRACT(YEAR FROM STA.DATE_JOUR) AS INTEGER) AS ANNEE, " +
                                "STA.CHIFFRE_AFFAIRE, " +
                                "STA.CHIFFRE_AFFAIRE_DINER, " +
                                "STA.CHIFFRE_AFFAIRE_DEJEUNER " +
                                "FROM STAT_CHIFFRE_AFFAIRE AS STA" +
                            ") STA " +
                            "GROUP BY STA.ANNEE " +
                            "ORDER BY STA.ANNEE DESC " +
                            "LIMIT 1 " +
                            "OFFSET 0;";

        List<Map<String, Object>> statCAAnnee = orm.chercherNUplets(requeteAnnee);
        Function<Map<String, Object>, String> formateurAnnee = nUplet -> {
            String statFormatee =
                    nUplet.get("ANNEE") +
                            " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                            " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                            " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                            " ]" ;
            return statFormatee;
        };
        statCAAnnee.forEach(nUplet -> ui.afficher(formateurAnnee.apply(nUplet)));
        //Retour vers l'accueil.
        AccueilControleur.consulter();
    }
}
