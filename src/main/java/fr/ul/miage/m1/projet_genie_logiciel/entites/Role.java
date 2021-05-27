package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité des rôles des utilisateurs.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Role extends Entite {
    //Ids des rôles existants.
    public final static int DIRECTEUR = 1;
    public final static int MAITRE_HOTEL = 2;
    public final static int CUISINIER = 3;
    public final static int SERVEUR = 4;
    public final static int ASSISTANT_SERVICE = 5;

    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "ROLE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("LIBELLE", String.class);
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

    @Override
    public String toString() {
        return "Rôle [ id = " + getId() + ", libellé = " + getLibelle() + " ]";
    }
}