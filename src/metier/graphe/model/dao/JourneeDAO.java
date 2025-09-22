package metier.graphe.model.dao;

import metier.persistence.Journee;

import java.sql.*;
import java.util.*;

/**
 * DAO class for managing persistence operations for the {@link Journee} entity.
 * This class allows interaction with the database for CRUD operations.
 */
public class JourneeDAO extends DAO<Journee> {

    /**
     * Inserts a new {@link Journee} into the database.
     *
     * @param j the {@link Journee} to create
     * @return number of rows affected (1 if success, -1 if failure)
     */
    @Override
    public int create(Journee j) {
        String query = "INSERT INTO Journee (jour, mois, annee) VALUES (?, ?, ?)";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, j.getJour());
            ps.setInt(2, j.getMois());
            ps.setInt(3, j.getAnnee());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates an existing {@link Journee}. Not useful if primary key fields are unchanged.
     *
     * @param j the {@link Journee} to update
     * @return number of rows affected or -1 if an error occurs
     */
    @Override
    public int update(Journee j) {
        String query = "UPDATE Journee SET jour = ?, mois = ?, annee = ? WHERE jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, j.getJour());
            ps.setInt(2, j.getMois());
            ps.setInt(3, j.getAnnee());
            ps.setInt(4, j.getJour());
            ps.setInt(5, j.getMois());
            ps.setInt(6, j.getAnnee());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Deletes a {@link Journee} record from the database.
     *
     * @param j the {@link Journee} to delete
     * @return number of rows affected or -1 if an error occurs
     */
    @Override
    public int delete(Journee j) {
        String query = "DELETE FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, j.getJour());
            ps.setInt(2, j.getMois());
            ps.setInt(3, j.getAnnee());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves all {@link Journee} entries from the database.
     *
     * @return a list of all {@link Journee} records
     */
    @Override
    public List<Journee> findAll() {
        List<Journee> liste = new LinkedList<>();
        String query = "SELECT * FROM Journee";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Journee(
                        rs.getInt("jour"),
                        rs.getInt("mois"),
                        rs.getInt("annee")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

    /**
     * Unsupported operation for {@link Journee} as it lacks a unique identifier.
     *
     * @param id not used
     * @return always returns null
     */
    @Override
    public Journee findByID(Long id) {
        return null;
    }

    /**
     * Retrieves a {@link Journee} entry based on its day, month, and year.
     *
     * @param jour  the day
     * @param mois  the month
     * @param annee the year
     * @return the corresponding {@link Journee} or null if not found
     */
    public Journee findByDate(int jour, int mois, int annee) {
        String query = "SELECT * FROM Journee WHERE jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, jour);
            ps.setInt(2, mois);
            ps.setInt(3, annee);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Journee(
                            rs.getInt("jour"),
                            rs.getInt("mois"),
                            rs.getInt("annee")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
