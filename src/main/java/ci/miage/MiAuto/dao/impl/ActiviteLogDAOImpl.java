package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IActiviteLogDAO;
import main.java.ci.miage.MiAuto.models.ActiviteLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DAO pour les logs d'activités
 */
public class ActiviteLogDAOImpl extends BaseDAOImpl<ActiviteLog> implements IActiviteLogDAO {

    /**
     * Constructeur
     */
    public ActiviteLogDAOImpl() {
        super();
    }

    @Override
    public ActiviteLog findById(int id) throws SQLException {
        String query = "SELECT * FROM activite_log WHERE ID_ACTIVITE = ?";

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
    public List<ActiviteLog> findAll() throws SQLException {
        List<ActiviteLog> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_log ORDER BY DATE_ACTIVITE DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                activites.add(mapResultSetToEntity(rs));
            }
        }

        return activites;
    }

    @Override
    public ActiviteLog save(ActiviteLog activite) throws SQLException {
        String query = "INSERT INTO activite_log (TYPE_ACTIVITE, TYPE_REFERENCE, ID_REFERENCE, DESCRIPTION, DATE_ACTIVITE) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, activite.getTypeActivite());
            stmt.setString(2, activite.getTypeReference());
            stmt.setInt(3, activite.getIdReference());
            stmt.setString(4, activite.getDescription());

            if (activite.getDateActivite() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(activite.getDateActivite()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        activite.setIdActivite(generatedKeys.getInt(1));
                        return activite;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(ActiviteLog activite) throws SQLException {
        String query = "UPDATE activite_log SET TYPE_ACTIVITE = ?, TYPE_REFERENCE = ?, ID_REFERENCE = ?, DESCRIPTION = ?, DATE_ACTIVITE = ? WHERE ID_ACTIVITE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, activite.getTypeActivite());
            stmt.setString(2, activite.getTypeReference());
            stmt.setInt(3, activite.getIdReference());
            stmt.setString(4, activite.getDescription());

            if (activite.getDateActivite() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(activite.getDateActivite()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setInt(6, activite.getIdActivite());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM activite_log WHERE ID_ACTIVITE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<ActiviteLog> findRecent(int limit) throws SQLException {
        List<ActiviteLog> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_log ORDER BY DATE_ACTIVITE DESC LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return activites;
    }

    @Override
    public List<ActiviteLog> findByType(String typeActivite) throws SQLException {
        List<ActiviteLog> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_log WHERE TYPE_ACTIVITE = ? ORDER BY DATE_ACTIVITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, typeActivite);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return activites;
    }

    @Override
    public List<ActiviteLog> findByReference(int idReference, String typeReference) throws SQLException {
        List<ActiviteLog> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_log WHERE ID_REFERENCE = ? AND TYPE_REFERENCE = ? ORDER BY DATE_ACTIVITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idReference);
            stmt.setString(2, typeReference);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return activites;
    }

    @Override
    protected ActiviteLog mapResultSetToEntity(ResultSet rs) throws SQLException {
        ActiviteLog activite = new ActiviteLog();

        activite.setIdActivite(rs.getInt("ID_ACTIVITE"));
        activite.setTypeActivite(rs.getString("TYPE_ACTIVITE"));
        activite.setTypeReference(rs.getString("TYPE_REFERENCE"));
        activite.setIdReference(rs.getInt("ID_REFERENCE"));
        activite.setDescription(rs.getString("DESCRIPTION"));

        Timestamp date = rs.getTimestamp("DATE_ACTIVITE");
        if (date != null) {
            activite.setDateActivite(date.toLocalDateTime());
        }

        return activite;
    }
}