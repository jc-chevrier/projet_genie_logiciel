package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité des ingredients.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Ingredient extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "INGREDIENT";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("LIBELLE", String.class);
        STRUCTURE.put("STOCK", Double.class);
        STRUCTURE.put("ID_UNITE", Integer.class);
    }

    public Ingredient() {
        super();
    }

    public Ingredient(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getLibelle() {
        return (String) get("LIBELLE");
    }

    public void setLibelle(@NotNull String libelle) {
        set("LIBELLE", libelle);
    }

    public Double getStock() {
        return (Double) get("STOCK");
    }

    public void setStock(@NotNull Double stock) {
        set("STOCK", stock);
    }

    public Integer getIdUnite() {
        return (Integer) get("ID_UNITE");
    }

    public void setIdUnite(@NotNull Integer idUnite) {
        set("ID_UNITE", idUnite);
    }

    /**
     * Savoir si un ingrédient est utilisé par des plats.
     *
     * @return
     */
    public boolean estUtiliseParPlat() {
        return ORM.getInstance().compterNUpletsAvecPredicat("WHERE ID_INGREDIENT = " + getId(), PlatIngredients.class) > 0;
    }

    @Override
    public String toString() {
        Integer idUnite = getIdUnite();
        Unite unite = (Unite) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = " + idUnite, Unite.class);
        return "Ingrédient [ id = " + getId() + ", libellé = " + getLibelle() + ", unité = " + unite.getLibelle() +
                ", stock = " + getStock() + " " + unite.getLibelle() + " ]";
    }
}