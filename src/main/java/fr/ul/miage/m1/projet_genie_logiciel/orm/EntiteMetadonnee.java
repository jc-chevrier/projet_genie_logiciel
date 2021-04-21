package fr.ul.miage.m1.projet_genie_logiciel.orm;

import fr.ul.miage.m1.projet_genie_logiciel.entites.Entite;
import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * Outils pour les métadonnées des entités.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class EntiteMetadonnee {
    /**
     * Récupérer la structure d'une entité.
     *
     * @param entiteClasse
     * @return
     */
    public static String getEntiteNomTable(@NotNull Class entiteClasse) {
        String entiteNomTable = null;
        try {
            entiteNomTable = (String) entiteClasse.getDeclaredField("NOM_TABLE").get(String.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return entiteNomTable;
    }

    /**
     * Récupérer la structure d'une entité.
     *
     * @param entiteClasse
     * @return
     */
    public static Map<String, Class> getEntiteStructure(@NotNull Class entiteClasse) {
        Map<String, Class> entiteStructure = null;
        try {
            entiteStructure = (Map<String, Class>) entiteClasse.getDeclaredField("STRUCTURE").get(Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return entiteStructure;
    }

    /**
     * Créer un nouvel n-uplet d'une entité.
     *
     * @param entiteClasse
     * @return
     */
    public static Entite instancierNUplet(@NotNull Class entiteClasse,
                                          @NotNull Map<String, Object> nUpletAttributs) {
        Entite nUplet = null;
        try {
            nUplet = (Entite) entiteClasse.getDeclaredConstructor(Map.class).newInstance(nUpletAttributs);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return nUplet;
    }
}
