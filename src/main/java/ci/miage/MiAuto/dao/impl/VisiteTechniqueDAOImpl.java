package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IVisiteTechniqueDAO;
import main.java.ci.miage.MiAuto.models.VisiteTechnique;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisiteTechniqueDAOImpl extends BaseDAOImpl<VisiteTechnique> implements IVisiteTechniqueDAO {

    public VisiteTechniqueDAOImpl() {
        super();
    }

    @Override
    public VisiteTechnique findById(int id) throws SQLException {
        String query = "SELECT * FROM visite_technique WHERE id_visite = ?";
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
    public List<VisiteTechnique> findAll() throws SQLException {
        List<VisiteTechnique> visites = new ArrayList<>();
        String query = "SELECT * FROM visite_technique";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) visites.add(mapResultSetToEntity(rs));
        }
        return visites;
    }

    @Override
    public VisiteTechnique save(VisiteTechnique v) throws SQLException {
        String query = "INSERT INTO visite_technique (id_vehicule, date_visite, date_expiration, centre) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, v.getIdVehicule());
            stmt.setDate(2, Date.valueOf(v.getDateVisite()));
            stmt.setDate(3, Date.valueOf(v.getDateExpiration()));
            stmt.setString(4, v.getCentre());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        v.setIdVisite(keys.getInt(1));
                        return v;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(VisiteTechnique v) throws SQLException {
        String query = "UPDATE visite_technique SET date_visite = ?, date_expiration = ?, centre = ? WHERE id_visite = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(v.getDateVisite()));
            stmt.setDate(2, Date.valueOf(v.getDateExpiration()));
            stmt.setString(3, v.getCentre());
            stmt.setInt(4, v.getIdVisite());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM visite_technique WHERE id_visite = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected VisiteTechnique mapResultSetToEntity(ResultSet rs) throws SQLException {
        VisiteTechnique v = new VisiteTechnique();
        v.setIdVisite(rs.getInt("id_visite"));
        v.setIdVehicule(rs.getInt("id_vehicule"));
        v.setDateVisite(rs.getDate("date_visite").toLocalDate());
        v.setDateExpiration(rs.getDate("date_expiration").toLocalDate());
        v.setCentre(rs.getString("centre"));
        return v;
    }
}
