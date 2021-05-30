package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entité des plats du catalogue du restaurant.
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

    //Prédicat permettant de détermminer si pour un plat
    //tous les ingrédients sont disponibles en stock avec
    //les quantités minimales nécessaires.
    public final static String PREDICAT_DISPONIBLE_EN_STOCK =  "ID NOT IN " +
                                                                   "(SELECT P.ID " +
                                                                    "FROM PLAT AS P " +
                                                                    "INNER JOIN PLAT_INGREDIENTS AS PI " +
                                                                    "ON PI.ID_PLAT = P.ID " +
                                                                    "INNER JOIN INGREDIENT AS I " +
                                                                    "ON I.ID = PI.ID_INGREDIENT " +
                                                                    "WHERE PI.QUANTITE > I.STOCK)";

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

    public void setIdCategorie(Integer idCategorie) {
        set("ID_CATEGORIE", idCategorie);
    }

    /**
     * Savoir si un plat est utilisé par des lignes de commande.
     *
     * @return
     */
    public boolean estUtiliseParLigneCommande() {
        return ORM.getInstance().compterNUpletsAvecPredicat("WHERE ID_PLAT = " + getId(), LigneCommande.class) > 0;
    }

    @Override
    public String toString() {
        ORM orm = ORM.getInstance();
        Integer id = getId();

        //Formatage du plat en chaine de caractères.
        Categorie categorie = (Categorie) orm.chercherNUpletAvecPredicat("WHERE ID = " + getIdCategorie(), Categorie.class);
        String contenu = "Plat [ id = " + id +
                         ", libellé = " + getlibelle() +
                         ", prix = " + getPrix() + " €, catégorie = " + categorie.getLibelle() +
                         ", carte = " + (getCarte() == 1 ? "oui" : "non") +
                         " ]\nComposition [ ";

        //Formatage de la composition du plat en chaine de caractères.
        List<Entite> platIngredients = orm.chercherNUpletsAvecPredicat("WHERE ID_PLAT = " + id, PlatIngredients.class);
        int nbPlatsIngredients = platIngredients.size();
        for(int index = 0; index < nbPlatsIngredients; index++) {
            PlatIngredients platIngredient = (PlatIngredients) platIngredients.get(index);
            Ingredient ingredient = (Ingredient) orm.chercherNUpletAvecPredicat("WHERE ID = " + platIngredient.getIdIngredient(), Ingredient.class);
            Unite unite = (Unite) orm.chercherNUpletAvecPredicat("WHERE ID = " + ingredient.getIdUnite(), Unite.class);
            contenu += "(" + ingredient.getLibelle() + " : " +
                       platIngredient.getQuantite() + " " + unite.getLibelle() + ")" +
                       ((index < (nbPlatsIngredients - 1)) ? ", " : " ]");
        }

        return contenu;
    }
}