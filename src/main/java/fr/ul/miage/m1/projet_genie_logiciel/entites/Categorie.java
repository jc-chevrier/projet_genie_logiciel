package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité catégorie.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Categorie extends Entite {

    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "CATEGORIE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("LIBELLE", String.class);
    }

    public Categorie() {
        super();
    }

    public Categorie(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getLibelle() {
        return (String) get("LIBELLE");
    }

    public void setLibelle(@NotNull String libelle) {
        set("LIBELLE", libelle);
    }
    @Override
    public String toString() {
        return "Catégorie [ id = " + getId() + ", libellé = " + getLibelle() + " ]";
    }
    public boolean estUtiliseParPlat() {
        return ORM.getInstance().compterNUpletsAvecPredicat(
                "INNER JOIN PLAT AS P " +
                        "ON P.ID_CATEGORIE = " + getId(), Categorie.class) > 0;
    }
}