package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité Plat.
 *
 * @author CHEVRIER, HADJ MESSAOUD,LOUGADI
 */
public class Plat extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "PLAT";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("LIBELLE", String.class);
        STRUCTURE.put("PRIX", Double.class);
        STRUCTURE.put("CARTE", Integer.class);
        STRUCTURE.put("ID_CATEGORIE", Integer.class);
    }

    public Plat() {
        super();
    }

    public Plat(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getlibelle() {
        return (String) get("LIBELLE");
    }

    public void setLibelle(@NotNull String libelle) {
        set("LIBELLE", libelle);
    }

    public Double getPrix() {
        return (Double) get("PRIX");
    }

    public void setPrix(@NotNull Double prix) {
        set("PRIX", prix);
    }

    public Integer getCarte() {
        return (Integer) get("CARTE");
    }

    public void setCarte(@NotNull Integer carte) {
        set("CARTE", carte);
    }

    public Integer getIdCategorie() {
        return (Integer) get("ID_CATEGORIE");
    }

    public void setIdCategorie(@NotNull Integer idCategorie) {
        set("ID_CATEGORIE", idCategorie);
    }


}
