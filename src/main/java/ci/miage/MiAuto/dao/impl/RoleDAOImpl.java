package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IRoleDAO;
import main.java.ci.miage.MiAuto.models.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl extends BaseDAOImpl<Role> implements IRoleDAO {

    public RoleDAOImpl() {
        super();
    }

    @Override
    public Role findById(int id) throws SQLException {
        String query = "SELECT * FROM role WHERE id_role = ?";
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
    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM role";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) roles.add(mapResultSetToEntity(rs));
        }
        return roles;
    }

    @Override
    public Role save(Role r) throws SQLException {
        String query = "INSERT INTO role (nom_role) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, r.getNomRole());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        r.setIdRole(keys.getInt(1));
                        return r;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean update(Role r) throws SQLException {
        String query = "UPDATE role SET nom_role = ? WHERE id_role = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, r.getNomRole());
            stmt.setInt(2, r.getIdRole());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM role WHERE id_role = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Role findByLibelle(String libelle) throws SQLException {
        String query = "SELECT * FROM role WHERE LOWER(nom_role) = LOWER(?)";
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
        String query = "SELECT COUNT(*) FROM role WHERE LOWER(nom_role) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public boolean deleteByLibelle(String libelle) throws SQLException {
        String query = "DELETE FROM role WHERE LOWER(nom_role) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, libelle);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateLibelle(int id, String nouveauLibelle) throws SQLException {
        String query = "UPDATE role SET nom_role = ? WHERE id_role = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nouveauLibelle);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> findPrivilegesByRole(String idRole) throws SQLException {
        List<String> privileges = new ArrayList<>();
        String query = "SELECT p.libelle_privilege FROM role_privilege rp " +
                "JOIN privilege p ON rp.id_privilege = p.id_privilege " +
                "WHERE rp.id_role = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(idRole));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    privileges.add(rs.getString("libelle_privilege"));
                }
            }
        }
        return privileges;
    }

    @Override
    protected Role mapResultSetToEntity(ResultSet rs) throws SQLException {
        Role r = new Role();
        r.setIdRole(rs.getInt("id_role"));
        r.setNomRole(rs.getString("nom_role"));
        return r;
    }
}
