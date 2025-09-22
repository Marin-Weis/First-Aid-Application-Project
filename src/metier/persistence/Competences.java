package metier.persistence;

/**
 * This class represents a competence with an intitule (title).
 * It provides methods to get and set the intitule of the competence.
 *
 * @author Nolann L
 * @author Marin W
 * @author Arthur J
 * @author Mattjieu G
 */
public class Competences {
    /** The title of the competence */
    private String intitule;

    /**
     * Constructs a Competences object with the specified intitule.
     * @param intitule the title of the competence
     */
    public Competences(String intitule) {
        this.intitule = intitule;
    }

    /**
     * Returns the intitule of the competence.
     * @return the intitule
     */
    public String getIntitule() {
        return this.intitule;
    }

    /**
     * Sets the intitule of the competence.
     * @param intitule the intitule to set
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
}