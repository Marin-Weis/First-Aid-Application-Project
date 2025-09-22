package metier.service;

import metier.persistence.DPS;
import metier.graphe.model.dao.DPSDAO;

import java.util.LinkedHashSet;
import java.sql.Time;
import java.util.List;

public class DPSMngt {

    private LinkedHashSet<DPS> listeDPS = new LinkedHashSet<>();
    private DPSDAO dao = new DPSDAO();

    public DPSMngt() {
        // Charger les DPS existants en base (optionnel)
        List<DPS> dpsEnBase = dao.findAll();
        listeDPS.addAll(dpsEnBase);
    }

    public boolean ajouterDPS(DPS dispositif) {
        int resultat = dao.create(dispositif);
        if (resultat > 0) {
            listeDPS.add(dispositif);
            return true;
        }
        return false;
    }

    public boolean supprimerDPS(DPS dispositif) {
        int resultat = dao.delete(dispositif);
        if (resultat > 0) {
            listeDPS.remove(dispositif);
            return true;
        }
        return false;
    }

    public void modifierHeureDebut(DPS dispositif, Time heure) {
        dispositif.setHeureDebut(heure);
        dao.update(dispositif);
    }

    public void modifierHeureFin(DPS dispositif, Time heure) {
        dispositif.setHeureFin(heure);
        dao.update(dispositif);
    }

    public LinkedHashSet<DPS> getListeDPS() {
        return listeDPS;
    }
}
