package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité des catégories des plats.
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

    /**
     * Savoir si une catégorie est utilisée par des plats.
     *
     * @return
     */
    public boolean estUtiliseeParPlat() {
        return ORM.getInstance().compterNUpletsAvecPredicat("WHERE ID_CATEGORIE = " + getId(), Plat.class) > 0;
    }

    @Override
    public String toString() {
        return "Catégorie [ id = " + getId() + ", libellé = " + getLibelle() + " ]";
    }
}