package metier.graphe.algorithme;

import metier.persistence.Affectation;
import metier.persistence.Besoin;
import metier.persistence.Possede;

import java.util.*;

/**
 * * This class represents a graph structure used for managing the assignment of rescuers to needs based on their competencies.
 * It provides methods for greedy and exhaustive assignment algorithms, as well as utility methods for graph properties.
 */
public class Graphe {

    /** LIste of rescuers and their competencies */
    private ArrayList<Possede> listeSecouristeComp;
    /** List of all needs */
    private ArrayList<Besoin> listeBesoins;
    /** Adjacency matrix representing the relationships between rescuers and needs */
    private int[][] matAdj;
    /** Best combination of assignments found during exhaustive search */
    private int[] meilleureCombin;
    /** Current score of the assignment */
    private int scoreActuel;
    /** Best score found during exhaustive search */
    private int bestScore;
    /** Index mapping for unique rescuers */
    private HashMap<Integer, Long> indexSecouriste;
    /** Index mapping for needs */
    private HashMap<Integer, Besoin> indexBesoin;

    /**
     * Constructor of the Gaphe class
     * @param listeSecouristeComp list of skills and rescuer association
     * @param lalisteBesoins list of all needs
     */
    public Graphe(ArrayList<Possede> listeSecouristeComp, ArrayList<Besoin> lalisteBesoins) {
        this.listeSecouristeComp = new ArrayList<>(listeSecouristeComp);
        this.listeBesoins = new ArrayList<>(lalisteBesoins);

        // Indexation des secouristes UNIQUES
        this.indexSecouriste = new HashMap<>();
        int ind = 0;
        Set<Long> secouristesDejaVus = new HashSet<>();
        for (Possede p : this.listeSecouristeComp) {
            if (!secouristesDejaVus.contains(p.getIdSecouriste())) {
                this.indexSecouriste.put(ind, p.getIdSecouriste());
                secouristesDejaVus.add(p.getIdSecouriste());
                ind++;
            }
        }

        this.indexBesoin = new HashMap<>();
        for (int i = 0; i < this.listeBesoins.size(); i++) {
            this.indexBesoin.put(i, this.listeBesoins.get(i));
        }

        // construction matrice (carree)
        int taille = Math.max(this.indexSecouriste.size(), this.indexBesoin.size());
        this.matAdj = new int[taille][taille];

        for (int i = 0; i < this.indexSecouriste.size(); i++) {
            long idSecouriste = this.indexSecouriste.get(i);

            for (int j = 0; j < this.indexBesoin.size(); j++) {
                Besoin b = this.indexBesoin.get(j);

                // Vérifie si le secouriste a la compétence requise
                for (Possede p : this.listeSecouristeComp) {
                    if (p.getIdSecouriste() == idSecouriste &&
                            p.getIntitule().equals(b.getIntituleComp())) {
                        this.matAdj[i][j] = 1;
                    }
                }
            }
        }
    }

    // ================================================ GLOUTON ================================================

    /**
     * Glouton assignement algorithm
     * @return list of assignement object
     */
    public ArrayList<Affectation> affectationGloutonne() {
        ArrayList<Affectation> affectations = new ArrayList<>();
        boolean[] dpsAffectes = new boolean[this.indexBesoin.size()];

        for (int i = 0; i < this.indexSecouriste.size(); i++) { // parcours les secouristes
            for (int j = 0; j < this.indexBesoin.size(); j++) { // parcours chaque besoins
                if (matAdj[i][j] == 1 && !dpsAffectes[j]) {

                    // construit une affectation
                    long idSecouriste = this.indexSecouriste.get(i);
                    Besoin besoin = this.indexBesoin.get(j);

                    if (besoin != null) {
                        affectations.add(new Affectation(idSecouriste, besoin.getIntituleComp(), besoin.getIdDPS()));
                        dpsAffectes[j] = true; // on marque le dps comme affecté
                    }
                }
            }
        }
        return affectations;
    }

    // ================================================ EXHAUSTIF ================================================

    /**
     * Exhaustive assignement algorithm
     * @return list of assignement object
     */
    public ArrayList<Affectation> affectationExhaustive() {
        ArrayList<Affectation> meilleureAffectation = new ArrayList<>();
        this.bestScore = 0;
        this.scoreActuel = 0;

        // init tab de combinaison
        this.meilleureCombin = new int[this.indexSecouriste.size()];
        Arrays.fill(this.meilleureCombin, -1);

        rechercherMeilleureAffectation(0, new int[this.indexSecouriste.size()], new boolean[this.indexBesoin.size()]);

        // construction la liste d'affectations à partir de la meilleure combinaison
        for (int i = 0; i < this.meilleureCombin.length; i++) {
            if (this.meilleureCombin[i] != -1 && matAdj[i][this.meilleureCombin[i]] == 1) {
                long idSecouriste = this.indexSecouriste.get(i);
                Besoin besoin = this.indexBesoin.get(this.meilleureCombin[i]);
                meilleureAffectation.add(new Affectation(idSecouriste, besoin.getIntituleComp(), besoin.getIdDPS()));
            }
        }
        return meilleureAffectation;
    }

