package metier.persistence;

/**
 * This class represents an assignment of a rescuer to a specific DPS (Dispositif Prévisionnel de Secours).
 * It contains the rescuer's ID, the title of the competence required, and the ID of the DPS.
 *
 * @author Nolann L
 * @author Marin W
 * @author Arthur J
 * @author Mattjieu G
 */
public class Affectation {
    /** The ID of the rescuer assigned to the DPS */
    private long idSecouriste;
    /** The title of the competence required for the assignment */
    private String intituleComp;
    /** The ID of the DPS to which the rescuer is assigned */
    private long idDPS;

    /**
     * Constructs an Affectation object with the specified rescuer ID, competence title, and DPS ID.
     * @param idSecouriste the ID of the rescuer
     * @param intituleComp the title of the competence required
     * @param idDPS        the ID of the DPS
     */
    public Affectation(long idSecouriste, String intituleComp, long idDPS) {
        this.idSecouriste = idSecouriste;
        this.intituleComp = intituleComp;
        this.idDPS = idDPS;
    }

    /**
     * Returns the ID of the rescuer.
     * @return the rescuer's ID
     */
    public long getIdSecouriste() {
        return this.idSecouriste;
    }

    /**
     * Sets the ID of the rescuer.
     * @param idSecouriste the rescuer's ID to set
     */
    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }

    /**
     * Returns the title of the competence required.
     * @return the competence title
     */
    public String getIntituleComp() {
        return this.intituleComp;
    }

    /**
     * Sets the title of the competence required.
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

    public void showAffectation() {
        String message = "Affectation | secouriste : " + this.getIdSecouriste() + " | la compétence : " + this.intituleComp + " | idDPS : " + this.getIdDPS() + "\n";
        System.out.println(message);
    }
}
