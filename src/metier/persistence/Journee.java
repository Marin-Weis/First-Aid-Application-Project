package metier.persistence;

import java.util.Objects;

public class Journee {
    private int jour;
    private int mois;
    private int annee;

    public Journee(int jour, int mois, int annee) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    public int getJour() {
        return this.jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public int getMois() {
        return this.mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return this.annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Journee journee = (Journee) o;
        return jour == journee.jour && mois == journee.mois && annee == journee.annee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jour, mois, annee);
    }

}

