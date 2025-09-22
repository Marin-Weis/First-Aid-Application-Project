package metier.persistence;

import java.util.LinkedHashSet;

/**
 * Represents a site where DPS (events/interventions) can take place.
 * A Site has a unique code, a name, geographic coordinates, and a set of associated DPS.
 */
public class Site {

    /** Unique code identifying the site */
    private String code;

    /** Display name of the site */
    private String nom;

    /** Longitude coordinate of the site */
    private float longitude;

    /** Latitude coordinate of the site */
    private float latitude;

    /** Set of DPS (interventions) related to this site */
    private LinkedHashSet<DPS> listeDPS;

    /**
     * Constructs a Site with basic information and initial DPS list.
     *
     * @param code unique identifier
     * @param nom name of the site
     * @param longitude longitude coordinate
     * @param latitude latitude coordinate
     * @param tabDPS array of DPS to associate with the site
     */
    public Site(String code, String nom, float longitude, float latitude, DPS[] tabDPS) {
        this.code = code;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.listeDPS = new LinkedHashSet<>();
        for (DPS leDPS : tabDPS) {
            listeDPS.add(leDPS);
        }
    }

    /**
     * Returns the site code.
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the site code.
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the site name.
     * @return the name
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Sets the site name.
     * @param nom the name to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Returns the longitude.
     * @return the longitude
     */
    public float getLongitude() {
        return this.longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude the value to set
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the latitude.
     * @return the latitude
     */
    public float getLatitude() {
        return this.latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude the value to set
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Adds a DPS (intervention) to the site.
     *
     * @param leDPS the DPS to add
     * @throws IllegalArgumentException if the DPS is null
     */
    public void ajouterDPS(DPS leDPS) throws IllegalArgumentException {
        // Check if the DPS is null
        if (leDPS == null) {
            throw new IllegalArgumentException("Erreur ajouterDPS() : le DPS en paramètre est à null!");
        }

        // Ajout du DPS à l'ensemble
        listeDPS.add(leDPS);
    }
}
