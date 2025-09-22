package metier.graphe.model.dao;

import metier.persistence.Competences;
import metier.persistence.Possede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * DAO class for managing the association between {@link metier.persistence.Secouriste} and {@link Competences}.
 * Handles the operations related to the Possede table.
 */
public class PossedeDAO extends DAO<Possede> {

    /**
     * Inserts a new association between a Secouriste and a Competence.
     *
     * @param possede The association object to be inserted.
     * @return Number of rows affected (1 if success, -1 if error).
     */
    @Override
    public int create(Possede possede) {
        String query = "INSERT INTO Possede (idSecouriste, intitule) VALUES (?, ?)";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, possede.getIdSecouriste());
            ps.setString(2, possede.getIntitule());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Deletes all competence associations for a given Secouriste.
     *
     * @param idSecouriste The ID of the Secouriste.
     * @return Number of rows affected (>= 0 if success, -1 if error).
     */
    public int deleteAllBySecouriste(long idSecouriste) {
        String query = "DELETE FROM Possede WHERE idSecouriste = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, idSecouriste);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves the list of competences held by a specific Secouriste.
     *
     * @param secouristeId The ID of the Secouriste.
     * @return A list of {@link Competences} held by the secouriste.
     */
    public List<Competences> findCompetencesBySecouriste(long secouristeId) {
        List<Competences> competences = new ArrayList<>();
        String query = "SELECT intitule FROM Possede WHERE idSecouriste = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, secouristeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    competences.add(new Competences(rs.getString("intitule")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return competences;
    }

    /**
     * Unsupported operation for this association table.
     *
     * @param possede The element to update.
     * @return Always -1 (not applicable).
     */
    @Override
    public int update(Possede possede) {
        return -1; // Pas d'update logique pour une table d'association simple
    }

    /**
     * Deletes a specific association between a Secouriste and a Competence.
     *
     * @param possede The association to delete.
     * @return Number of rows affected (1 if success, -1 if error).
     */
    @Override
    public int delete(Possede possede) {
        String query = "DELETE FROM Possede WHERE idSecouriste = ? AND intitule = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, possede.getIdSecouriste());
            ps.setString(2, possede.getIntitule());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Not applicable: no unique identifier for this association.
     *
     * @param id Ignored.
     * @return Always null.
     */
    @Override
    public Possede findByID(Long id) {
        return null;
    }

    /**
     * Not implemented: use specific methods instead (e.g. findCompetencesBySecouriste).
     *
     * @return Always null.
     */
    @Override
    public List<Possede> findAll() {
        return null;
    }


}