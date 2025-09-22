package metier.persistence;


/**
 * Represents an administrator in the system.
 * An {@code Admin} has a unique identifier, name, email, and password.
 * This class overrides {@code equals} and {@code hashCode} to allow comparison and use in collections.
 */
public class Admin {

    /** Unique ID of the admin (can be null if not persisted yet) */
    private Long id;

    /** Last name of the admin */
    private String nom;

    /** First name of the admin */
    private String prenom;

    /** Email address of the admin */
    private String email;

    /** Hashed password of the admin */
    private String motDePasse;

    /**
     * Constructs a new Admin instance with the given information.
     *
     * @param id          the unique ID of the admin
     * @param nom         the last name of the admin
     * @param prenom      the first name of the admin
     * @param email       the email address of the admin
     * @param motDePasse  the hashed password of the admin
     */
    public Admin(Long id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters et setters

    /**
     * Returns the ID of the admin.
     * @return the ID
     */
    public Long getId() { return id; }

    /**
     * Returns the last name of the admin.
     * @return the last name
     */
    public String getNom() { return nom; }

    /**
     * Returns the first name of the admin.
     * @return the first name
     */
    public String getPrenom() { return prenom; }

    /**
     * Returns the email of the admin.
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Returns the password of the admin.
     * @return the hashed password
     */
    public String getMotDePasse() { return motDePasse; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }


    /**
     * Computes a hash code for this admin based on all its fields.
     * This is useful for use in collections such as HashMap or HashSet.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prenom != null ? prenom.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (motDePasse != null ? motDePasse.hashCode() : 0);
        return result;
    }


    /**
     * Compares this admin to another object for equality.
     * Two admins are considered equal if all their fields are equal.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // même référence
        if (obj == null || getClass() != obj.getClass()) return false; // null ou mauvaise classe

        Admin other = (Admin) obj;

        if (id != null ? !id.equals(other.id) : other.id != null) return false;
        if (nom != null ? !nom.equals(other.nom) : other.nom != null) return false;
        if (prenom != null ? !prenom.equals(other.prenom) : other.prenom != null) return false;
        if (email != null ? !email.equals(other.email) : other.email != null) return false;
        return motDePasse != null ? motDePasse.equals(other.motDePasse) : other.motDePasse == null;
    }
}
