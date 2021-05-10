package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Entité PlatIngredients.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class PlatIngredients extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "PLAT_INGREDIENTS";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("QUANTITE", Double.class);
        STRUCTURE.put("ID_PLAT", Double.class);
        STRUCTURE.put("ID_INGREDIENT", Integer.class);
    }

    public PlatIngredients() {
        super();
    }

    public PlatIngredients(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public Double getQuantite() {
        return (Double) get("QUANTITE");
    }

    public void setQuantite(@NotNull Double quantite) {
        set("QUANTITE", quantite);
    }

    public Integer getIdPlat() {
        return (Integer) get("ID_PLAT");
    }

    public void setIdPlat(@NotNull Integer idPlat) {
        set("ID_PLAT", idPlat);
    }

    public Integer getIdIngredient() {
        return (Integer) get("ID_INGREDIENT");
    }

    public void setIdIngredient(@NotNull Integer idIngredient) {
        set("ID_INGREDIENT", idIngredient);
    }
}