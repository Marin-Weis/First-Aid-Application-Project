package metier.persistence;

/**
 * Represents a requirement for a specific skill (competence).
 * This class is used to link a resource (e.g., a sport or event) with the required skill.
 */
public class Requiert {

    /** The title of the required skill (e.g., "PSE1", "SSA") */
    private String intituleComp;

    /**
     * Constructs a new Requiert instance with the given skill title.
     *
     * @param intituleComp the title of the required skill
     */
    public Requiert(String intituleComp) {
        this.intituleComp = intituleComp;
    }

    /**
     * Returns the title of the required skill.
     *
     * @return the skill title
     */
    public String getIntituleComp() {
        return this.intituleComp;
    }

    /**
     * Sets the title of the required skill.
     *
     * @param intituleComp the new skill title
     */
    public void setIntituleComp(String intituleComp) {
        this.intituleComp = intituleComp;
    }

    /**
     * Computes the hash code based on the skill name.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        if (intituleComp == null) {
            return 0;
        }
        return intituleComp.hashCode();
    }
}
