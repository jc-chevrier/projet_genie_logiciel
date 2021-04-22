package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class Role extends Entite {
    public static String NOM_TABLE = "ROLE";
    public static Map<String, String> STRUCTURE;

    static {
        STRUCTURE = new HashMap<String, String>();
        STRUCTURE.put("ID", "Integer");
        STRUCTURE.put("LIBELLE", "String");
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
