package fr.ul.miage.m1.projet_genie_logiciel.entites;

import fr.ul.miage.m1.projet_genie_logiciel.orm.ORM;
import org.jetbrains.annotations.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entité des tables du restaurant.
 *
 * Elle a nommée "PLACE" car "TABLE" est un mot clé réservé en SQL.
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

    public Place() { super(); }

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

    public void setIdCompteServeur(Integer idCompteServeur) { set("ID_COMPTE_SERVEUR", idCompteServeur);}

    public String toEtatString() {
        return "Table [ id = " + getId() + ", état = " + getEtat() + " ]";
    }

    /**
     * Savoir si un table est utilisée par des commandes.
     *
     * @return
     */
    public boolean estUtiliseeParCommandeOuServeur() {
        ORM orm =  ORM.getInstance();
        int id = getId();
        int nbCommandes = orm.compterNUpletsAvecPredicat("WHERE ID_PLACE = " + id, Commande.class);
        int aServeur = orm.compterNUpletsAvecPredicat("WHERE ID_COMPTE_SERVEUR IS NOT NULL AND ID = " + id, Place.class);
        return nbCommandes > 0 || aServeur == 1;
    }

    /**
     * Formateur en chaine de caractères partiel, permettant d'afficher
     * que de manière partielle une table, en ne conservant que l'id,
     * l'état, et le serveur de la table.
     *
     * @return
     */
    public String toEtatServeurString() {
        Compte serveur = (Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = " + getIdCompteServeur(), Compte.class);
        return "Table [ id = " + getId() +
               ", état = " + getEtat() +
               (serveur == null ?
               ", pas de serveur associé" : (", serveur = " + serveur.getNom() + " " + serveur.getPrenom())) + " ]";
    }

    @Override
    public String toString() {
        Compte serveur = (Compte) ORM.getInstance().chercherNUpletAvecPredicat("WHERE ID = " + getIdCompteServeur(), Compte.class);
        return "Table [ id = " + getId() +
               ", état = " + getEtat() +
               (serveur == null ?
               ", pas de serveur associé" : (", serveur = " + serveur.getNom() + " " + serveur.getPrenom())) +
               (getDatetimeReservation() == null ?
               ", pas de réservation associée" : (", réservation = " + getNomReservation() + " " + getPrenomReservation() +
               ", date de réservation = " + getDatetimeReservation().toLocaleString())) + " ]";
    }
}