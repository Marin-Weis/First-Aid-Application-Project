package metier.service;

import metier.persistence.DPS;
import metier.persistence.Disponibilite;

import java.util.LinkedHashSet;

/**
 *
 */
public class DispoMngt {

    private LinkedHashSet<Disponibilite> listeDispo;

    public DispoMngt(){

        listeDispo = new LinkedHashSet<>();
    }

    public LinkedHashSet<Disponibilite> nouvDispo(Disponibilite dispo){
        listeDispo.add(dispo);
        return listeDispo;
    }

    public LinkedHashSet<Disponibilite> enleverDispo(Disponibilite dispo){
        listeDispo.remove(dispo);
        return listeDispo;
    }

}
