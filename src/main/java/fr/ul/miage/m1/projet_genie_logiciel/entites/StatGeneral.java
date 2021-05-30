package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité des statistiques générales :
 * - nombre de clients par jour ;
 * - nombre de commandes préparées par jour ;
 * - nombre de commandes payées par jour ;
 * - temps de préparation par jour.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class StatGeneral extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "STAT_GENERAL";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("DATE_JOUR", Date.class);
        STRUCTURE.put("TEMPS_PREPARATION", Integer.class);
        STRUCTURE.put("NB_COMMANDES_PREPAREES", Integer.class);
        STRUCTURE.put("NB_COMMANDES_PAYEES", Integer.class);
        STRUCTURE.put("NB_CLIENTS", Integer.class);
    }

    public StatGeneral () {
        super();
    }

    public StatGeneral (@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public Date getDateJour() {
        return (Date) get("DATE_JOUR");
    }

    public void setDateJour(@NotNull Date dateJour) {
        set("DATE_JOUR", dateJour);
    }

    public Integer getTempsPreparation() {
        return (Integer) get("TEMPS_PREPARATION");
    }

    public void setTempsPreparation(Integer tempsPreparation) { set("TEMPS_PREPARATION", tempsPreparation);}

    public Integer getNbCommandesPreparees() {return (Integer) get("NB_COMMANDES_PREPAREES");}

    public void setNbCommandesPreparees(Integer nbCommandesPreparees) { set("NB_COMMANDES_PREPAREES", nbCommandesPreparees);}

    public Integer getNbCommandesPayees() {return (Integer) get("NB_COMMANDES_PAYEES");}

    public void setNbCommandesPayees(Integer nbCommandesPayees) { set("NB_COMMANDES_PAYEES", nbCommandesPayees);}

    public Integer getNbClients() { return (Integer) get("NB_CLIENTS");}

    public void setNbClients(Integer nbClients) { set("NB_CLIENTS", nbClients);}
}