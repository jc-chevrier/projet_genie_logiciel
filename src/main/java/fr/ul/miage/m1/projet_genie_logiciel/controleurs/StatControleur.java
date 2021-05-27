package fr.ul.miage.m1.projet_genie_logiciel.controleurs;

import fr.ul.miage.m1.projet_genie_logiciel.entites.*;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Contrôleur pour la consultation des statistiques.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class StatControleur extends Controleur {
    private final static String SQL_DATE_FORMAT = "dd/mm/yyyy";
    private final static SimpleDateFormat JAVA_DATE_FORMATEUR = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Obtenir la date courante.
     *
     * @return
     */
    private final static Date getDateCourante() {
        return new Date();
    }

    /**
     * Obtenir la date courante en chaine de caractères.
     *
     * @return
     */
    private final static String getDateCouranteCommeChaine() {
        return JAVA_DATE_FORMATEUR.format(getDateCourante());
    }

    /**
     * Incrémenter le nombre de clients du jour.
     */
    public static void incrementerNombreClients() {
        //Récupération.
        String aujourdhuiCommeChaine = getDateCouranteCommeChaine();
        StatGeneral statGeneral = (StatGeneral) orm.chercherNUpletAvecPredicat("WHERE TO_CHAR(DATE_JOUR, '" + SQL_DATE_FORMAT + "') = '" +
                                                                               aujourdhuiCommeChaine + "'",
                                                                               StatGeneral.class);

        //Sauvegarde.
        if(statGeneral == null) {
            statGeneral = new StatGeneral();
            statGeneral.setDateJour(new Date());
            statGeneral.setNbClients(1);
        } else {
            Integer nbClientsActuel = statGeneral.getNbClients();
            if(nbClientsActuel == null) {
                statGeneral.setNbClients(1);
            } else {
                statGeneral.setNbClients(nbClientsActuel + 1);
            }
        }
        orm.persisterNUplet(statGeneral);
    }

    /**
     * Mettre à jour le temps de prépration pris pour les commandes
     * pour le jour en cours, et incrémenter le nombre de commandes
     * préparées pour le jour en cours.
     *
     * @param commande
     */
    public static void mettreAjourTempsPreparation(@NotNull Commande commande) {
        //Récupération.
        String aujourdhuiCommeChaine = getDateCouranteCommeChaine();
        StatGeneral statGeneral = (StatGeneral) orm.chercherNUpletAvecPredicat("WHERE TO_CHAR(DATE_JOUR, '" + SQL_DATE_FORMAT + "') = '" +
                                                                                aujourdhuiCommeChaine + "'",
                                                                                StatGeneral.class);
        int tempsPreparation = (int) ((new Date()).getTime() - commande.getDatetimeCreation().getTime()) / 1000 / 60;

        //Sauvegarde.
        if(statGeneral == null) {
            statGeneral = new StatGeneral();
            statGeneral.setDateJour(new Date());
            statGeneral.setTempsPreparation(tempsPreparation);
            statGeneral.setNbCommandesPreparees(1);
        } else {
            Integer tempsPreparationActuel = statGeneral.getTempsPreparation();
            if(tempsPreparationActuel == null) {
                statGeneral.setTempsPreparation(tempsPreparation);
                statGeneral.setNbCommandesPreparees(1);
            } else {
                statGeneral.setTempsPreparation(tempsPreparationActuel + tempsPreparation);
                statGeneral.setNbCommandesPreparees(statGeneral.getNbCommandesPreparees() + 1);
            }
        }
        orm.persisterNUplet(statGeneral);
    }

    /**
     * Incrémenter le nombre de commandes payées pour le jour en cours.
     */
    public static void incrementerNombreCommandesPayees() {
        //Récupération.
        String aujourdhuiCommeChaine = getDateCouranteCommeChaine();
        StatGeneral statGeneral = (StatGeneral) orm.chercherNUpletAvecPredicat("WHERE TO_CHAR(DATE_JOUR, '" + SQL_DATE_FORMAT + "') = '" +
                                                                                aujourdhuiCommeChaine + "'",
                                                                                StatGeneral.class);
        //Sauvegarde.
        if(statGeneral == null) {
            statGeneral = new StatGeneral();
            statGeneral.setDateJour(new Date());
            statGeneral.setNbCommandesPayees(1);
        } else {
            Integer nbCommandesActuel = statGeneral.getNbCommandesPayees();
            if(nbCommandesActuel == null) {
                statGeneral.setNbCommandesPayees(1);
            } else {
                statGeneral.setNbCommandesPayees(nbCommandesActuel + 1);
            }
        }
        orm.persisterNUplet(statGeneral);
    }

    /**
     * Mettre à jour le chiffre d'affaire pour le jour en cours.
     *
     * @param commande
     */
    public static void mettreAjourChiffreAffaire(@NotNull Commande commande) {
        //Récupération.
        Date aujourdhui = getDateCourante();
        String aujourdhuiCommeChaine = getDateCouranteCommeChaine();
        List<Entite> statChiffreAffaires = orm.chercherNUpletsAvecPredicat("INNER JOIN LIGNE_COMMANDE AS LC "+
                                                                            "ON LC.ID_PLAT = FROM_TABLE.ID_PLAT "+
                                                                            "WHERE TO_CHAR(DATE_JOUR, '" + SQL_DATE_FORMAT + "') = '" +
                                                                            aujourdhuiCommeChaine + "' " +
                                                                            "AND LC.ID_COMMANDE ="+
                                                                            commande.getId(),
                                                                            StatChiffreAffaire.class);

        //Sauvegarde.
        //Si liste vide : pas de statistiques pour les chiffres d'affaires aujourd'hui.
        if(statChiffreAffaires.isEmpty()){
            orm.chercherNUpletsAvecPredicat("WHERE ID_COMMANDE ="+commande.getId(), LigneCommande.class)
                .forEach(entite -> {
                    LigneCommande ligneCommande = (LigneCommande) entite;
                    StatChiffreAffaire statChiffreAffaire = new StatChiffreAffaire();
                    statChiffreAffaire.setDateJour(aujourdhui);
                    statChiffreAffaire.setIdPlat(ligneCommande.getIdPlat());
                    Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID ="+ligneCommande.getIdPlat(),Plat.class);
                    Double chiffreAffaire = plat.getPrix()*ligneCommande.getNbOccurences();

                    //Vérifier si le plat a été commandé pour le déjeuner ou le dinner.
                    if(aujourdhui.getHours() > 11 && aujourdhui.getHours() < 14){
                        statChiffreAffaire.setChiffreAffaireDejeuner(chiffreAffaire);
                        statChiffreAffaire.setChiffreAffaire(chiffreAffaire);
                    }else{
                        statChiffreAffaire.setChiffreAffaireDiner(chiffreAffaire);
                        statChiffreAffaire.setChiffreAffaire(chiffreAffaire);
                    }

                    orm.persisterNUplet(statChiffreAffaire);
                });

        //Sinon : remettre à jour les statistiques.
        }else{
            statChiffreAffaires.forEach(entite -> {
                StatChiffreAffaire statChiffreAffaire = (StatChiffreAffaire) entite;
                Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID ="+statChiffreAffaire.getIdPlat(),Plat.class);
                LigneCommande ligneCommande = (LigneCommande) orm.chercherNUpletAvecPredicat("WHERE ID_PLAT = "+plat.getId()+
                                " AND ID_COMMANDE = "+commande.getId(),
                        LigneCommande.class);
                Double chiffreAffaire = plat.getPrix()*ligneCommande.getNbOccurences();

                if(aujourdhui.getHours() > 11 && aujourdhui.getHours() < 14){
                    if(statChiffreAffaire.getChiffreAffaireDejeuner() == null){
                        statChiffreAffaire.setChiffreAffaireDejeuner(chiffreAffaire);
                    }else{
                        statChiffreAffaire.setChiffreAffaireDejeuner(statChiffreAffaire.getChiffreAffaireDejeuner() +chiffreAffaire);
                    }
                    statChiffreAffaire.setChiffreAffaire(statChiffreAffaire.getChiffreAffaire() + chiffreAffaire);
                }else{
                    if(statChiffreAffaire.getChiffreAffaireDiner() == null){
                        statChiffreAffaire.setChiffreAffaireDiner(chiffreAffaire);
                    }else{
                        statChiffreAffaire.setChiffreAffaireDiner(statChiffreAffaire.getChiffreAffaireDiner() + chiffreAffaire);
                    }
                    statChiffreAffaire.setChiffreAffaire(statChiffreAffaire.getChiffreAffaire() + chiffreAffaire);
                }

                orm.persisterNUplet(statChiffreAffaire);
            });
        }
    }

    /**
     * Consulter les statistiques générales.
     */
    public static void consulterGenerales() {
        //Message du titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Consultation des statistiques générales :");

        //Statistiques des 7 derniers jours.
        List<Entite> statsGenerales7DerniersJours = orm.chercherNUpletsAvecPredicat("WHERE DATE_JOUR >= (NOW()::date - 6)",
                                                                                    StatGeneral.class);
        ui.afficher("Statistiques générales sur les 7 derniers jours :");
        Function<Entite, String> formateurJour = entite -> {
            StatGeneral statGeneral = (StatGeneral) entite;
            String statFormatee = JAVA_DATE_FORMATEUR.format(statGeneral.getDateJour()) +
                                " [ Nombre de clients : "  + statGeneral.getNbCommandesPayees() + "," +
                                " Nombre de commandes (payées) : "  + statGeneral.getNbCommandesPayees() + "," +
                                " Temps mpyen de prépration des commandes : " +
                                Math.round((double) statGeneral.getTempsPreparation() /
                                        (double) statGeneral.getNbCommandesPreparees()) +
                                " ]" ;
            return statFormatee;
        };
        ui.listerNUplets(statsGenerales7DerniersJours, formateurJour);

        //Statistiques des 4 dernières semaines.
        ui.afficher("\nStatistiques générales sur les 4 dernières semaines :");
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
            String statFormatee = JAVA_DATE_FORMATEUR.format(nUplet.get("DATE_DEBUT_SEMAINE")) + " - " +
                                JAVA_DATE_FORMATEUR.format(nUplet.get("DATE_FIN_SEMAINE")) +
                                " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                                " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                                " Temps mpyen de prépration des commandes : " +
                                Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                        ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                                " ]" ;
            return statFormatee;
        };
        statsGeneralesSemaine.forEach(nUplet -> ui.afficher(formateurSemaine.apply(nUplet)));

        //Statistiques des 2 derniers mois.
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
            String statFormatee = nUplet.get("MOIS") +
                                " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                                " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                                " Temps mpyen de prépration des commandes : " +
                                Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                        ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                                " ]" ;
            return statFormatee;
        };
        statsGeneralesMois.forEach(nUplet -> ui.afficher(formateurMois.apply(nUplet)));

        //Statistiques de l'année.
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
            String statFormatee = nUplet.get("ANNEE") +
                                " [ Nombre de clients : "  + nUplet.get("NB_CLIENTS") + "," +
                                " Nombre de commandes (payées) : "  + nUplet.get("NB_COMMANDES_PAYEES") + "," +
                                " Temps mpyen de prépration des commandes : " +
                                Math.round(((Number) nUplet.get("TEMPS_PREPARATION")).doubleValue() /
                                        ((Number) nUplet.get("NB_COMMANDES_PREPAREES")).doubleValue()) +
                                " ]" ;
            return statFormatee;
        };
        statsGeneralesAnnee.forEach(nUplet -> ui.afficher(formateurAnnee.apply(nUplet)));
    }

    /**
     * Constulter les statistiques sur le chiffre d'affaire.
     */
    public static void consulterChiffresAffaire()  {
        //Message de titre.
        ui.afficherAvecDelimiteurEtUtilisateur("Consultation des statistiques sur le chiffre d'affaire :");

        //Obtenir les statistiques des 7 derniers jours.
        ui.afficher("Statistiques sur le chiffre d'affaire pour les 7 derniers jours :");
        String requeteJour = "SELECT " +
                                "STA.DATE_JOUR, " +
                                "SUM(STA.CHIFFRE_AFFAIRE) AS CA, " +
                                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DINER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DINER END ) AS CADI, " +
                                "SUM(CASE WHEN STA.CHIFFRE_AFFAIRE_DEJEUNER IS NULL THEN 0 ELSE STA.CHIFFRE_AFFAIRE_DEJEUNER END) AS CADE " +
                          "FROM STAT_CHIFFRE_AFFAIRE AS STA " +
                          "WHERE STA.DATE_JOUR >= (NOW()::date - 6) "+
                          "GROUP BY STA.DATE_JOUR";
        List<Map<String, Object>> chiffreAffaire7DernierJours = orm.chercherNUplets(requeteJour);
        Function<Map<String, Object>, String> formateurJour = nUplet -> {
            String statFormatee = JAVA_DATE_FORMATEUR.format(nUplet.get("DATE_JOUR")) +
                                " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                                " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                                " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                                " ]" ;
            return statFormatee;
        };
        chiffreAffaire7DernierJours.forEach(nUplet -> ui.afficher(formateurJour.apply(nUplet)));

        //Statistiques des 4 dernières semaines.
        ui.afficher("\nStatistiques sur le chiffre d'affaire sur les 4 dernières semaines :");
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
                                    ") AS STA " +
                                "GROUP BY STA.DATE_DEBUT_SEMAINE, STA.DATE_FIN_SEMAINE " +
                                "ORDER BY STA.DATE_DEBUT_SEMAINE DESC " +
                                "LIMIT 4 " +
                                "OFFSET 0; ";
        List<Map<String, Object>> chiffreAffaireSemaine = orm.chercherNUplets(requeteSemaine);
        Function<Map<String, Object>, String> formateurSemaine = nUplet -> {
            String statFormatee = JAVA_DATE_FORMATEUR.format(nUplet.get("DATE_DEBUT_SEMAINE")) + " - " +
                                 JAVA_DATE_FORMATEUR.format(nUplet.get("DATE_FIN_SEMAINE")) +
                                " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                                " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                                " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                                " ]" ;
            return statFormatee;
        };
        chiffreAffaireSemaine.forEach(nUplet -> ui.afficher(formateurSemaine.apply(nUplet)));

        //Statistiques des 2 derniers mois.
        ui.afficher("\nStatistiques sur le chiffre d'affaire sur les 2 derniers mois :");
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
            String statFormatee = nUplet.get("MOIS") +
                                " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                                " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                                " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                                " ]" ;
            return statFormatee;
        };
        statsCAMois.forEach(nUplet -> ui.afficher(formateurMois.apply(nUplet)));

        //Statistiques de l'année.
        ui.afficher("\nStatistiques sur le chiffre d'affaire sur l'année  :");
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
            String statFormatee = nUplet.get("ANNEE") +
                                " [ Chiffre d'affaire du déjeuner  : "  + nUplet.get("CADE") + " €," +
                                " Chiffre d'affaire du diner  : "  + nUplet.get("CADI") + " €," +
                                " Chiffre d'affaire total  : "  + nUplet.get("CA") +" €"+
                                " ]" ;
            return statFormatee;
        };
        statCAAnnee.forEach(nUplet -> ui.afficher(formateurAnnee.apply(nUplet)));
    }
}