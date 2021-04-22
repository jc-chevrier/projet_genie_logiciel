package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité role.
 *
 * @author CHEVRIER, HADJ MESSAOUD,LOUGADI
 */
public class Role extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "ROLE";
    //Structure de l'entité.
    public static Map<String, String> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, String>();
        STRUCTURE.put("ID", "Integer");
        STRUCTURE.put("LIBELLE", "String");
    }

    public Role() {
        super();
    }

    public Role(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getLibelle() {
        return (String) get("LIBELLE");
    }

    public void setLibelle(@NotNull String libelle) {
         set("LIBELLE", libelle);
    }
}
