package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entité ligne de commande.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class LigneCommande extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "LIGNE_COMMANDE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("NB_OCCURENCES", Integer.class);
        STRUCTURE.put("ETAT", String.class);
        STRUCTURE.put("ID_PLAT", Integer.class);
        STRUCTURE.put("ID_COMMANDE", Integer.class);
    }

    public LigneCommande() {
        super();
    }

    public LigneCommande(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public Integer getNbOccurences() {
        return (Integer) get("NB_OCCURENCES");
    }

    public void setNbOccurences(@NotNull Integer nbOccurences) {
        set("NB_OCCURENCES", nbOccurences);
    }

    public String getEtat() {
        return (String) get("ETAT");
    }

    public void setEtat(@NotNull String etat) {
        set("ETAT", etat);
    }

    public Integer getIdPlat() {
        return (Integer) get("ID_PLAT");
    }

    public void setIdPlat(@NotNull Integer idPlat) {
        set("ID_PLAT", idPlat);
    }

    public Integer getIdCommande() {
        return (Integer) get("ID_COMMANDE");
    }

    public void setIdCommande(@NotNull Integer idCommande) {
        set("ID_COMMANDE", idCommande);
    }

    @Override
    public String toString() {
        ORM orm = ORM.getInstance();
        Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = "  + getIdPlat(), Plat.class);
        String contenu = "Ligne de commande [ id = " + getId() +
                ", état = " + getEtat() +
                ", id de la commande = " + getIdCommande() + " ]\n" + getNbOccurences() + "x " + plat.toString();
        return contenu;
    }
}