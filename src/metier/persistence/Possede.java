package metier.persistence;

/**
 * Class which connect secouriste's and compentence's classes
 */
public class Possede {
    private String intituleComp;
    private long idSecouriste;


    public Possede(String intituleComp, long id) {
        this.intituleComp = intituleComp;
        this.idSecouriste = id;
    }

    public String getIntitule() {
        return this.intituleComp;
    }

    public void setIntitule(String intituleComp) {
        this.intituleComp = intituleComp;
    }

    public long getIdSecouriste() {
        return this.idSecouriste;
    }

    public void setIdSecouriste(int idSecouriste) {
        this.idSecouriste = idSecouriste;
    }
}