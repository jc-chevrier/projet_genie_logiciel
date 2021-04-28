package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entité Plat.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
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



    @Override
    public String toString() {
        ORM orm = ORM.getInstance();
        String contenu = "Plat [ ";
        Object id = attributs.get("ID");
        contenu +=  "id = " + id + ", ";
        Object libelle = attributs.get("LIBELLE");
        contenu +=  "libellé = " + libelle + ", ";
        Object prix = attributs.get("PRIX");
        contenu +=  "prix = " + prix + " € ]\nComposition [ ";
        //TODO catégorie.
        List<Entite> platIngredients = orm.chercherNUpletsAvecPredicat(
                                                                "WHERE ID_PLAT = "  + id,
                                                                 PlatIngredients.class);
        int nbPlatsIngredients = platIngredients.size();
        for(int index = 0; index < nbPlatsIngredients; index++) {
            PlatIngredients platIngredient = (PlatIngredients) platIngredients.get(index);
            Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat(
                                                        "WHERE ID = " + platIngredient.getIdIngredient(),
                                                        Ingredient.class);
            Unite unite = (Unite) orm.chercherNUpletAvecPredicat(
                                                        "WHERE ID = " + ingredient.getIdUnite(),
                                                         Unite.class);
            contenu += "(" + ingredient.getLibelle() + " : " +
                       platIngredient.getQuantite() + " " + unite.getLibelle() + ")" +
                       ((index < (nbPlatsIngredients - 1)) ? ", " : " ]");
        }
        return contenu;
    }
}
