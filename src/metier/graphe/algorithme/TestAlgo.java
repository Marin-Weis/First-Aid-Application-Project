package metier.graphe.algorithme;

import metier.graphe.algorithme.Graphe;
import metier.persistence.Affectation;
import metier.persistence.Besoin;
import metier.persistence.Possede;

import java.util.*;

public class TestAlgo {
    
    public static void main(String[] args) {
        testComplet();
    }
    
    public static void testComplet() {
        String[] competences = {"CO", "CP", "CE", "PBC", "PBF", "PSE1", "PSE2", "SSA", "VPSP"};
        Random rand = new Random();

        // 5 scénarios de test
        for (int scenario = 1; scenario <= 5; scenario++) {
            System.out.println("\n==== SCENARIO " + scenario + " ====");

            ArrayList<Possede> possedes = new ArrayList<>();
            ArrayList<Besoin> besoins = new ArrayList<>();

            int nbSecouristes, nbBesoins;

            switch(scenario) {
                case 1: // plus de besoins que de secouristes critique
                    nbSecouristes = 5 + rand.nextInt(3); // 5-7 secouristes
                    nbBesoins = 8 + rand.nextInt(3);     // 8-10 besoins
                    break;

                case 2: // plus de secouristes que de besoins
                    nbSecouristes = 8 + rand.nextInt(3); // 8-10 secouristes
                    nbBesoins = 5 + rand.nextInt(3);     // 5-7 besoins
                    break;

                case 3: // nombre égal mais compétences rares
                    nbSecouristes = 7 + rand.nextInt(2); // 7-8 secouristes
                    nbBesoins = nbSecouristes;
                    break;

                case 4: // gros déséquilibre avec 1 secouriste polyvalent
                    nbSecouristes = 6;
                    nbBesoins = 9;
                    break;

                case 5: // cas aléatoire complexe
                    nbSecouristes = 7 + rand.nextInt(4); // 7-10 secouristes
                    nbBesoins = 7 + rand.nextInt(4);     // 7-10 besoins
                    break;

                default:
                    nbSecouristes = 8;
                    nbBesoins = 8;
            }

            switch(scenario) {
                case 1: // cas critique avec besoins prioritaires
                    // créer 3 besoins critique (PSE2, CE, CP)
                    besoins.add(new Besoin(1, "PSE2", 1001));
                    besoins.add(new Besoin(1, "CE", 1002));
                    besoins.add(new Besoin(1, "CP", 1003));

                    // ajoute besoins aléatoires
                    for (int i = 4; i <= nbBesoins; i++) {
                        String comp = competences[rand.nextInt(competences.length)];
                        besoins.add(new Besoin(1, comp, 1000 + i));
                    }

                    // Créer secouristes spécialisés ---> 1 comp
                    possedes.add(new Possede("PSE2", 1)); // Seul PSE2
                    possedes.add(new Possede("CE", 2));   // Seul CE
                    possedes.add(new Possede("CP", 3));   // Seul CP

                    // ajoute des secouristes avec compétences aléatoires
                    for (long i = 4; i <= nbSecouristes; i++) {
                        String comp = competences[rand.nextInt(competences.length)];
                        possedes.add(new Possede(comp, i));
                    }
                    break;

                case 2: // Situation avec chevauchements
                    // Besoins aléatoires
                    for (int i = 1; i <= nbBesoins; i++) {
                        String comp = competences[rand.nextInt(competences.length)];
                        besoins.add(new Besoin(1, comp, 2000 + i));
                    }

                    // Secouristes avec 2-4 compétences
                    for (long i = 1; i <= nbSecouristes; i++) {
                        int nbComp = 2 + rand.nextInt(3); // 2-4 compétences
                        Set<String> comps = new HashSet<>();
                        while (comps.size() < nbComp) {
                            comps.add(competences[rand.nextInt(competences.length)]);
                        }
                        for (String comp : comps) {
                            possedes.add(new Possede(comp, i));
                        }
                    }
                    break;

                case 3: // Compétences rares
                    // Besoins concentrés sur quelques compétences
                    String[] compRares = {"CE", "CP", "PBC"};
                    for (int i = 1; i <= nbBesoins; i++) {
                        String comp = compRares[rand.nextInt(compRares.length)];
                        besoins.add(new Besoin(1, comp, 3000 + i));
                    }

                    // Peu de secouristes ont ces compétences
                    for (long i = 1; i <= nbSecouristes; i++) {
                        // 30% de chance compétence rare
                        if (rand.nextDouble() < 0.3) {
                            String comp = compRares[rand.nextInt(compRares.length)];
                            possedes.add(new Possede(comp, i));
                        }
                        // 70% de chance compétence commune
                        else {
                            String[] compCommunes = {"PSE1", "PSE2", "SSA"};
                            String comp = compCommunes[rand.nextInt(compCommunes.length)];
                            possedes.add(new Possede(comp, i));
                        }
                    }
                    break;

                case 4: // sec polyvalent avec besoins variés
                    besoins.add(new Besoin(1, "PSE2", 4001));
                    besoins.add(new Besoin(1, "CE", 4002));
                    besoins.add(new Besoin(1, "CP", 4003));
                    besoins.add(new Besoin(1, "PBC", 4004));
                    besoins.add(new Besoin(1, "VPSP", 4005));
                    besoins.add(new Besoin(1, "SSA", 4006));
                    besoins.add(new Besoin(1, "PSE1", 4007));
                    besoins.add(new Besoin(1, "CO", 4008));
                    besoins.add(new Besoin(1, "PBF", 4009));

                    // sec polyvalent
                    for (String comp : competences) {
                        possedes.add(new Possede(comp, 1));
                    }

                    // autres sec avec 1 seule compétence aléatoire
                    for (long i = 2; i <= nbSecouristes; i++) {
                        String comp = competences[rand.nextInt(competences.length)];
                        possedes.add(new Possede(comp, i));
                    }
                    break;

                default: // cas aléatoire complexe pour le gloutin
                    // besoins aléatoires
                    for (int i = 1; i <= nbBesoins; i++) {
                        String comp = competences[rand.nextInt(competences.length)];
                        besoins.add(new Besoin(1, comp, 5000 + i));
                    }

                    // secouristes avec 1 à 3 comp aléatoires
                    for (long i = 1; i <= nbSecouristes; i++) {
                        int nbComp = 1 + rand.nextInt(3); // 1-3 compétences
                        Set<String> comps = new HashSet<>();
                        while (comps.size() < nbComp) {
                            comps.add(competences[rand.nextInt(competences.length)]);
                        }
                        for (String comp : comps) {
                            possedes.add(new Possede(comp, i));
                        }
                    }
            }

            // affichage configuration
            System.out.println("Configuration : " + nbSecouristes + " secouristes, " + nbBesoins + " besoins");
            System.out.println("Compétences disponibles : " + Arrays.toString(competences));

            Graphe graphe = new Graphe(possedes, besoins);

            /*
            // afficher matrice (debug)
            if (nbSecouristes <= 10 && nbBesoins <= 10) {
                System.out.println("\nMatrice d'adjacence :");
                afficherMatrice(graphe.getMatriceAdj(), graphe.getIndexSecouriste(), graphe.getIndexBesoin());
            }
            */

            // test glouton
            long startGlouton = System.nanoTime();
            ArrayList<Affectation> affG = graphe.affectationGloutonne();
            long endGlouton = System.nanoTime();

            // test exhaustif
            ArrayList<Affectation> affE = new ArrayList<>();
            long startExh = 0;
            long endExh = 0;
            if (nbSecouristes <= 10 && nbBesoins <= 10) {
                startExh = System.nanoTime();
                affE = graphe.affectationExhaustive();
                endExh = System.nanoTime();
            }

            // résultats
            System.out.println("\nRésultats :");
            System.out.println("Glouton   : " + graphe.calculerScore(affG) + " affectations | Durée : " + (endGlouton - startGlouton) / 1000000 + " ms");

            if (!affE.isEmpty()) {
                System.out.println("Exhaustif : " + graphe.calculerScore(affE) + " affectations | Durée : " + (endExh - startExh) / 1000000 + " ms");

                if (graphe.calculerScore(affG) < graphe.calculerScore(affE)) {
                    System.out.println("--> Le glouton a échoué à trouver la solution optimale !");
                    afficherDifferences(affG, affE);
                } else if (graphe.calculerScore(affG) > graphe.calculerScore(affE)) {
                    System.out.println("--> ERREUR : le glouton a fait mieux que l'exhaustif !");
                } else {
                    System.out.println("--> Le glouton a trouvé la solution optimale");
                }
            } else {
                System.out.println("Exhaustif : non exécuté (trop grand)");
            }
        }
    }

