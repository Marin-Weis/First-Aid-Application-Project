package metier.graphe.model.dao;

import metier.persistence.Disponibilite;
import metier.persistence.Journee;

import java.sql.*;
import java.util.*;

/**
 * DAO class for handling operations related to the {@link Disponibilite} entity.
 * This class provides methods to create, delete, and query availability records
 * for rescuers in the database.
 */
public class DispoDAO extends DAO<Disponibilite> {

    /**
     * Inserts a new availability into the database if it does not already exist.
     *
     * @param d The {@link Disponibilite} to create
     * @return 1 if the insert was successful, 0 if it already existed, -1 if an error occurred
     */
    @Override
    public int create(Disponibilite d) {
        if (exists((int) d.getIdSecouriste(), d.getDateDispo())) {
            // La disponibilité existe déjà, on ne fait rien ou on retourne 0
            return 0;
        }
        String query = "INSERT INTO Disponibilite (idSecouriste, jour, mois, annee) VALUES (?, ?, ?, ?)";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, (int) d.getIdSecouriste());
            ps.setInt(2, d.getDateDispo().getJour());
            ps.setInt(3, d.getDateDispo().getMois());
            ps.setInt(4, d.getDateDispo().getAnnee());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Checks if a specific availability record already exists in the database.
     *
     * @param idSecouriste The rescuer's ID
     * @param journee The date to check
     * @return true if the availability already exists, false otherwise
     */
    // Verifie si une disponibilité existe déjà pour un secouriste à une date donnée
    private boolean exists(int idSecouriste, Journee journee) {
        String query = "SELECT 1 FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idSecouriste);
            ps.setInt(2, journee.getJour());
            ps.setInt(3, journee.getMois());
            ps.setInt(4, journee.getAnnee());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si trouvé, donc existe déjà
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Not applicable for this DAO, as availability has no unique identifier suitable for updating.
     *
     * @param d The {@link Disponibilite} to update
     * @return Always returns -1
     */
    @Override
    public int update(Disponibilite d) {
        //pas de clé primaire unique autre que tous les champs
        return -1;
    }

    /**
     * Deletes an availability from the database.
     *
     * @param d The {@link Disponibilite} to delete
     * @return Number of affected rows, or -1 if an error occurred
     */
    @Override
    public int delete(Disponibilite d) {
        String query = "DELETE FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, (int) d.getIdSecouriste());
            ps.setInt(2, d.getDateDispo().getJour());
            ps.setInt(3, d.getDateDispo().getMois());
            ps.setInt(4, d.getDateDispo().getAnnee());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Retrieves all availability entries from the database.
     *
     * @return A list of {@link Disponibilite}
     */
    @Override
    public List<Disponibilite> findAll() {
        List<Disponibilite> liste = new LinkedList<>();
        String query = "SELECT * FROM Disponibilite";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Journee j = new Journee(
                        rs.getInt("jour"),
                        rs.getInt("mois"),
                        rs.getInt("annee")
                );
                liste.add(new Disponibilite(rs.getInt("idSecouriste"), j));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

    /**
     * Finds the first availability entry by rescuer ID.
     *
     * @param id The ID of the rescuer
     * @return A {@link Disponibilite} if found, null otherwise
     */
    @Override
    public Disponibilite findByID(Long id) {
        String query = "SELECT * FROM Disponibilite WHERE idSecouriste = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Journee j = new Journee(
                            rs.getInt("jour"),
                            rs.getInt("mois"),
                            rs.getInt("annee")
                    );
                    return new Disponibilite(rs.getInt("idSecouriste"), j);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Finds all availability entries for a given rescuer.
     *
     * @param idSec The rescuer's ID
     * @return A list of {@link Disponibilite}
     */
    public List<Disponibilite> findAllBySecouriste(int idSec) {
        List<Disponibilite> liste = new LinkedList<>();
        String query = "SELECT * FROM Disponibilite WHERE idSecouriste = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idSec);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Journee j = new Journee(
                            rs.getInt("jour"),
                            rs.getInt("mois"),
                            rs.getInt("annee")
                    );
                    liste.add(new Disponibilite(rs.getInt("idSecouriste"), j));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return liste;
    }

    /**
     * Checks if a rescuer is available on a given day.
     *
     * @param idSecouriste The rescuer's ID
     * @param journee The day to check
     * @return true if the rescuer is available (not found in the table), false otherwise
     */
    public boolean isDisponible(int idSecouriste, Journee journee) {
        String query = "SELECT 1 FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idSecouriste);
            ps.setInt(2, journee.getJour());
            ps.setInt(3, journee.getMois());
            ps.setInt(4, journee.getAnnee());

            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next(); // Inverse : true s'il n'est PAS dans la table, donc DISPONIBLE
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
