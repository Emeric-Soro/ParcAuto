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
    public Role save(Role role) throws SQLException {
        String query = "INSERT INTO role (NOM_ROLE, DESCRIPTION) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, role.getNomRole());
            stmt.setString(2, role.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        role.setIdRole(generatedKeys.getInt(1));
                        return role;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public boolean update(Role role) throws SQLException {
        String query = "UPDATE role SET NOM_ROLE = ?, DESCRIPTION = ? WHERE ID_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role.getNomRole());
            stmt.setString(2, role.getDescription());
            stmt.setInt(3, role.getIdRole());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String query = "DELETE FROM role WHERE ID_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Role findById(int id) throws SQLException {
        String query = "SELECT * FROM role WHERE ID_ROLE = ?";

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
    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String query = "SELECT * FROM role ORDER BY ID_ROLE";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                roles.add(mapResultSetToEntity(rs));
            }
        }

        return roles;
    }

    @Override
    public Role findByName(String nomRole) throws SQLException {
        String query = "SELECT * FROM role WHERE NOM_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nomRole);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public boolean addPrivilegeToRole(int idRole, int idPrivilege) throws SQLException {
        String query = "INSERT INTO role_privilege (ID_ROLE, ID_PRIVILEGE) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRole);
            stmt.setInt(2, idPrivilege);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removePrivilegeFromRole(int idRole, int idPrivilege) throws SQLException {
        String query = "DELETE FROM role_privilege WHERE ID_ROLE = ? AND ID_PRIVILEGE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRole);
            stmt.setInt(2, idPrivilege);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    protected Role mapResultSetToEntity(ResultSet rs) throws SQLException {
        Role role = new Role();

        role.setIdRole(rs.getInt("ID_ROLE"));
        role.setNomRole(rs.getString("NOM_ROLE"));
        role.setDescription(rs.getString("DESCRIPTION"));

        return role;
    }
}