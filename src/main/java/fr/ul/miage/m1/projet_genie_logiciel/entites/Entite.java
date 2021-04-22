package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Superlasse des entités.
 *
 * @author CHEVRIER, HADJ MESSAOUD,LOUGADI
 */
public abstract class Entite {
    protected Map<String, Object> attributs;

    /**
     * Créer un nouvel n-uplet sur l'application.
     */
    public Entite() {
        attributs = new HashMap<String, Object>();
        set("ID", null);
    }

    /**
     * Parser en objet un n-uplet de la base de données.
     * (Constructeur utilisé par l'ORM).
     */
    public Entite(@NotNull Map<String, Object> attributs) {
        this.attributs = attributs;
    }

    /**
     * Obtenir un attribut.
     *
     * @param attribut
     * @return
     */
    public Object get(@NotNull String attribut) {
        if(!attributs.containsKey(attribut)) {
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
