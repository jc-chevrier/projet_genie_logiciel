package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité Compte.
 *
<<<<<<< HEAD
<<<<<<< HEAD
 * @author CHEVRIER, HADJ MESSAOUD,LOUGADI
=======
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
>>>>>>> ad2e01bfd92cc902f7f5c4dd52063f20b6480318
=======
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
>>>>>>> 198fc4e98eb3e7e0c548e20cd9ba8574a7386d14
 */
public class Compte extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "COMPTE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("PRENOM", String.class);
        STRUCTURE.put("NOM", String.class);
        STRUCTURE.put("ACTIF", Integer.class);
        STRUCTURE.put("ID_ROLE", Integer.class);
    }

    public Compte() {
        super();
    }

    public Compte(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getPrenom() {
        return (String) get("PRENOM");
    }

    public void setPrenom(@NotNull String prenom) {
        set("PRENOM", prenom);
    }

    public String getNom() {
        return (String) get("NOM");
    }

    public void setNom(@NotNull String nom) {
        set("NOM", nom);
    }

    public Integer getActif() {
        return (Integer) get("ACTIF");
    }

    public void setActif(@NotNull Integer actif) {
        set("ACTIF", actif);
    }

    public Integer getIdRole() {
        return (Integer) get("ID_ROLE");
    }

    public void setIdRole(@NotNull Integer idRole) {
        set("ID_ROLE", idRole);
    }
}
