package fr.ul.miage.m1.projet_genie_logiciel.entites;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité rôle.
 *
 * @author CHEVRIER, HADJ MESSAOUD, LOUGADI
 */
public class Place extends Entite {
    //Nom de la table correspondant à l'entité.
    public static String NOM_TABLE = "PLACE";
    //Structure de l'entité [attribut -> type].
    public static Map<String, Class> STRUCTURE;

    //Initialisation de la structure.
    static {
        STRUCTURE = new HashMap<String, Class>();
        STRUCTURE.put("ID", Integer.class);
        STRUCTURE.put("ETAT", String.class);
        STRUCTURE.put("DATETIME_RESERVATION", Date.class);
        STRUCTURE.put("NOM_RESERVATION", String.class);
        STRUCTURE.put("PRENOM_RESERVATION", String.class);
        STRUCTURE.put("ID_COMPTE_SERVEUR", Integer.class);
    }

    public Place() {
        super();
    }

    public Place(@NotNull Map<String, Object> attributs) {
        super(attributs);
    }

    public String getEtat() {
        return (String) get("ETAT");
    }

    public void setEtat(@NotNull String etat) {
        set("ETAT", etat);
    }

    public Date getDatetimeReservation() {
        return (Date) get("DATETIME_RESERVATION");
    }

    public void setDatetimeReservation(Date datetimeReservation) { set("DATETIME_RESERVATION", datetimeReservation);}

    public String getNomReservation() {
        return (String) get("NOM_RESERVATION");
    }

    public void setNomReservation(String nomReservation) {
        set("NOM_RESERVATION", nomReservation);
    }

    public String getPrenomReservation() {
        return (String) get("PRENOM_RESERVATION");
    }

    public void setPrenomReservation(String prenomReservation) { set("PRENOM_RESERVATION", prenomReservation);}

    public Integer getIdCompteServeur(){return (Integer) get("ID_COMPTE_SERVEUR");}

    public void setIdCompteServeur(@NotNull Integer idCompteServeur) { set("ID_COMPTE_SERVEUR", idCompteServeur);}

}
