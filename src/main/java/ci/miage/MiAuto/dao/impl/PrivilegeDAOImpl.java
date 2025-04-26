package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IPrivilegeDAO;
import main.java.ci.miage.MiAuto.models.Privilege;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeDAOImpl extends BaseDAOImpl<Privilege> implements IPrivilegeDAO {

    public PrivilegeDAOImpl() {
        super();
    }

    @Override
    public Privilege findById(int id) throws SQLException {
        String query = "SELECT * FROM privilege WHERE id_privilege = ?";
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
    public List<Privilege> findAll() throws SQLException {
        List<Privilege> list = new ArrayList<>();
        String query = "SELECT * FROM privilege";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) list.add(mapResultSetToEntity(rs));
        }
        return list;
    }

    @Override
    public Privilege save(Privilege p) throws SQLException {
        String query = "INSERT INTO privilege (nom_privilege, description) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNomPrivilege());
            stmt.setString(2,p.getDescription());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        p.setIdPrivilege(keys.getInt(1));
                        return p;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Privilege p) throws SQLException {
        String query = "UPDATE privilege SET nom_privilege = ?, description = ?  WHERE id_privilege = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, p.getNomPrivilege());
            stmt.setString(2,p.getDescription());
            stmt.setInt(3, p.getIdPrivilege());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM privilege WHERE id_privilege = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Privilege findByLibelle(String libelle) throws SQLException {
        String query = "SELECT * FROM privilege WHERE LOWER(nom_privilege) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }

    @Override
    public boolean existsByLibelle(String libelle) throws SQLException {
        String query = "SELECT COUNT(*) FROM privilege WHERE LOWER(nom_privilege) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean deleteByLibelle(String libelle) throws SQLException {
        String query = "DELETE FROM privilege WHERE LOWER(nom_privilege) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateLibelle(String idPrivilege, String nouveauLibelle) throws SQLException {
        return false;
    }

    @Override
    public boolean updateLibelle(int id, String nouveauLibelle) throws SQLException {
        String query = "UPDATE privilege SET nom_privilege = ? WHERE id_privilege = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nouveauLibelle);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Privilege mapResultSetToEntity(ResultSet rs) throws SQLException {
        Privilege p = new Privilege();
        p.setIdPrivilege(rs.getInt("id_privilege"));
        p.setNomPrivilege(rs.getString("nom_privilege"));
        p.setDescription(rs.getString("description"));
        return p;
    }
}
