package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IVisiteTechniqueDAO;
import main.java.ci.miage.MiAuto.models.VisiteTechnique;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les opérations liées aux visites techniques
 */
public class VisiteTechniqueDAOImpl extends BaseDAOImpl<VisiteTechnique> implements IVisiteTechniqueDAO {

    /**
     * Constructeur par défaut
     */
    public VisiteTechniqueDAOImpl() {
        super();
    }

    @Override
    public VisiteTechnique save(VisiteTechnique visiteTechnique) throws SQLException {
        String query = "INSERT INTO visite_technique (ID_VEHICULE, DATE_VISITE, DATE_EXPIRATION, RESULTAT, COUT, CENTRE_VISITE, OBSERVATIONS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, visiteTechnique.getIdVehicule());

            if (visiteTechnique.getDateVisite() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(visiteTechnique.getDateVisite()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            }

            if (visiteTechnique.getDateExpiration() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(visiteTechnique.getDateExpiration()));
            } else {
                // Par défaut, expiration dans 6 mois
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusMonths(6)));
            }

            stmt.setString(4, visiteTechnique.getResultat());
            stmt.setInt(5, visiteTechnique.getCout());
            stmt.setString(6, visiteTechnique.getCentreVisite());
            stmt.setString(7, visiteTechnique.getObservations());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        visiteTechnique.setIdVisite(generatedKeys.getInt(1));
                        return visiteTechnique;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(VisiteTechnique visiteTechnique) throws SQLException {
        String query = "UPDATE visite_technique SET ID_VEHICULE = ?, DATE_VISITE = ?, DATE_EXPIRATION = ?, " +
                "RESULTAT = ?, COUT = ?, CENTRE_VISITE = ?, OBSERVATIONS = ? WHERE ID_VISITE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, visiteTechnique.getIdVehicule());

            if (visiteTechnique.getDateVisite() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(visiteTechnique.getDateVisite()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            if (visiteTechnique.getDateExpiration() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(visiteTechnique.getDateExpiration()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setString(4, visiteTechnique.getResultat());
            stmt.setInt(5, visiteTechnique.getCout());
            stmt.setString(6, visiteTechnique.getCentreVisite());
            stmt.setString(7, visiteTechnique.getObservations());
            stmt.setInt(8, visiteTechnique.getIdVisite());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM visite_technique WHERE ID_VISITE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public VisiteTechnique findById(int id) throws SQLException {
        String query = "SELECT * FROM visite_technique WHERE ID_VISITE = ?";

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
    public List<VisiteTechnique> findAll() throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique ORDER BY DATE_VISITE DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                visites.add(mapResultSetToEntity(rs));
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findByVehicule(int idVehicule) throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE ID_VEHICULE = ? ORDER BY DATE_VISITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findValides() throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE DATE_EXPIRATION > NOW() ORDER BY DATE_EXPIRATION";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                visites.add(mapResultSetToEntity(rs));
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findExpirees() throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE DATE_EXPIRATION <= NOW() ORDER BY DATE_EXPIRATION DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                visites.add(mapResultSetToEntity(rs));
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findProchesExpiration(int joursAvantExpiration) throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE DATE_EXPIRATION > NOW() AND " +
                "DATE_EXPIRATION <= DATE_ADD(NOW(), INTERVAL ? DAY) ORDER BY DATE_EXPIRATION";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, joursAvantExpiration);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE DATE_VISITE BETWEEN ? AND ? ORDER BY DATE_VISITE";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findByResultat(String resultat) throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE RESULTAT = ? ORDER BY DATE_VISITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, resultat);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return visites;
    }

    @Override
    public List<VisiteTechnique> findByCentre(String centre) throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique WHERE CENTRE_VISITE LIKE ? ORDER BY DATE_VISITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + centre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return visites;
    }

    @Override
    protected VisiteTechnique mapResultSetToEntity(ResultSet rs) throws SQLException {
        VisiteTechnique visite = new VisiteTechnique();

        visite.setIdVisite(rs.getInt("ID_VISITE"));
        visite.setIdVehicule(rs.getInt("ID_VEHICULE"));

        Timestamp tsDateVisite = rs.getTimestamp("DATE_VISITE");
        if (tsDateVisite != null) {
            visite.setDateVisite(tsDateVisite.toLocalDateTime());
        }

        Timestamp tsDateExpiration = rs.getTimestamp("DATE_EXPIRATION");
        if (tsDateExpiration != null) {
            visite.setDateExpiration(tsDateExpiration.toLocalDateTime());
        }

        visite.setResultat(rs.getString("RESULTAT"));
        visite.setCout(rs.getInt("COUT"));
        visite.setCentreVisite(rs.getString("CENTRE_VISITE"));
        visite.setObservations(rs.getString("OBSERVATIONS"));

        return visite;
    }
}