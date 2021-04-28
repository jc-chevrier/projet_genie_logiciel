package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité Ingredient.
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



    @Override
    public String toString() {
        String contenu = "Ingrédient [ ";
        Object id = attributs.get("ID");
        contenu +=  "id = " + id + ", ";
        Object libelle = attributs.get("LIBELLE");
        contenu +=  "libellé = " + libelle + ", ";
        Object idUnite = attributs.get("ID_UNITE");
        Unite unite = (Unite) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = " + idUnite, Unite.class);
        contenu +=  "unité = " + unite.getLibelle() + ", ";
        Object stock = attributs.get("STOCK");
        contenu +=  "stock = " + stock + " " + unite.getLibelle() + " ]";
        return contenu;
    }
}
