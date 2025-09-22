package metier.persistence;

/**
 * Represents a sport that may require specific skills and has an associated risk level.
 */
public class Sport {

    /** Name of the sport */
    private String nom;

    /** Risk level associated with the sport (e.g., low, medium, high) */
    private String niveauDeRisque;

    /** Competences required to supervise or secure this sport */
    private String competencesRequises;

    /**
     * Constructs a Sport with its name, risk level, and required competences.
     *
     * @param nom name of the sport
     * @param niveauDeRisque level of risk associated with the sport
     * @param competencesRequises competences required to participate or secure the sport
     */
    public Sport(String nom, String niveauDeRisque, String competencesRequises) {
        this.nom = nom;
        this.niveauDeRisque = niveauDeRisque;
        this.competencesRequises = competencesRequises;
    }

    /**
     * Gets the name of the sport.
     * @return the sport name
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets the name of the sport.
     * @param nom the sport name to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Gets the risk level of the sport.
     * @return the risk level
     */
    public String getNiveauDeRisque() {
        return niveauDeRisque;
    }

    /**
     * Sets the risk level of the sport.
     * @param niveauDeRisque the risk level to set
     */
    public void setNiveauDeRisque(String niveauDeRisque) {
        this.niveauDeRisque = niveauDeRisque;
    }

    /**
     * Gets the required competences for the sport.
     * @return the required competences
     */
    public String getCompetencesRequises() {
        return competencesRequises;
    }

    /**
     * Sets the required competences for the sport.
     * @param competencesRequises the required competences to set
     */
    public void setCompetencesRequises(String competencesRequises) {
        this.competencesRequises = competencesRequises;
    }
}
