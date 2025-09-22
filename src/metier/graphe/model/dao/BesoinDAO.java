package metier.graphe.model.dao;

import metier.persistence.Besoin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * DAO class for managing {@link Besoin} objects (resource needs for a DPS).
 * This DAO allows insertion, deletion, and retrieval of needs (competences and required quantity).
 */
public class BesoinDAO extends DAO<Besoin> {

    /**
     * Inserts a new {@link Besoin} into the database.
     *
     * @param besoin The {@link Besoin} object to insert.
     * @return 1 if the insertion was successful, -1 otherwise.
     */
    @Override
    public int create(Besoin besoin) {
        String query = "INSERT INTO Besoin (nombreSecouriste, intituleComp, idDPS) VALUES (?, ?, ?)";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, besoin.getNombreSecouriste());
            ps.setString(2, besoin.getIntituleComp());
            ps.setInt(3, (int) besoin.getIdDPS());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Update is not supported for {@link Besoin}, since the key is composite and immutable.
     *
     * @param besoin The {@link Besoin} object to update.
     * @return -1 to indicate operation not supported.
     */
    @Override
    public int update(Besoin besoin) {
        // Non applicable car on ne modifie pas un besoin existant (clé multiple)
        return -1;
    }

    /**
     * Deletes a {@link Besoin} based on its competence and DPS ID.
     *
     * @param besoin The {@link Besoin} object to delete.
     * @return 1 if deletion was successful, -1 otherwise.
     */
    @Override
    public int delete(Besoin besoin) {
        String query = "DELETE FROM Besoin WHERE intituleComp = ? AND idDPS = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, besoin.getIntituleComp());
            ps.setInt(2, (int) besoin.getIdDPS());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves all {@link Besoin} entries from the database.
     *
     * @return A list of all needs.
     */
    @Override
    public List<Besoin> findAll() {
        List<Besoin> liste = new LinkedList<>();
        String query = "SELECT * FROM Besoin";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Besoin b = new Besoin(
                        rs.getInt("nombreSecouriste"),
                        rs.getString("intituleComp"),
                        rs.getInt("idDPS")
                );
                liste.add(b);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

    /**
     * This method is not applicable as {@link Besoin} does not have a unique identifier.
     *
     * @param id Not used.
     * @return Always null.
     */
    @Override
    public Besoin findByID(Long id) {
        // Non applicable car pas d'identifiant unique
        return null;
    }

    /**
     * Retrieves all {@link Besoin} entries associated with a specific DPS.
     *
     * @param idDPS The ID of the DPS.
     * @return A list of needs for the given DPS.
     */
    public List<Besoin> findAllByDPS(int idDPS) {
        List<Besoin> liste = new LinkedList<>();
        String query = "SELECT * FROM Besoin WHERE idDPS = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idDPS);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Besoin b = new Besoin(
                            rs.getInt("nombreSecouriste"),
                            rs.getString("intituleComp"),
                            rs.getInt("idDPS")
                    );
                    liste.add(b);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

}
