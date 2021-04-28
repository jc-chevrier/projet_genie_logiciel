package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.EntiteMetadonnee;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Superlasse des entités.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public abstract class Entite {
    ///Attributs de l'entité.
    protected Map<String, Object> attributs;

    /**
     * Créer un nouvel n-uplet sur l'application.
     */
    public Entite() {
        Map<String, Class> structure = EntiteMetadonnee.getEntiteStructure(getClass());
        attributs = new HashMap<String, Object>();
        for(String attribut : structure.keySet()) {
            set(attribut, null);
        }
    }

    /**
     * Parser en objet un n-uplet de la base de données.
     * (Constructeur utilisé par l'ORM).
     */
    public Entite(@NotNull Map<String, Object> attributs) {
        this.attributs = attributs;
    }

    /**
     * Savoir si le n-uplet a renseigné un attribut.
     */
    public boolean renseigne(@NotNull String attribut) {
        return attributs.containsKey(attribut);
    }

    /**
     * Obtenir un attribut.
     *
     * @param attribut
     * @return
     */
    public Object get(@NotNull String attribut) {
        if(!renseigne(attribut)) {
            throw new IllegalArgumentException("L'attribut " + attribut + " est introuvale !");
        }
        return attributs.get(attribut);
    }

    /**
     * Modifier / ajouter un attribut.
     *
     * @param attribut
     * @param valeur
     * @return
     */
    public Object set(@NotNull String attribut, Object valeur) {
        return attributs.put(attribut, valeur);
    }

    /**
     * Obtenir l'attribut ID.
     *
     * @return
     */
    public Integer getId() {
        return (Integer) get("ID");
    }

    @Override
    public String toString() {
        return "Entite{" +
                    "attributs=" + attributs +
               "}";
    }
}
