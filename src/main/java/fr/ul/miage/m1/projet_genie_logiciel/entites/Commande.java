package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entité des commandes des clients.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Commande extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "COMMANDE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("DATETIME_CREATION", Date.class);
        STRUCTURE.put("COUT_TOTAL", Double.class);
        STRUCTURE.put("ETAT", String.class);
        STRUCTURE.put("ID_PLACE", Integer.class);
    }

    public Commande() {
        super();
    }

    public Commande(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public Date getDatetimeCreation() {
        return (Date) get("DATETIME_CREATION");
    }

    public void setDatetimeCreation(@NotNull Date datetimeCreation) {
        set("DATETIME_CREATION", datetimeCreation);
    }

    public Double getCoutTotal() {
        return (Double) get("COUT_TOTAL");
    }

    public void setCoutTotal(@NotNull Double coutTotal) {
        set("COUT_TOTAL", coutTotal);
    }

    public String getEtat() {
        return (String) get("ETAT");
    }

    public void setEtat(@NotNull String etat) {
        set("ETAT", etat);
    }

    public Integer getIdPlace() {
        return (Integer) get("ID_PLACE");
    }

    public void setIdPlace(@NotNull Integer idPlace) {
        set("ID_PLACE", idPlace);
    }

    public String toString() {
        ORM orm = ORM.getInstance();
        Integer id = getId();

        //Formatage de la commande en chaine de caractères.
        String contenu = "Commande [ id = " + id +
                         ", table = #" + getIdPlace() +
                         ", date de création = " + getDatetimeCreation().toLocaleString() +
                         ", coût total = " + getCoutTotal() + " €, état = " + getEtat() +
                         " ]\nComposition [ ";

        //Formatage de la composition de la commande en chaine de caractères.
        List<Entite> ligneCommandes = orm.chercherNUpletsAvecPredicat("WHERE ID_Commande = " + id, LigneCommande.class);
        int nbLignesCommandes = ligneCommandes.size();
        for(int index = 0; index < nbLignesCommandes; index++) {
            LigneCommande ligneCommande = (LigneCommande) ligneCommandes.get(index);
            Plat plat = (Plat) orm.chercherNUpletAvecPredicat("WHERE ID = " + ligneCommande.getIdPlat(), Plat.class);
            contenu += "(" + plat.getlibelle() + ", " +
                       plat.getPrix() +" €, x"+
                       ligneCommande.getNbOccurences() + ")" +
                       ((index < (nbLignesCommandes - 1)) ? ", " : " ]");
        }

        return contenu;
    }
}