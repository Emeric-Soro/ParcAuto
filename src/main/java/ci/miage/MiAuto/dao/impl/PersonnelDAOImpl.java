package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IPersonnelDAO;
import main.java.ci.miage.MiAuto.models.Personnel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDAOImpl extends BaseDAOImpl<Personnel> implements IPersonnelDAO {

    public PersonnelDAOImpl() {
        super();
    }

    @Override
    public Personnel findById(int id) throws SQLException {
        String query = "SELECT * FROM personnel WHERE id_personnel = ?";
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
    public List<Personnel> findAll() throws SQLException {
        List<Personnel> list = new ArrayList<>();
        String query = "SELECT * FROM personnel";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) list.add(mapResultSetToEntity(rs));
        }
        return list;
    }

    @Override
    public Personnel save(Personnel p) throws SQLException {
        String query = "INSERT INTO personnel (nom, prenom, genre, telephone, email, id_fonction, id_service) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setString(3, p.getGenre());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getEmail());
            stmt.setInt(6, p.getIdFonction());
            stmt.setInt(7, p.getIdService());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        p.setIdPersonnel(keys.getInt(1));
                        return p;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Personnel p) throws SQLException {
        String query = "UPDATE personnel SET nom = ?, prenom = ?, genre = ?, telephone = ?, email = ?, id_fonction = ?, id_service = ? WHERE id_personnel = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setString(3, p.getGenre());
            stmt.setString(4, p.getTelephone());
            stmt.setString(5, p.getEmail());
            stmt.setInt(6, p.getIdFonction());
            stmt.setInt(7, p.getIdService());
            stmt.setInt(8, p.getIdPersonnel());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM personnel WHERE id_personnel = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Personnel mapResultSetToEntity(ResultSet rs) throws SQLException {
        Personnel p = new Personnel();
        p.setIdPersonnel(rs.getInt("id_personnel"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));
        p.setGenre(rs.getString("genre"));
        p.setTelephone(rs.getString("telephone"));
        p.setEmail(rs.getString("email"));
        p.setIdFonction(rs.getInt("id_fonction"));
        p.setIdService(rs.getInt("id_service"));
        return p;
    }
}