    /**
     * Recursive method to find the best assignment configuration using backtracking.
     * @param secouristeIndex index of the rescuer
     * @param combinaisonActuelle configuration of the actual assignement
     * @param besoinsUtilises list of needs used or not
     */
    private void rechercherMeilleureAffectation(int secouristeIndex, int[] combinaisonActuelle, boolean[] besoinsUtilises) {
        if (secouristeIndex >= this.indexSecouriste.size()) {

            // calcul le score de cette combinaison d'affectations
            int score = calculerScoreCombinaison(combinaisonActuelle);

            // maj de la meilleur affectation
            if (score > this.bestScore) {
                this.bestScore = score;
                System.arraycopy(combinaisonActuelle, 0, this.meilleureCombin, 0, combinaisonActuelle.length);
            }
            return;
        }

        // affecter le secouriste courant à chaque besoin possible
        for (int besoinIndex = 0; besoinIndex < this.indexBesoin.size(); besoinIndex++) {
            // vérifier si besoin disponible && affectation est valide
            if (!besoinsUtilises[besoinIndex] && this.matAdj[secouristeIndex][besoinIndex] == 1) {
                combinaisonActuelle[secouristeIndex] = besoinIndex;
                besoinsUtilises[besoinIndex] = true;

                // récursion sur les affectations possible pour les autres sec
                rechercherMeilleureAffectation(secouristeIndex + 1, combinaisonActuelle, besoinsUtilises);

                // backtracking
                combinaisonActuelle[secouristeIndex] = -1;
                besoinsUtilises[besoinIndex] = false;
            }
        }

        // explore la possibilité de ne pas affecter le sec
        combinaisonActuelle[secouristeIndex] = -1;
        rechercherMeilleureAffectation(secouristeIndex + 1, combinaisonActuelle, besoinsUtilises);
    }

    /**
     * Method which compute of many valid assignement the algorithm return
     * @param combinaison the assignement configuration
     * @return the number of assignement
     */
    private int calculerScoreCombinaison(int[] combinaison) {
        int score = 0;
        for (int i = 0; i < combinaison.length; i++) {
            if (combinaison[i] != -1 && matAdj[i][combinaison[i]] == 1) {
                score++;
            }
        }
        return score;
    }


    // ================================================ GETTER ET AUTRES ================================================

    /**
     * Getter of the rescuer list
     * @return list of rescuer
     */
    public ArrayList<Possede> getListeSecouriste() {
        return this.listeSecouristeComp;
    }

    /**
     * Getter of the needs list
     * @return list of needs
     */
    public ArrayList<Besoin> getListeBesoins() {
        return this.listeBesoins;
    }

    /**
     * Getter of the matrix
     * @return the adjency matrix
     */
    public int[][] getMatriceAdj() {
        return this.matAdj;
    }

    /**
     * Getter of the rescuer's index
     * @return rescuer's index
     */
    public HashMap<Integer, Long> getIndexSecouriste() {
        return this.indexSecouriste;
    }

    /**
     * Getter of the need's index
     * @return
     */
    public HashMap<Integer, Besoin> getIndexBesoin() {
        return this.indexBesoin;
    }

    /**
     * Calculates the valid assignment score by checking for unique rescuer-DPS pairs with matching competencies.
     * @param affectations List of assignments to evaluate
     * @return Number of valid assignments where rescuers have required competencies and aren't duplicated
     */
    public int calculerScore(ArrayList<Affectation> affectations) {
        Set<Long> secouristesAffectes = new HashSet<>();
        Set<Long> besoinsCouverts = new HashSet<>();
        int score = 0;

        for (Affectation a : affectations) {
            if (!secouristesAffectes.contains(a.getIdSecouriste())) { // vérifie que le secouriste n'est pas affecté
                if (!besoinsCouverts.contains(a.getIdDPS())) { // vérifie que le besoin n'est pas déjà couvert
                    boolean competenceValide = false; // vérifie que le secouriste a la comp
                    for (Possede p : this.listeSecouristeComp) {
                        if (p.getIdSecouriste() == a.getIdSecouriste()
                                && p.getIntitule().equals(a.getIntituleComp())) {
                            competenceValide = true;
                        }
                    }
                    if (competenceValide) {
                        score++;
                        secouristesAffectes.add(a.getIdSecouriste());
                        besoinsCouverts.add(a.getIdDPS());
                    }
                }
            }
        }
        return score;
    }

    /**
     * Method which verify if a graph is a DAG (acyclic and oriented)
     * @param mat adjacency matrix of the graph
     * @return true if it's a graph
     */
    public boolean estUnDAG(int[][] mat) {
        boolean dag = true;
        HashMap<Integer, Integer> state = new HashMap<>();
        /*
        Different states possible for each vertex :
        -1 --> unvisited vertex
        0 --> vertex in process
        1 --> visited vertex
         */
        for (int i = 0 ; i < mat.length ; i++) {
            state.put(i, -1);
        }
        for (int i = 0 ; i < mat.length ; i++) {
            if (dfs(mat, i, state)) {
                dag = false;
            }
        }
        return dag;
    }

    /**
     * Method which run an DFS in the graph
     * @param mat adjacency matrix of the graph
     * @param sommet a vertex of the graph
     * @param state table of states of each vertex
     * @return true if a cycle is detected, false otherwise
     */
    private boolean dfs(int[][] mat, int sommet, HashMap<Integer, Integer> state) {

        boolean hasCycle = false;
        state.replace(sommet, -1, 0);

        for (int i = 0 ; i < mat[sommet].length ; i++) {
            if (mat[sommet][i] == 1) {
                if (state.get(i) == -1) {
                    if ( dfs(mat, i, state) ) {
                        hasCycle = true;
                    }
                } else if (state.get(i) == 0) {
                    hasCycle = true;
                }
            }
        }
        state.put(sommet, 1);
        return hasCycle;
    }
}
