package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité stat chiffre d'affaire.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class StatChiffreAffaire extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "STAT_CHIFFRE_AFFAIRE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("DATE_JOUR", Date.class);
        STRUCTURE.put("CHIFFRE_AFFAIRE_DEJEUNER", Date.class);
        STRUCTURE.put("CHIFFRE_AFFAIRE_DINER", Double.class);
        STRUCTURE.put("CHIFFRE_AFFAIRE", Double.class);
        STRUCTURE.put("ID_PLAT", Integer.class);
    }

    public StatChiffreAffaire () {
        super();
    }

    public StatChiffreAffaire (@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public Date getDateJour() {
        return (Date) get("DATE_JOUR");
    }

    public void setDateJour(@NotNull Date dateJour) {
        set("DATE_JOUR", dateJour);
    }

    public Double getChiffreAffaireDejeuner() {
        return (Double) get("CHIFFRE_AFFAIRE_DEJEUNER");
    }

    public void setChiffreAffaireDejeuner(Double chiffreAffaireDejeuner) {
        set("CHIFFRE_AFFAIRE_DEJEUNER", chiffreAffaireDejeuner);
    }

    public Double getChiffreAffaireDiner() {
        return (Double) get("CHIFFRE_AFFAIRE_DINER");
    }

    public void setChiffreAffaireDiner(Double chiffreAffaireDiner) {
        set("CHIFFRE_AFFAIRE_DINER", chiffreAffaireDiner);
    }

    public Double getChiffreAffaire() {
        return (Double) get("CHIFFRE_AFFAIRE");
    }

    public void setChiffreAffaire(Double chiffreAffaire) {
        set("CHIFFRE_AFFAIRE", chiffreAffaire);
    }

    public Integer getIdPlat() {
        return (Integer) get("ID_PLAT");
    }

    public void setIdPlat(@NotNull Integer idPlat) {
        set("ID_PLAT", idPlat);
    }
}
