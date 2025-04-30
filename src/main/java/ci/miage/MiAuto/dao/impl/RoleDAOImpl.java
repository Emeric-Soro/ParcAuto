package main.java.ci.miage.MiAuto.dao.impl;

import main.java.ci.miage.MiAuto.dao.interfaces.IRoleDAO;
import main.java.ci.miage.MiAuto.models.Privilege;
import main.java.ci.miage.MiAuto.models.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl extends BaseDAOImpl<Role> implements IRoleDAO {

    private PrivilegeDAOImpl privilegeDAO;

    public RoleDAOImpl() {
        super();
        this.privilegeDAO = new PrivilegeDAOImpl();
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

                        // Ajouter les privilèges pour ce rôle si présents
                        if (role.getPrivileges() != null && !role.getPrivileges().isEmpty()) {
                            for (Privilege privilege : role.getPrivileges()) {
                                addPrivilegeToRole(role.getIdRole(), privilege.getIdPrivilege());
                            }
                        }

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
        // Supprimer d'abord les associations avec les privilèges
        String deletePrivilegesQuery = "DELETE FROM role_privilege WHERE ID_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmtDeletePrivileges = conn.prepareStatement(deletePrivilegesQuery)) {

            stmtDeletePrivileges.setInt(1, id);
            stmtDeletePrivileges.executeUpdate();
        }

        // Puis supprimer le rôle
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
                    Role role = mapResultSetToEntity(rs);

                    // Charger les privilèges associés
                    loadPrivileges(role);

                    return role;
                }
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

            while (rs.next()) {
                Role role = mapResultSetToEntity(rs);

                // Charger les privilèges associés
                loadPrivileges(role);

                roles.add(role);
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
                    Role role = mapResultSetToEntity(rs);

                    // Charger les privilèges associés
                    loadPrivileges(role);

                    return role;
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
        } catch (SQLException e) {
            // Si le privilège existe déjà pour ce rôle, ignorer l'erreur
            if (e.getErrorCode() == 1062) { // Code d'erreur MySQL pour clé dupliquée
                return true;
            }
            throw e;
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

    /**
     * Charge les privilèges associés à un rôle
     * @param role Rôle à compléter
     * @throws SQLException En cas d'erreur SQL
     */
    private void loadPrivileges(Role role) throws SQLException {
        String query = "SELECT p.* FROM privilege p " +
                "JOIN role_privilege rp ON p.ID_PRIVILEGE = rp.ID_PRIVILEGE " +
                "WHERE rp.ID_ROLE = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, role.getIdRole());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Privilege privilege = new Privilege();
                    privilege.setIdPrivilege(rs.getInt("ID_PRIVILEGE"));
                    privilege.setNomPrivilege(rs.getString("NOM_PRIVILEGE"));
                    privilege.setDescription(rs.getString("DESCRIPTION"));

                    role.addPrivilege(privilege);
                }
            }
        }
    }
}