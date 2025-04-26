package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IRolePrivilegeDAO;
import main.java.ci.miage.MiAuto.models.RolePrivilege;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolePrivilegeDAOImpl extends BaseDAOImpl<RolePrivilege> implements IRolePrivilegeDAO {

    public RolePrivilegeDAOImpl() {
        super();
    }

    @Override
    public RolePrivilege findById(int id) throws SQLException {
        // Les rôles-privilèges ont une clé composée, donc on n'utilise pas cette méthode
        throw new UnsupportedOperationException("findById is not supported for composite keys");
    }

    @Override
    public List<RolePrivilege> findAll() throws SQLException {
        List<RolePrivilege> list = new ArrayList<>();
        String query = "SELECT * FROM role_privilege";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        }
        return list;
    }

    @Override
    public RolePrivilege save(RolePrivilege rp) throws SQLException {
        String query = "INSERT INTO role_privilege (id_role, id_privilege) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rp.getIdRole());
            stmt.setInt(2, rp.getIdPrivilege());
            stmt.executeUpdate();
            return rp;
        }
    }

    @Override
    public boolean update(RolePrivilege entity) {
        // Pas de mise à jour sur une relation pivot, on supprime et recrée si besoin
        throw new UnsupportedOperationException("update not supported on pivot table");
    }

    @Override
    public boolean delete(int id) {
        // Pas de suppression par ID sur clé composite
        throw new UnsupportedOperationException("delete by ID not supported on pivot table");
    }

    @Override
    public boolean addPrivilegeToRole(String idRole, String idPrivilege) throws SQLException {
        return false;
    }

    @Override
    public boolean removePrivilegeFromRole(String idRole, String idPrivilege) throws SQLException {
        return false;
    }

    @Override
    public int removeAllPrivilegesFromRole(String idRole) throws SQLException {
        return 0;
    }

    @Override
    public List<String> findPrivilegesIdsByRole(String idRole) throws SQLException {
        return List.of();
    }

    @Override
    public boolean exists(String idRole, String idPrivilege) throws SQLException {
        return false;
    }

    @Override
    public boolean deleteByRoleAndPrivilege(int idRole, int idPrivilege) throws SQLException {
        String query = "DELETE FROM role_privilege WHERE id_role = ? AND id_privilege = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idRole);
            stmt.setInt(2, idPrivilege);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<RolePrivilege> findByRole(int idRole) throws SQLException {
        List<RolePrivilege> list = new ArrayList<>();
        String query = "SELECT * FROM role_privilege WHERE id_role = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idRole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<RolePrivilege> findByPrivilege(int idPrivilege) throws SQLException {
        List<RolePrivilege> list = new ArrayList<>();
        String query = "SELECT * FROM role_privilege WHERE id_privilege = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPrivilege);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        }
        return list;
    }

    @Override
    protected RolePrivilege mapResultSetToEntity(ResultSet rs) throws SQLException {
        RolePrivilege rp = new RolePrivilege();
        rp.setIdRole(rs.getInt("id_role"));
        rp.setIdPrivilege(rs.getInt("id_privilege"));
        return rp;
    }
}
