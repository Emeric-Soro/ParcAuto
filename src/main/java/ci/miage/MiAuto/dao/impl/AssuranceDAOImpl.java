package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IAssuranceDAO;
import main.java.ci.miage.MiAuto.models.Assurance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DAO pour les opérations liées aux assurances
 */
public class AssuranceDAOImpl extends BaseDAOImpl<Assurance> implements IAssuranceDAO {

    /**
     * Constructeur par défaut
     */
    public AssuranceDAOImpl() {
        super();
    }

    @Override
    public Assurance findById(int id) throws SQLException {
        String query = "SELECT * FROM assurance WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Assurance> findAll() throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                assurances.add(mapResultSetToEntity(rs));
            }
        }

        return assurances;
    }

    @Override
    public Assurance save(Assurance assurance) throws SQLException {
        String query = "INSERT INTO assurance (DATE_DEBUT_ASSURANCE, DATE_FIN_ASSURANCE, AGENCE, COUT_ASSURANCE) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (assurance.getDateDebutAssurance() != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(assurance.getDateDebutAssurance()));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            if (assurance.getDateFinAssurance() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(assurance.getDateFinAssurance()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, assurance.getAgence());
            stmt.setInt(4, assurance.getCoutAssurance());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        assurance.setNumCarteAssurance(generatedKeys.getInt(1));
                        return assurance;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(Assurance assurance) throws SQLException {
        String query = "UPDATE assurance SET DATE_DEBUT_ASSURANCE = ?, DATE_FIN_ASSURANCE = ?, AGENCE = ?, COUT_ASSURANCE = ? WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (assurance.getDateDebutAssurance() != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(assurance.getDateDebutAssurance()));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            if (assurance.getDateFinAssurance() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(assurance.getDateFinAssurance()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, assurance.getAgence());
            stmt.setInt(4, assurance.getCoutAssurance());
            stmt.setInt(5, assurance.getNumCarteAssurance());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        // Supprimer d'abord les relations dans la table couvrir
        String deleteRelationsQuery = "DELETE FROM couvrir WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmtRelations = conn.prepareStatement(deleteRelationsQuery)) {

            stmtRelations.setInt(1, id);
            stmtRelations.executeUpdate();
        }

        // Ensuite supprimer l'assurance elle-même
        String query = "DELETE FROM assurance WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Assurance> findByAgence(String agence) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE AGENCE LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + agence + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToEntity(rs));
                }
            }
        }

        return assurances;
    }

    @Override
    public List<Assurance> findValides() throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE DATE_FIN_ASSURANCE > NOW()";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                assurances.add(mapResultSetToEntity(rs));
            }
        }

        return assurances;
    }

    @Override
    public List<Assurance> findExpirees() throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE DATE_FIN_ASSURANCE <= NOW()";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                assurances.add(mapResultSetToEntity(rs));
            }
        }

        return assurances;
    }

    @Override
    public List<Assurance> findProchesExpiration(int joursAvantExpiration) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE DATE_FIN_ASSURANCE > NOW() AND DATE_FIN_ASSURANCE <= DATE_ADD(NOW(), INTERVAL ? DAY)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, joursAvantExpiration);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToEntity(rs));
                }
            }
        }

        return assurances;
    }

    @Override
    public List<Assurance> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE DATE_DEBUT_ASSURANCE <= ? AND DATE_FIN_ASSURANCE >= ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(fin));
            stmt.setTimestamp(2, Timestamp.valueOf(debut));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToEntity(rs));
                }
            }
        }

        return assurances;
    }

    @Override
    public List<Assurance> findByDateExpiration(LocalDateTime dateExpiration) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String query = "SELECT * FROM assurance WHERE DATE(DATE_FIN_ASSURANCE) = DATE(?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(dateExpiration));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToEntity(rs));
                }
            }
        }

        return assurances;
    }

    @Override
    public int calculerCoutTotal(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        String query = "SELECT SUM(COUT_ASSURANCE) AS total FROM assurance WHERE DATE_DEBUT_ASSURANCE <= ? AND DATE_FIN_ASSURANCE >= ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(fin));
            stmt.setTimestamp(2, Timestamp.valueOf(debut));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }

        return 0;
    }

    /**
     * Attribue une assurance à un véhicule
     * @param numCarteAssurance Numéro de carte d'assurance
     * @param idVehicule ID du véhicule
     * @return true si l'attribution a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean assignerVehicule(int numCarteAssurance, int idVehicule) throws SQLException {
        // Vérifier si cette assurance est déjà assignée à ce véhicule
        String checkQuery = "SELECT COUNT(*) FROM couvrir WHERE NUM_CARTE_ASSURANCE = ? AND ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, numCarteAssurance);
            checkStmt.setInt(2, idVehicule);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // L'assignation existe déjà
                    return true;
                }
            }
        }

        // Insérer l'assignation
        String query = "INSERT INTO couvrir (NUM_CARTE_ASSURANCE, ID_VEHICULE) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, numCarteAssurance);
            stmt.setInt(2, idVehicule);

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Supprime l'attribution d'une assurance à un véhicule
     * @param numCarteAssurance Numéro de carte d'assurance
     * @param idVehicule ID du véhicule (0 pour supprimer toutes les attributions de cette assurance)
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur SQL
     */
    public boolean supprimerAssignationVehicule(int numCarteAssurance, int idVehicule) throws SQLException {
        String query;

        if (idVehicule > 0) {
            query = "DELETE FROM couvrir WHERE NUM_CARTE_ASSURANCE = ? AND ID_VEHICULE = ?";
        } else {
            query = "DELETE FROM couvrir WHERE NUM_CARTE_ASSURANCE = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, numCarteAssurance);

            if (idVehicule > 0) {
                stmt.setInt(2, idVehicule);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Récupère les véhicules associés à une assurance
     * @param numCarteAssurance Numéro de carte d'assurance
     * @return Liste des IDs des véhicules associés
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Integer> getVehiculesIdsForAssurance(int numCarteAssurance) throws SQLException {
        List<Integer> vehiculeIds = new ArrayList<>();
        String query = "SELECT ID_VEHICULE FROM couvrir WHERE NUM_CARTE_ASSURANCE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, numCarteAssurance);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehiculeIds.add(rs.getInt("ID_VEHICULE"));
                }
            }
        }

        return vehiculeIds;
    }

    /**
     * Récupère les assurances associées à un véhicule
     * @param idVehicule ID du véhicule
     * @return Liste des numéros de carte d'assurance associés
     * @throws SQLException En cas d'erreur SQL
     */
    public List<Integer> getAssuranceIdsForVehicule(int idVehicule) throws SQLException {
        List<Integer> assuranceIds = new ArrayList<>();
        String query = "SELECT NUM_CARTE_ASSURANCE FROM couvrir WHERE ID_VEHICULE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assuranceIds.add(rs.getInt("NUM_CARTE_ASSURANCE"));
                }
            }
        }

        return assuranceIds;
    }

    @Override
    protected Assurance mapResultSetToEntity(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();

        assurance.setNumCarteAssurance(rs.getInt("NUM_CARTE_ASSURANCE"));

        Timestamp tsDateDebut = rs.getTimestamp("DATE_DEBUT_ASSURANCE");
        if (tsDateDebut != null) {
            assurance.setDateDebutAssurance(tsDateDebut.toLocalDateTime());
        }

        Timestamp tsDateFin = rs.getTimestamp("DATE_FIN_ASSURANCE");
        if (tsDateFin != null) {
            assurance.setDateFinAssurance(tsDateFin.toLocalDateTime());
        }

        assurance.setAgence(rs.getString("AGENCE"));
        assurance.setCoutAssurance(rs.getInt("COUT_ASSURANCE"));

        return assurance;
    }
}