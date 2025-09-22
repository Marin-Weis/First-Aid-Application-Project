package metier.persistence;

import java.util.ArrayList;
import java.util.List;

public class Secouriste {
    private long id;
    private String nom;
    private String prenom;
    private String email;
    private ArrayList<Disponibilite> disponibilites;
    private String motDePasse;
    private String telephone;
    private List<Competences> competences;

    public Secouriste(long id, String nom, String prenom, String email, String motDePasse, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.disponibilites = new ArrayList<>();
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Competences> getCompetences() {
        return competences;
    }

    public void setCompetences(List<Competences> competences) {
        this.competences = competences;
    }

    public void ajouterDispo(Disponibilite laDispo) {
        if (laDispo == null) {
            throw new IllegalArgumentException("Erreur ajouterDispo() : le paramètre fournis est à null !");
        } else {
            this.disponibilites.add(laDispo);
        }
    }

    public void retirerDispo(Disponibilite laDispo) {
        if (laDispo == null) {
            throw new IllegalArgumentException("Erreur ajouterDispo() : le paramètre fournis est à null !");
        } else if ( ! this.disponibilites.contains(laDispo)) {
            throw new IllegalArgumentException("Erreur retirerDispo() : la disponibilité à supprimer n'est pas présente dans la liste des disponibilités !");
        } else {
            this.disponibilites.remove(laDispo);
        }
    }

    public ArrayList<Disponibilite> getDispo() {
        return this.disponibilites;
    }

}
