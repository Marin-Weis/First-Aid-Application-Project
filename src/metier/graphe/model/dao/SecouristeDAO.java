package metier.graphe.model.dao;

import metier.persistence.Secouriste;

import java.sql.*;
import java.util.*;

/**
 * DAO class responsible for handling all database operations related to {@link Secouriste}.
 * Provides CRUD operations and custom queries using JDBC.
 */
public class SecouristeDAO extends DAO<Secouriste> {

    /**
     * Inserts a new {@link Secouriste} into the database.
     *
     * @param s The secouriste to insert.
     * @return The number of rows affected, or -1 if an error occurred.
     */
    @Override
    public int create(Secouriste s) {
        String query = "INSERT INTO Secouriste (nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, s.getNom());
            ps.setString(2, s.getPrenom());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getMotDePasse());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates the details of an existing {@link Secouriste}.
     *
     * @param s The secouriste object with updated information.
     * @return The number of rows affected, or -1 if an error occurred.
     */
    @Override
    public int update(Secouriste s) {
        String query = "UPDATE Secouriste SET nom = ?, prenom = ?, email = ?, telephone = ? WHERE id = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, s.getNom());
            ps.setString(2, s.getPrenom());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getTelephone());
            ps.setLong(5, s.getId());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Deletes a {@link Secouriste} from the database.
     *
     * @param s The secouriste to delete.
     * @return The number of rows affected, or -1 if an error occurred.
     */
    @Override
    public int delete(Secouriste s) {
        String query = "DELETE FROM Secouriste WHERE id = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, s.getId());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves all {@link Secouriste} entries from the database.
     *
     * @return A list of all secouristes.
     */
    @Override
    public List<Secouriste> findAll() {
        List<Secouriste> liste = new LinkedList<>();
        String query = "SELECT * FROM Secouriste";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Secouriste(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getString("telephone")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

    /**
     * Finds a {@link Secouriste} by its ID.
     *
     * @param id The ID of the secouriste.
     * @return The corresponding secouriste object, or null if not found.
     */
    @Override
    public Secouriste findByID(Long id) {
        String query = "SELECT * FROM Secouriste WHERE id = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                            rs.getLong("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Finds a {@link Secouriste} by their email address.
     *
     * @param email The email of the secouriste.
     * @return The corresponding secouriste object, or null if not found.
     */
    public Secouriste findByEmail(String email) {
        String query = "SELECT * FROM Secouriste WHERE email = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                            rs.getLong("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("motDePasse"),
                            rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
