package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IAssuranceDAO;
import main.java.ci.miage.MiAuto.models.Assurance;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssuranceDAOImpl extends BaseDAOImpl<Assurance> implements IAssuranceDAO {

    public AssuranceDAOImpl() {
        super();
    }

    @Override
    public Assurance findById(int id) throws SQLException {
        String query = "SELECT * FROM assurance WHERE num_carte_assurance = ?";
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
        String query = "INSERT INTO assurance (num_carte_assurance, compagnie, date_debut, date_fin, id_vehicule) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, a.getNumCarteAssurance());
            stmt.setString(2, a.getAgence());
            stmt.setDate(3, Date.valueOf(String.valueOf(a.getDateDebutAssurance())));
            stmt.setDate(4, Date.valueOf(String.valueOf(a.getDateFinAssurance())));
            stmt.setInt(5, a.getIdVehicule());

            int rows = stmt.executeUpdate();
            return rows > 0 ? a : null;
        }
    }

    @Override
    public boolean update(Assurance a) throws SQLException {
        String query = "UPDATE assurance SET compagnie = ?, date_debut = ?, date_fin = ?, id_vehicule = ? WHERE num_carte_assurance = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getAgence());
            stmt.setDate(2, Date.valueOf(String.valueOf(a.getDateDebutAssurance())));
            stmt.setDate(3, Date.valueOf(String.valueOf(a.getDateFinAssurance())));
            stmt.setInt(4, a.getIdVehicule());
            stmt.setInt(5, a.getNumCarteAssurance());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM assurance WHERE num_carte_assurance = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Assurance mapResultSetToEntity(ResultSet rs) throws SQLException {
        Assurance a = new Assurance();
        a.setNumCarteAssurance(rs.getInt("num_carte_assurance"));
        a.setAgence(rs.getString("compagnie"));
        a.setDateDebutAssurance(rs.getDate("date_debut").toLocalDate().atStartOfDay());
        a.setDateFinAssurance(rs.getDate("date_fin").toLocalDate().atStartOfDay());
        a.setIdVehicule(rs.getInt("id_vehicule"));
        return a;
    }
}
