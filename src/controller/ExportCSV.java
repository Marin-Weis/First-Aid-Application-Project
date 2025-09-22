package controller;

import metier.persistence.Secouriste;
import metier.persistence.Competences;
import metier.graphe.model.dao.PossedeDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class to export a list of Secouriste objects to a CSV file.
 * It retrieves associated competencies using the PossedeDAO and formats the output as comma-separated values.
 */
public class ExportCSV {

    /**
     * Exports the given list of Secouriste objects into a CSV file at the specified file path.
     * Each row contains secouriste information and their associated competencies.
     *
     * CSV format:
     * ID,Nom,Prénom,Email,Téléphone,Compétences
     *
     * @param secouristes     The list of secouristes to export.
     * @param cheminFichier   The file path where the CSV should be written.
     */
    public static void exportSecouristes(List<Secouriste> secouristes, String cheminFichier) {
        try {
            FileWriter writer = new FileWriter(cheminFichier);

            // Écriture de l'en-tête du fichier CSV
            writer.write("ID,Nom,Prénom,Email,Téléphone,Compétences\n");

            // DAO utilisé pour récupérer les compétences de chaque secouriste
            PossedeDAO possedeDAO = new PossedeDAO();

            // Pour chaque secouriste, écrire une ligne dans le fichier
            for (Secouriste s : secouristes) {
                // Récupération des compétences associées à ce secouriste
                List<Competences> competences = possedeDAO.findCompetencesBySecouriste(s.getId());

                // Construction de la chaîne des compétences séparées par des ;
                String competencesStr = "";
                for (int i = 0; i < competences.size(); i++) {
                    competencesStr += competences.get(i).getIntitule();
                    if (i < competences.size() - 1) {
                        competencesStr += ";";
                    }
                }

                // Écriture d'une ligne CSV pour ce secouriste
                writer.write(
                        s.getId() + "," +
                                s.getNom() + "," +
                                s.getPrenom() + "," +
                                s.getEmail() + "," +
                                s.getTelephone() + "," +
                                competencesStr + "\n"
                );
            }

            writer.close();
            System.out.println("Export terminé : " + cheminFichier);

        } catch (IOException e) {
            System.out.println("Erreur lors de l'export CSV : " + e.getMessage());
        }
    }
}
