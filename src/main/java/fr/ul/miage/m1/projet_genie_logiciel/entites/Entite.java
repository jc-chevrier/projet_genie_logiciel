package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;
import java.util.Map;

public abstract class Entite {
    protected Map<String, Object> attributs;

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
     * Modifier un attribut.
     *
     * @param attribut
     * @param valeur
     * @return
     */
    public Object set(@NotNull String attribut, @NotNull Object valeur) {
        if(!attributs.containsKey(attribut)) {
            throw new IllegalArgumentException("L'attribut " + attribut + " est introuvale !");
        }
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
                '}';
    }
}
