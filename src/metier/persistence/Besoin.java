package metier.persistence;

/**
 * This class represents a need for a certain number of rescuers with specific competencies
 * for a given DPS (Dispositif Prévisionnel de Secours).
 * It contains the number of rescuers required, the title of the competence needed, and the ID of the DPS.
 *
 * @author Nolann L
 * @author Marin W
 * @author Arthur J
 * @author Mattjieu G
 */
public class Besoin {
    /** The number of rescuers required for the DPS */
    private int nombreSecouriste;
    /** The title of the competence needed for the DPS */
    private String intituleComp;
    /** The ID of the DPS for which the rescuers are needed */
    private long idDPS;

    /**
     * Constructs a Besoin object with the specified number of rescuers, competence title, and DPS ID.
     * @param nombreSecouriste the number of rescuers required
     * @param intituleComp     the title of the competence needed
     * @param idDPS            the ID of the DPS
     */
    public Besoin(int nombreSecouriste, String intituleComp, long idDPS) {
        this.nombreSecouriste = nombreSecouriste;
        this.intituleComp = intituleComp;
        this.idDPS = idDPS;
    }

    /**
     * Returns the number of rescuers required.
     * @return the number of rescuers
     */
    public int getNombreSecouriste() {
        return this.nombreSecouriste;
    }

    /**
     * Sets the number of rescuers required.
     * @param nombreSecouriste the number of rescuers to set
     */
    public void setNombreSecouriste(int nombreSecouriste) {
        this.nombreSecouriste = nombreSecouriste;
    }

    /**
     * Returns the title of the competence needed.
     * @return the competence title
     */
    public String getIntituleComp() {
        return this.intituleComp;
    }

    /**
     * Sets the title of the competence needed.
     * @param intituleComp the competence title to set
     */
    public void setIntituleComp(String intituleComp) {
        this.intituleComp = intituleComp;
    }

    /**
     * Returns the ID of the DPS.
     * @return the DPS ID
     */
    public long getIdDPS() {
        return this.idDPS;
    }

    /**
     * Sets the ID of the DPS.
     * @param idDPS the DPS ID to set
     */
    public void setIdDPS(int idDPS) {
        this.idDPS = idDPS;
    }
}
