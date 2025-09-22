package metier.persistence;

import java.sql.Date;
import java.sql.Time;

/**
 * Represents a DPS (poste de secours) entity in the system.
 * A DPS contains event details such as time, location, type of sport, and description.
 */
public class DPS {

    /** Unique identifier for the DPS */
    private long id;

    /** Label or title of the DPS */
    private String label;

    /** Date when the DPS takes place */
    private Date date;

    /** Starting time of the DPS */
    private Time heureDebut;

    /** Ending time of the DPS */
    private Time heureFin;

    /** Sport associated with the event */
    private String sportAssocie;

    /** Site code where the DPS is held */
    private int codeSite;

    /** Location of the DPS */
    private String lieu;

    /** Optional description of the DPS */
    private String description;

    /** Color used for UI representation of the DPS (optional) */
    private String couleur;

    /**
     * Constructs a DPS with the specified values.
     *
     * @param id the unique ID of the DPS
     * @param label the name or label of the DPS
     * @param date the date of the DPS
     * @param heureDebut the start time of the DPS
     * @param heureFin the end time of the DPS
     * @param sportAssocie the associated sport
     * @param codeSite the site code
     * @param lieu the location of the DPS
     * @param description the description of the DPS
     */
    public DPS(long id, String label, Date date, Time heureDebut, Time heureFin, String sportAssocie, int codeSite, String lieu, String description) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.sportAssocie = sportAssocie;
        this.codeSite = codeSite;
        this.lieu = lieu;
        this.description = description;
    }

    // Getters & Setters

    /**
     * Returns the ID of the DPS.
     * @return the ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the DPS.
     * @param id the ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the label of the DPS.
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the DPS.
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the date of the DPS.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the DPS.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the start time of the DPS.
     * @return the start time
     */
    public Time getHeureDebut() {
        return heureDebut;
    }

    /**
     * Sets the start time of the DPS.
     * @param heureDebut the start time to set
     */
    public void setHeureDebut(Time heureDebut) {
        this.heureDebut = heureDebut;
    }

    /**
     * Returns the end time of the DPS.
     * @return the end time
     */
    public Time getHeureFin() {
        return heureFin;
    }

    /**
     * Sets the end time of the DPS.
     * @param heureFin the end time to set
     */
    public void setHeureFin(Time heureFin) {
        this.heureFin = heureFin;
    }

    /**
     * Returns the associated sport of the DPS.
     * @return the sport name
     */
    public String getSportAssocie() {
        return sportAssocie;
    }

    /**
     * Sets the associated sport of the DPS.
     * @param sportAssocie the sport to set
     */
    public void setSportAssocie(String sportAssocie) {
        this.sportAssocie = sportAssocie;
    }

    /**
     * Returns the site code of the DPS.
     * @return the site code
     */
    public int getCodeSite() {
        return codeSite;
    }

    /**
     * Sets the site code of the DPS.
     * @param codeSite the code to set
     */
    public void setCodeSite(int codeSite) {
        this.codeSite = codeSite;
    }

    /**
     * Returns the location of the DPS.
     * @return the location
     */
    public String getLieu() {
        return lieu;
    }

    /**
     * Sets the location of the DPS.
     * @param lieu the location to set
     */
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    /**
     * Returns the description of the DPS.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the DPS.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the color for UI representation.
     * @return the color
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * Sets the color for UI representation.
     * @param couleur the color to set
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
