package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité stat générale.
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
        STRUCTURE.put("TEMPS_PREPARATION", Double.class);
        STRUCTURE.put("NB_COMMANDES", Integer.class);
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

    public Double getTempsPreparation() {
        return (Double) get("TEMPS_PREPARATION");
    }

    public void setTempsPreparation(Double tempsPreparation) { set("TEMPS_PREPARATION", tempsPreparation);}

    public Integer getNbCommandes() {return (Integer) get("NB_COMMANDES");}

    public void setNbCommandes(Integer nbCommandes) { set("NB_COMMANDES", nbCommandes);}

    public Integer getNbClients() { return (Integer) get("NB_CLIENTS");}

    public void setNbClients(Integer nbClients) { set("NB_CLIENTS", nbClients);}
}

