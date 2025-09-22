package metier.graphe.model.dao;

import metier.persistence.Affectation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


/**
 * DAO (Data Access Object) pour la table {@code Affectation}.
 * Cette classe permet l'insertion et la suppression d'affectations de compétences
 * de secouristes à des dispositifs de secours (DPS).
 */
public class AffectationDAO extends DAO<Affectation> {

    /**
     * Crée une nouvelle affectation entre un secouriste, une compétence, et un DPS.
     *
     * @param affectation L'objet {@link Affectation} à insérer
     * @return 1 si l'insertion a réussi, -1 en cas d'erreur
     */
    @Override
    public int create(Affectation affectation) {
        String query = "INSERT INTO Affectation (idSecouriste, intitule, idDPS) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, (int) affectation.getIdSecouriste());
            ps.setString(2, affectation.getIntituleComp());
            ps.setInt(3, (int) affectation.getIdDPS());

            return ps.executeUpdate(); // 1 si insertion réussie
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // erreur
        }
    }

    /**
     * La mise à jour n'est pas applicable pour une affectation (relation à trois clés).
     *
     * @param affectation L'objet {@link Affectation} à mettre à jour
     * @throws UnsupportedOperationException car l'opération est non pertinente
     */
    @Override
    public int update(Affectation affectation) {
        // Pas pertinent si la table ne contient que deux clés primaires (pas de mise à jour)
        throw new UnsupportedOperationException("Update non supporté pour Affectation.");
    }

    /**
     * Supprime une affectation spécifique (basée sur l'identifiant du secouriste, l'intitulé de la compétence, et le DPS).
     *
     * @param affectation L'objet {@link Affectation} à supprimer
     * @return 1 si la suppression a réussi, -1 en cas d'erreur
     */
    @Override
    public int delete(Affectation affectation) {
        String query = "DELETE FROM Affectation WHERE idSecouriste = ? AND intitule = ? AND idDPS = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, (int) affectation.getIdSecouriste());
            ps.setString(2, affectation.getIntituleComp());
            ps.setInt(3, (int) affectation.getIdDPS());

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Cette méthode n'est pas supportée car l'entité {@link Affectation} ne possède pas de clé primaire simple.
     *
     * @param id L'identifiant (non utilisé)
     * @throws UnsupportedOperationException car non applicable
     */
    @Override
    public Affectation findByID(Long id) {
        // Pas applicable : pas de clé primaire simple
        throw new UnsupportedOperationException("findByID non supporté pour Affectation.");
    }

    /**
     * Récupération de toutes les affectations non implémentée.
     *
     * @throws UnsupportedOperationException car non implémenté
     */
    @Override
    public List<Affectation> findAll() {
        throw new UnsupportedOperationException("findAll non implémenté pour Affectation.");
    }
}
