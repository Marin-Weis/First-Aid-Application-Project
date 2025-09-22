package metier.service;

import metier.persistence.Secouriste;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Administrator class for secourist management
 */
public class SecouristeMngt {

    private LinkedHashSet<Secouriste> listeSecouriste;

    public SecouristeMngt() {
        listeSecouriste = new LinkedHashSet<>();
    }

    public void ajouterSecouriste(Secouriste sec) throws IllegalArgumentException {

        // Check if the secouriste parameter is null
        if (sec == null) {
            throw new IllegalArgumentException("Erreur ajouterSecouriste() : le secouriste en paramètre est à null!");
        } else {

            // Else we add the rescuer to the list
            listeSecouriste.add(sec);
        }
    }


    public void retirerSecouriste(Secouriste sec) throws IllegalArgumentException {
        // Check if the secouriste parameter is null
        if (sec == null) {
            throw new IllegalArgumentException("Erreur retirerSecouriste() : le secouriste en paramètre est à null!");
        }

        // remove the rescuer from the arraylist if exists
        boolean retirer = false;
        for (Secouriste s : listeSecouriste) {
            if (s.getId() == sec.getId()) {
                listeSecouriste.remove(s);
                retirer = true;
            }
        }

        // If the rescuer wasn't remove ---> he isn't in the arraylist
        if ( !retirer ) {
            throw new IllegalArgumentException("Erreur retirerSecouriste() : le secouriste en paramètre n'existe pas!");
        }
    }

}
