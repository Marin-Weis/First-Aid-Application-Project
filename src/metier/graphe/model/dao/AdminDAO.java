package metier.graphe.model.dao;

import metier.persistence.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) pour la gestion des objets {@link Admin}.
 * Fournit les opérations CRUD sur la table Admin.
 */
public class AdminDAO extends DAO<Admin> {

    /**
     * Recherche un administrateur en fonction de son email et de son mot de passe.
     *
     * @param email L'adresse email de l'administrateur
     * @param password Le mot de passe de l'administrateur
     * @return Un objet {@link Admin} si les identifiants sont valides, sinon {@code null}
     */
    public Admin findByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM Admin WHERE email = ? AND motDePasse = ?";

        try (Connection connexion = getConnection();
             PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Insère un nouvel administrateur dans la base de données.
     *
     * @param admin L'administrateur à insérer
     * @return Le nombre de lignes affectées (1 si réussi, 0 sinon)
     */
    @Override
    public int create(Admin admin) {
        String sql = "INSERT INTO Admin (nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getPrenom());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getMotDePasse());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Insère un nouvel administrateur dans la base de données.
     *
     * @param admin L'administrateur à insérer
     * @return Le nombre de lignes affectées (1 si réussi, 0 sinon)
     */
    @Override
    public int update(Admin admin) {
        String sql = "UPDATE Admin SET nom = ?, prenom = ?, email = ?, motDePasse = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getPrenom());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getMotDePasse());
            stmt.setLong(5, admin.getId());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Met à jour les informations d’un administrateur existant.
     *
     * @param admin L'administrateur avec les nouvelles données
     * @return Le nombre de lignes affectées (1 si réussi, 0 sinon)
     */
    @Override
    public int delete(Admin admin) {
        String sql = "DELETE FROM Admin WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, admin.getId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Recherche un administrateur par son identifiant.
     *
     * @param id L'identifiant de l'administrateur
     * @return L'objet {@link Admin} correspondant, ou {@code null} si introuvable
     */
    @Override
    public Admin findByID(Long id) {
        String query = "SELECT * FROM Admin WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Récupère tous les administrateurs enregistrés en base de données.
     *
     * @return Une liste d'objets {@link Admin}
     */
    @Override
    public List<Admin> findAll() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM Admin";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getLong("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse")
                );
                list.add(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
