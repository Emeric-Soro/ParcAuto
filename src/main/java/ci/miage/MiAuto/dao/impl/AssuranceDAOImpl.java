package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IAssuranceDAO;
import main.java.ci.miage.MiAuto.models.Assurance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssuranceDAOImpl extends BaseDAOImpl<Assurance> implements IAssuranceDAO {

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
                if (rs.next()) return mapResultSetToEntity(rs);
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
            while (rs.next()) assurances.add(mapResultSetToEntity(rs));
        }
        return assurances;
    }

    @Override
    public Assurance save(Assurance a) throws SQLException {
        String query = "INSERT INTO assurance (DATE_DEBUT_ASSURANCE, DATE_FIN_ASSURANCE, AGENCE, COUT_ASSURANCE) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (a.getDateDebutAssurance() != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(a.getDateDebutAssurance()));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            if (a.getDateFinAssurance() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(a.getDateFinAssurance()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, a.getAgence());
            stmt.setInt(4, a.getCoutAssurance());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        a.setNumCarteAssurance(generatedKeys.getInt(1));
                        return a;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public boolean update(Assurance a) throws SQLException {
        String query = "UPDATE assurance SET DATE_DEBUT_ASSURANCE = ?, DATE_FIN_ASSURANCE = ?, AGENCE = ?, COUT_ASSURANCE = ? WHERE NUM_CARTE_ASSURANCE = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (a.getDateDebutAssurance() != null) {
                stmt.setTimestamp(1, Timestamp.valueOf(a.getDateDebutAssurance()));
            } else {
                stmt.setNull(1, Types.TIMESTAMP);
            }

            if (a.getDateFinAssurance() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(a.getDateFinAssurance()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, a.getAgence());
            stmt.setInt(4, a.getCoutAssurance());
            stmt.setInt(5, a.getNumCarteAssurance());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM assurance WHERE NUM_CARTE_ASSURANCE = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Assurance mapResultSetToEntity(ResultSet rs) throws SQLException {
        Assurance a = new Assurance();
        a.setNumCarteAssurance(rs.getInt("NUM_CARTE_ASSURANCE"));
        a.setAgence(rs.getString("AGENCE"));

        Timestamp dateDebut = rs.getTimestamp("DATE_DEBUT_ASSURANCE");
        if (dateDebut != null) {
            a.setDateDebutAssurance(dateDebut.toLocalDateTime());
        }

        Timestamp dateFin = rs.getTimestamp("DATE_FIN_ASSURANCE");
        if (dateFin != null) {
            a.setDateFinAssurance(dateFin.toLocalDateTime());
        }

        a.setCoutAssurance(rs.getInt("COUT_ASSURANCE"));
        return a;
    }
}