    // afficher matrice
    private static void afficherMatrice(int[][] matrice, HashMap<Integer, Long> secouristes, HashMap<Integer, Besoin> besoins) {
        System.out.print("          ");
        for (int j = 0; j < besoins.size(); j++) {
            System.out.print(String.format("%-8s", besoins.get(j).getIntituleComp()));
        }
        System.out.println();

        for (int i = 0; i < secouristes.size(); i++) {
            System.out.print(String.format("Sec%-6d", secouristes.get(i)));
            for (int j = 0; j < besoins.size(); j++) {
                System.out.print(String.format("%-8d", matrice[i][j]));
            }
            System.out.println();
        }
    }

    /***
     * Affiche les différences entre les 2 algos
     * @param glouton liste d'affectation du glouton
     * @param exhaustif liste d'affectation de l'exhaustif
     */
    private static void afficherDifferences(ArrayList<Affectation> glouton, ArrayList<Affectation> exhaustif) {
        Set<String> gloutonSet = new HashSet<>();
        for (Affectation a : glouton) {
            gloutonSet.add(a.getIdSecouriste() + "->" + a.getIdDPS() + "(" + a.getIntituleComp() + ")");
        }

        System.out.println("\nAffectations manquantes dans le glouton :");
        for (Affectation a : exhaustif) {
            String affStr = a.getIdSecouriste() + "->" + a.getIdDPS() + "(" + a.getIntituleComp() + ")";
            if (!gloutonSet.contains(affStr)) {
                System.out.println(" - " + affStr);
            }
        }
    }
}
