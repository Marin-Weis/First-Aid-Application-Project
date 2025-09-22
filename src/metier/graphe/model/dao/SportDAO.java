package metier.graphe.model.dao;

import metier.persistence.Sport;

import java.sql.*;
import java.util.*;

/**
 * DAO pour la gestion des entités {@link Sport}.
 * Fournit les opérations CRUD ainsi qu'une méthode de recherche par nom.
 */
public class SportDAO extends DAO<Sport> {

    /**
     * Insère un nouveau sport dans la base de données.
     *
     * @param sport le sport à insérer
     * @return 1 si l'insertion a réussi, -1 sinon
     */
    @Override
    public int create(Sport sport) {
        String query = "INSERT INTO Sport (nom, niveau_de_risque, competences_requises) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sport.getNom());
            ps.setString(2, sport.getNiveauDeRisque());
            ps.setString(3, sport.getCompetencesRequises());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Met à jour les informations d’un sport existant.
     *
     * @param sport le sport à mettre à jour
     * @return le nombre de lignes affectées, -1 en cas d’erreur
     */
    @Override
    public int update(Sport sport) {
        String query = "UPDATE Sport SET niveau_de_risque = ?, competences_requises = ? WHERE nom = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sport.getNiveauDeRisque());
            ps.setString(2, sport.getCompetencesRequises());
            ps.setString(3, sport.getNom());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Supprime un sport de la base de données.
     *
     * @param sport le sport à supprimer
     * @return le nombre de lignes supprimées, -1 en cas d’erreur
     */
    @Override
    public int delete(Sport sport) {
        String query = "DELETE FROM Sport WHERE nom = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, sport.getNom());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Récupère tous les sports enregistrés dans la base.
     *
     * @return une liste de tous les sports
     */
    @Override
    public List<Sport> findAll() {
        List<Sport> list = new ArrayList<>();
        String query = "SELECT * FROM Sport";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Sport(
                        rs.getString("nom"),
                        rs.getString("niveau_de_risque"),
                        rs.getString("competences_requises")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Recherche d’un sport par son identifiant.
     * Inapplicable ici car la clé primaire est un String (nom).
     *
     * @param id identifiant numérique (non utilisé)
     * @return toujours null
     */
    @Override
    public Sport findByID(Long id) {
        return null; // La clé primaire est un String, donc findByID ne s'applique pas ici
    }

    /**
     * Recherche d’un sport par son nom.
     *
     * @param nom le nom du sport
     * @return l’objet {@link Sport} correspondant, ou null si non trouvé
     */
    public Sport findByNom(String nom) {
        String query = "SELECT * FROM Sport WHERE nom = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nom);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Sport(
                        rs.getString("nom"),
                        rs.getString("niveau_de_risque"),
                        rs.getString("competences_requises")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
