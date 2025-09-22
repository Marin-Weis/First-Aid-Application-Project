package metier.graphe.algorithme;

import metier.persistence.Competences;
import metier.persistence.DPS;
import metier.persistence.Possede;
import metier.persistence.Secouriste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Affectation {

    private HashMap<String, String>  competences;
    /*
    public Affectation(competences) {

    }
    */
    // public void remplissage




    public HashMap<String, String> Secouristes_Exhaustif(HashMap<String, String> dico){

        int cout;
        String[] choix;

        return null;
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