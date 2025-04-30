package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IActiviteLogDAO;
import main.java.ci.miage.MiAuto.models.ActiviteLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActiviteLogDAOImpl extends BaseDAOImpl<ActiviteLog> implements IActiviteLogDAO {

    public ActiviteLogDAOImpl() {
        super();
    }

    @Override
    public ActiviteLog save(ActiviteLog activiteLog) throws SQLException {
        String query = "INSERT INTO activite_log (TYPE_ACTIVITE, TYPE_REFERENCE, ID_REFERENCE, DESCRIPTION, DATE_ACTIVITE) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, activiteLog.getTypeActivite());
            stmt.setString(2, activiteLog.getTypeReference());

            if (activiteLog.getIdReference() > 0) {
                stmt.setInt(3, activiteLog.getIdReference());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, activiteLog.getDescription());

            if (activiteLog.getDateActivite() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(activiteLog.getDateActivite()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        activiteLog.setIdActivite(generatedKeys.getInt(1));
                        return activiteLog;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(ActiviteLog activiteLog) throws SQLException {
        // Les logs d'activité ne devraient généralement pas être modifiés
        throw new UnsupportedOperationException("La modification des logs d'activité n'est pas supportée");
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
    public List<ActiviteLog> findByReference(String typeReference, int idReference) throws SQLException {
        List<ActiviteLog> activites = new ArrayList<>();
        String query = "SELECT * FROM activite_log WHERE TYPE_REFERENCE = ? AND ID_REFERENCE = ? ORDER BY DATE_ACTIVITE DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, typeReference);
            stmt.setInt(2, idReference);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    activites.add(mapResultSetToEntity(rs));
                }
            }
        }

        return activites;
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
    protected ActiviteLog mapResultSetToEntity(ResultSet rs) throws SQLException {
        ActiviteLog activiteLog = new ActiviteLog();

        activiteLog.setIdActivite(rs.getInt("ID_ACTIVITE"));
        activiteLog.setTypeActivite(rs.getString("TYPE_ACTIVITE"));
        activiteLog.setTypeReference(rs.getString("TYPE_REFERENCE"));
        activiteLog.setIdReference(rs.getInt("ID_REFERENCE"));
        activiteLog.setDescription(rs.getString("DESCRIPTION"));

        Timestamp tsDateActivite = rs.getTimestamp("DATE_ACTIVITE");
        if (tsDateActivite != null) {
            activiteLog.setDateActivite(tsDateActivite.toLocalDateTime());
        }

        return activiteLog;
    }
}