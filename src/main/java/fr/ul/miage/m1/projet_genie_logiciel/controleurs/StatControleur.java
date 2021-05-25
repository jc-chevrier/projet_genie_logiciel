package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import fr.ul.miage.m1.projet_genie_logiciel.entites.StatGeneral;
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
     * Consulter les statistiques générales.
     */
    public static void consulterGenerales() {
        //UI et ORM.
        UI ui = getUI();
        ORM orm = getORM();

        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Consultation des statistiques générales :");

       SimpleDateFormat dateFormateur = new SimpleDateFormat("dd/MM/yyyy");

        //Statistiques des 7 derniers jours.
        List<Entite> statsGenerales7DerniersJours = orm.chercherNUpletsAvecPredicat("WHERE DATE_JOUR >= (NOW()::date - 6)",
                                                                                    StatGeneral.class);
        ui.afficher("Statistiques générales des 7 derniers jours :");
        Function<Entite, String> formateurJour = entite -> {
            StatGeneral statGeneral = (StatGeneral) entite;
            String statFormatee = dateFormateur.format(statGeneral.getDateJour()) +
                    " [ Nombre de clients : "  + statGeneral.getNbCommandesPayees() + "," +
                    " Nombre de commandes (payées) : "  + statGeneral.getNbCommandesPayees() + "," +
                    " Temps mpyen de prépration des commandes : " +
                    Math.round((double) statGeneral.getTempsPreparation() /
                            (double) statGeneral.getNbCommandesPreparees()) +
                    " ]" ;
            return statFormatee;
        };
        ui.listerNUplets(statsGenerales7DerniersJours, formateurJour);

        //Statistiques de la semaine.
        ui.afficher("\nStatistiques générales des 4 dernières semaines :");
        String requeteSemaine = "SELECT " +
                                    "SG.DATE_DEBUT_SEMAINE, " +
                                    "SG.DATE_FIN_SEMAINE, " +
                                    "SUM(SG.NB_CLIENTS) AS NB_CLIENTS, " +
                                    "SUM(SG.NB_COMMANDES_PAYEES) AS NB_COMMANDES_PAYEES, " +
                                    "SUM(SG.NB_COMMANDES_PREPAREES) AS NB_COMMANDES_PREPAREES, " +
                                    "SUM(SG.TEMPS_PREPARATION) AS TEMPS_PREPARATION " +
                                "FROM (" +
                                    "SELECT " +
                                         "SG.DATE_JOUR - CAST(EXTRACT(ISODOW FROM SG.DATE_JOUR - 1) AS INTEGER) " +
                                         "AS DATE_DEBUT_SEMAINE, " +
                                         "SG.DATE_JOUR + CAST((-EXTRACT(ISODOW FROM SG.DATE_JOUR - 1) + 6) AS INTEGER) " +
                                         "AS DATE_FIN_SEMAINE, " +
                                         "SG.NB_CLIENTS, " +
                                         "SG.NB_COMMANDES_PREPAREES, " +
                                         "SG.NB_COMMANDES_PAYEES, " +
                                         "SG.TEMPS_PREPARATION " +
                                    "FROM STAT_GENERAL AS SG" +
                                ") SG " +
                                "GROUP BY SG.DATE_DEBUT_SEMAINE, SG.DATE_FIN_SEMAINE " +
                                "ORDER BY SG.DATE_DEBUT_SEMAINE DESC " +
                                "LIMIT 4 " +
                                "OFFSET 0;";
        List<Map<String, Object>> statsGeneralesSemaine = orm.chercherNUplets(requeteSemaine);
        Function<Map<String, Object>, String> formateurSemaine = nUplet -> {
            String statFormatee =
                    dateFormateur.format(nUplet.get("DATE_DEBUT_SEMAINE")) + " - " +
                            dateFormateur.format(nUplet.get("DATE_FIN_SEMAINE")) +
                            " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                            " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                            " Temps mpyen de prépration des commandes : " +
                            Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                    ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                            " ]" ;
            return statFormatee;
        };
        statsGeneralesSemaine.forEach(nUplet -> ui.afficher(formateurSemaine.apply(nUplet)));

        //Statistiques des mois.
        ui.afficher("\nStatistiques générales sur les 2 derniers mois :");
        String requeteMois = "SELECT " +
                                "SG.MOIS_2, " +
                                "SG.MOIS, " +
                                "SUM(SG.NB_CLIENTS) AS NB_CLIENTS, " +
                                "SUM(SG.NB_COMMANDES_PAYEES) AS NB_COMMANDES_PAYEES, " +
                                "SUM(SG.NB_COMMANDES_PREPAREES) AS NB_COMMANDES_PREPAREES, " +
                                "SUM(SG.TEMPS_PREPARATION) AS TEMPS_PREPARATION " +
                            "FROM (" +
                                "SELECT " +
                                    "TO_CHAR(SG.DATE_JOUR, 'TMMONTH') AS MOIS, " +
                                    "CAST(EXTRACT(MONTH FROM SG.DATE_JOUR) AS INTEGER) AS MOIS_2, " +
                                    "SG.NB_CLIENTS, " +
                                    "SG.NB_COMMANDES_PREPAREES, " +
                                    "SG.NB_COMMANDES_PAYEES, " +
                                    "SG.TEMPS_PREPARATION " +
                                "FROM STAT_GENERAL AS SG" +
                            ") SG " +
                            "GROUP BY SG.MOIS_2, SG.MOIS " +
                            "ORDER BY SG.MOIS_2 DESC " +
                            "LIMIT 2 " +
                            "OFFSET 0;";
        List<Map<String, Object>> statsGeneralesMois = orm.chercherNUplets(requeteMois);
        Function<Map<String, Object>, String> formateurMois = nUplet -> {
            String statFormatee =
                            nUplet.get("MOIS") +
                            " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                            " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                            " Temps mpyen de prépration des commandes : " +
                            Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                    ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                            " ]" ;
            return statFormatee;
        };
        statsGeneralesMois.forEach(nUplet -> ui.afficher(formateurMois.apply(nUplet)));

        //Statistiques des années.
        ui.afficher("\nStatistiques générales sur l'année  :");
        String requeteAnnee = "SELECT " +
                                    "SG.ANNEE, " +
                                    "SUM(SG.NB_CLIENTS) AS NB_CLIENTS, " +
                                    "SUM(SG.NB_COMMANDES_PAYEES) AS NB_COMMANDES_PAYEES, " +
                                    "SUM(SG.NB_COMMANDES_PREPAREES) AS NB_COMMANDES_PREPAREES, " +
                                    "SUM(SG.TEMPS_PREPARATION) AS TEMPS_PREPARATION " +
                                "FROM (" +
                                    "SELECT " +
                                        "CAST(EXTRACT(YEAR FROM SG.DATE_JOUR) AS INTEGER) AS ANNEE, " +
                                        "SG.NB_CLIENTS, " +
                                        "SG.NB_COMMANDES_PREPAREES, " +
                                        "SG.NB_COMMANDES_PAYEES, " +
                                        "SG.TEMPS_PREPARATION " +
                                    "FROM STAT_GENERAL AS SG" +
                                ") SG " +
                                "GROUP BY SG.ANNEE " +
                                "ORDER BY SG.ANNEE DESC " +
                                "LIMIT 1 " +
                                "OFFSET 0;";
        List<Map<String, Object>> statsGeneralesAnnee = orm.chercherNUplets(requeteAnnee);
        Function<Map<String, Object>, String> formateurAnnee = nUplet -> {
            String statFormatee =
                            nUplet.get("ANNEE") +
                            " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                            " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                            " Temps mpyen de prépration des commandes : " +
                            Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                    ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                            " ]" ;
            return statFormatee;
        };
        statsGeneralesAnnee.forEach(nUplet -> ui.afficher(formateurAnnee.apply(nUplet)));

        //Retour à l'accueil.
        AccueilControleur.consulter();
    }
}
