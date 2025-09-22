package metier.graphe.model.dao;

import metier.persistence.DPS;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for the {@link DPS} entity.
 * This class handles database operations such as create, update, delete, find by ID, and find all.
 */
public class DPSDAO extends DAO<DPS> {

    /**
     * Inserts a new DPS record into the database.
     *
     * @param dps The DPS to insert.
     * @return 1 if successful, -1 otherwise.
     */
    @Override
    public int create(DPS dps) {
        String query = "INSERT INTO DPS (label, date, heure_debut, heure_fin, sportAssocie, codeSite, lieu, description, couleur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dps.getLabel());
            ps.setDate(2, dps.getDate());
            ps.setTime(3, dps.getHeureDebut());
            ps.setTime(4, dps.getHeureFin());
            ps.setString(5, dps.getSportAssocie());
            ps.setInt(6, dps.getCodeSite());
            ps.setString(7, dps.getLieu());
            ps.setString(8, dps.getDescription());
            ps.setString(9, dps.getCouleur());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Updates an existing DPS record.
     *
     * @param dps The DPS to update.
     * @return 1 if successful, -1 otherwise.
     */
    @Override
    public int update(DPS dps) {
        String query = "UPDATE DPS SET label = ?, date = ?, heure_debut = ?, heure_fin = ?, sportAssocie = ?, codeSite = ?, lieu = ?, description = ?, couleur = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dps.getLabel());
            ps.setDate(2, dps.getDate());
            ps.setTime(3, dps.getHeureDebut());
            ps.setTime(4, dps.getHeureFin());
            ps.setString(5, dps.getSportAssocie());
            ps.setInt(6, dps.getCodeSite());
            ps.setString(7, dps.getLieu());
            ps.setString(8, dps.getDescription());
            ps.setString(9, dps.getCouleur());
            ps.setLong(10, dps.getId());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Deletes a DPS from the database.
     *
     * @param dps The DPS to delete.
     * @return 1 if successful, -1 otherwise.
     */
    @Override
    public int delete(DPS dps) {
        String query = "DELETE FROM DPS WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, dps.getId());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Finds a DPS by its ID.
     *
     * @param id The ID of the DPS.
     * @return The corresponding DPS or null if not found.
     */
    @Override
    public DPS findByID(Long id) {
        String query = "SELECT * FROM DPS WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DPS dps = new DPS(
                        rs.getLong("id"),
                        rs.getString("label"),
                        rs.getDate("date"),
                        rs.getTime("heure_debut"),
                        rs.getTime("heure_fin"),
                        rs.getString("sportAssocie"),
                        rs.getInt("codeSite"),
                        rs.getString("lieu"),
                        rs.getString("description")
                );
                dps.setCouleur(rs.getString("couleur"));
                return dps;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all DPS records from the database.
     *
     * @return A list of all DPS.
     */
    @Override
    public List<DPS> findAll() {
        List<DPS> list = new ArrayList<>();
        String query = "SELECT * FROM DPS";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DPS dps = new DPS(
                        rs.getLong("id"),
                        rs.getString("label"),
                        rs.getDate("date"),
                        rs.getTime("heure_debut"),
                        rs.getTime("heure_fin"),
                        rs.getString("sportAssocie"),
                        rs.getInt("codeSite"),
                        rs.getString("lieu"),
                        rs.getString("description")
                );
                dps.setCouleur(rs.getString("couleur"));
                list.add(dps);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Finds all DPS entries assigned to a specific secouriste.
     *
     * @param idSecouriste The ID of the secouriste.
     * @return A list of DPS associated with the secouriste.
     */
    public List<DPS> findBySecouriste(long idSecouriste) {
        List<DPS> list = new ArrayList<>();
        String query = "SELECT DPS.* FROM DPS " +
                "JOIN Affectation ON DPS.id = Affectation.idDPS " +
                "WHERE Affectation.idSecouriste = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, idSecouriste);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DPS dps = new DPS(
                        rs.getLong("id"),
                        rs.getString("label"),
                        rs.getDate("date"),
                        rs.getTime("heure_debut"),
                        rs.getTime("heure_fin"),
                        rs.getString("sportAssocie"),
                        rs.getInt("codeSite"),
                        rs.getString("lieu"),
                        rs.getString("description")
                );
                dps.setCouleur(rs.getString("couleur"));
                list.add(dps);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
