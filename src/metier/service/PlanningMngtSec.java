package metier.service;

import metier.graphe.model.EventData;

import java.time.LocalDate;
import java.util.*;

public class PlanningMngtSec {

    private Map<LocalDate, List<EventData>> listEvntSec;

    public PlanningMngtSec() {
        this.listEvntSec = new HashMap<>();
    }

    public void addEvent (LocalDate day, EventData event) {
        // computeIfAbsent permet de créer une nouvelle liste si la clé n'existe pas
        // ex : si day n'existe pas dans listEvntSec, une nouvelle liste sera créée
        this.listEvntSec.computeIfAbsent(day, k -> new ArrayList<>()).add(event);
    }

    public List<EventData> getEventDay(LocalDate day) {
        // renvoie la liste des événements pour un jour donné
        return this.listEvntSec.getOrDefault(day, new ArrayList<>());
    }

    public Map<LocalDate, List<EventData>> getListEvntSec() {
        // renvoie une vue non modifiable de la map des événements
        // Collections.unmodifiableMap permet de protéger la map contre les modifications
        return Collections.unmodifiableMap(this.listEvntSec);
    }

    public void clearEvents() {
        // vide la map des événements
        this.listEvntSec.clear();
    }
}

